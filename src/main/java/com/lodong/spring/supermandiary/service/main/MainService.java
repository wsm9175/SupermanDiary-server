package com.lodong.spring.supermandiary.service.main;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorAlarm;
import com.lodong.spring.supermandiary.domain.file.FileList;
import com.lodong.spring.supermandiary.domain.file.WorkFile;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.domain.working.NowWorkInfo;
import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import com.lodong.spring.supermandiary.dto.main.*;
import com.lodong.spring.supermandiary.repo.*;
import com.lodong.spring.supermandiary.repo.file.FileRepository;
import com.lodong.spring.supermandiary.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final WorkDetailRepository workDetailRepository;
    private final UserConstructorRepository userConstructorRepository;
    private final WorkingRepository workingRepository;

    private final ConstructorRepository constructorRepository;

    private final String STORAGE_ROOT_PATH = "/app/";
    private final String File_PATH = "work-file/";

    private final FileRepository fileRepository;
    private final NowWorkRepository nowWorkRepository;
    private final RequestOrderRepository requestOrderRepository;
    private final ConstructorAlarmRepository constructorAlarmRepository;
    private final AffiliatedInfoRepository affiliatedInfoRepository;

    @Transactional(readOnly = true)
    public List<MyWorkDto> getMyWork(String userUuid, String constructorId) {

        List<WorkDetail> workDetailList = workDetailRepository
                .findByUserConstructorNotNullAndWorkingConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("작업이 존재하지 않습니다."));

        List<MyWorkDto> workDtos = new ArrayList<>();

        Optional.ofNullable(workDetailList).orElseGet(Collections::emptyList).forEach(workDetail -> {
            String workId = workDetail.getWorking().getId();
            String workDetailId = workDetail.getId();
            String workLevel = workDetail.getName();
            String workLevelId = workDetail.getId();
            String productName = workDetail.getWorking().getConstructorProduct().getProduct().getName();
            LocalDate estimateWorkDate = workDetail.getEstimateWorkDate();
            LocalTime estimateWorkTime = workDetail.getEstimateWorkTime();
            boolean isInFileIn = workDetail.isFileIn();
            boolean isIsComplete = workDetail.isComplete();
            String homeName = null;
            String homeDong = null;
            String homeHosu = null;
            if (workDetail.getWorking().getApartment() != null) {
                homeName = workDetail.getWorking().getApartment().getName();
                homeDong = workDetail.getWorking().getApartmentDong();
                homeHosu = workDetail.getWorking().getApartmentHosu();
            } else if (workDetail.getWorking().getOtherHome() != null) {
                homeName = workDetail.getWorking().getOtherHome().getName();
                homeDong = workDetail.getWorking().getOtherHomeDong();
                homeHosu = workDetail.getWorking().getOtherHomeHosu();
            }
            String workerName = workDetail.getUserConstructor().getName();
            String workerId = workDetail.getUserConstructor().getId();
            boolean isIsMine = workerId.equals(userUuid);
            MyWorkDto myWorkDto = new MyWorkDto(workId, workDetailId, workLevel, workLevelId, productName, homeName, homeDong, homeHosu, estimateWorkDate, estimateWorkTime, isInFileIn, isIsComplete, isIsMine, workerName, workerId, workDetail.getStatus());
            workDtos.add(myWorkDto);
        });
        return workDtos;
    }

    @Transactional
    public NextWorkDetailDTO completeWorkNoFile(String workDetailId) throws RuntimeException {
        //완료처리
        WorkDetail workDetail = workDetailRepository
                .findById(workDetailId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 작업입니다."));

        if (workDetail.isComplete()) throw new RuntimeException("이미 완료된 작업입니다.");

        workDetail.setActualWorkDate(DateUtil.getNowDate());
        workDetail.setActualWorkTime(DateUtil.getNowTime());
        workDetail.setComplete(true);
        workDetailRepository.save(workDetail);

        //now work 수정
        String workId = workDetail.getWorking().getId();
        NowWorkInfo nowWorkInfo = workDetail.getWorking().getNowWorkInfo();
        int sequence = workDetail.getSequence();
        //현재 설정된 workDetail을 다음 sequence의 workDetail로 변환. 만약 다음 sequence가 없다면 변화X
        WorkDetail nextWork = workDetailRepository
                .findByWorkingIdAndSequence(workId, sequence + 1)
                .orElse(null);

        //만약 다음 sequence가 없다면 큰 작업은 완료된 셈
        if (nextWork != null) {
            nowWorkInfo.setWorkDetail(nextWork);
            nowWorkRepository.save(nowWorkInfo);
            return new NextWorkDetailDTO(nextWork.getId(), Optional.ofNullable(nextWork.getUserConstructor().getName()).orElse(null), nextWork.getNote(), nextWork.getEstimateWorkDate(), nextWork.getEstimateWorkTime(), nextWork.getName(), nextWork.getSequence());
        } else {
            workingRepository.completeWorking(workId, DateUtil.getNowDateTime());
            return null;
        }
    }

    @Transactional
    public NextWorkDetailDTO completeWork(List<MultipartFile> images, String workDetailId) throws IOException, RuntimeException {
        WorkDetail workDetail = workDetailRepository
                .findById(workDetailId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 작업입니다."));

        if (workDetail.isComplete()) throw new RuntimeException("이미 완료된 작업입니다.");

        List<FileList> fileLists = new ArrayList<>();

        int count = 0;
        if (images != null) {
            for (MultipartFile image : images) {
                String fileName = workDetailId + count++ + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf("."));
                String storage = STORAGE_ROOT_PATH + File_PATH + fileName;
                FileList fileList = new FileList();
                fileList.setId(UUID.randomUUID().toString());
                fileList.setName(fileName);
                fileList.setExtension(image.getContentType());
                fileList.setStorage(storage);
                fileList.setCreateAt(DateUtil.getNowDateTime().toString());
                fileLists.add(fileList);
                saveFile(image, storage);

                WorkFile workFile = WorkFile.builder()
                        .id(UUID.randomUUID().toString())
                        .fileList(fileList)
                        .workDetail(workDetail)
                        .build();

                fileList.setWorkFile(workFile);
                fileLists.add(fileList);
            }
            fileRepository.saveAll(fileLists);
        }
        return completeWorkNoFile(workDetailId);
    }

    @Transactional
    public void completeWorkingPay(String workId, String method) {
        workingRepository.completePaying(workId, method, DateUtil.getNowDateTime());
    }

    @Transactional(readOnly = true)
    public List<AlarmDTO> getAlarmList(String uuid) {
        List<ConstructorAlarm> constructorAlarmList = constructorAlarmRepository.findByConstructor_IdAndIsReadAlarm(uuid, false)
                .orElseGet(Collections::emptyList);
        List<AlarmDTO> alarmDTOS = new ArrayList<>();
        constructorAlarmList.forEach(userCustomerAlarm -> {
            AlarmDTO alarmDTO = new AlarmDTO(userCustomerAlarm.getId(), userCustomerAlarm.getKind(), userCustomerAlarm.getContent(), "해당 견적서는 삭제되었습니다.", userCustomerAlarm.getCreateAt());
            requestOrderRepository.findById(userCustomerAlarm.getContent()).ifPresent(requestOrder -> {
                String homeName = requestOrder.getApartment() != null ? requestOrder.getApartment().getName() : requestOrder.getOtherHome().getName();
                alarmDTO.setContent(homeName);
            });
            alarmDTOS.add(alarmDTO);
        });
        return alarmDTOS;
    }

    @Transactional
    public void readAlarm(String alarmId) throws NullPointerException {
        constructorAlarmRepository.findById(alarmId)
                .orElseThrow(() -> new NullPointerException("해당 알림은 존재하지 않습니다."));

        constructorAlarmRepository.updateReadAlarm(true, alarmId);
    }

    @Transactional(readOnly = true)
    public MyInfoDTO getMyInfo(String myUuid) throws NullPointerException {
        UserConstructor userConstructor = userConstructorRepository.findById(myUuid).orElseThrow(() -> new NullPointerException("유저 정보를 조회할 수 없습니다."));
        AffiliatedInfo affiliatedInfo = affiliatedInfoRepository.findByUserConstructor(userConstructor).orElse(null);
        List<String> techList = Optional.ofNullable(userConstructor.getUserConstructorTeches()).orElseGet(Collections::emptyList).stream().map(userConstructorTech -> userConstructorTech.getProduct().getName()).toList();
        if (affiliatedInfo == null) {
            return new MyInfoDTO(userConstructor.getName(), userConstructor.getPhoneNumber(), userConstructor.getEmail(), userConstructor.getCareer(), null, techList, userConstructor.isActive(), userConstructor.isCeo());
        } else {
            return new MyInfoDTO(userConstructor.getName(), userConstructor.getPhoneNumber(), userConstructor.getEmail(), userConstructor.getCareer(), affiliatedInfo.getConstructor().getName(), techList, userConstructor.isActive(), userConstructor.isCeo());
        }
    }
    @Transactional
    public void readAllAlarm(ReadAllAlarmDTO readAllAlarmDTOS) {
        for (String alarmId : readAllAlarmDTOS.getAlarmList()) {
            constructorAlarmRepository.updateReadAlarm(true, alarmId);
        }
    }
    @Transactional(readOnly = true)
    public PayActivationDTO isPayActivation(String constructorUid) {
        Constructor constructor = constructorRepository.findById(constructorUid)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 시공사입니다."));
        return new PayActivationDTO(constructor.isPayActivation());
    }
    private void saveFile(MultipartFile file, String storage) throws IOException {
        File saveFile = new File(storage);
        file.transferTo(saveFile);
    }

}
