package com.lodong.spring.supermandiary.service.main;

import com.lodong.spring.supermandiary.domain.file.FileList;
import com.lodong.spring.supermandiary.domain.file.WorkFile;
import com.lodong.spring.supermandiary.domain.working.NowWorkInfo;
import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import com.lodong.spring.supermandiary.dto.main.MyWorkDto;
import com.lodong.spring.supermandiary.repo.NowWorkRepository;
import com.lodong.spring.supermandiary.repo.UserConstructorRepository;
import com.lodong.spring.supermandiary.repo.WorkDetailRepository;
import com.lodong.spring.supermandiary.repo.WorkingRepository;
import com.lodong.spring.supermandiary.repo.file.FileRepository;
import com.lodong.spring.supermandiary.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MainService {
    private final WorkDetailRepository workDetailRepository;
    private final UserConstructorRepository userConstructorRepository;
    private final WorkingRepository workingRepository;

    private final String STORAGE_ROOT_PATH = "/home/lodong/TestStorage/";
    private final String File_PATH = "work-file/";

    private final FileRepository fileRepository;
    private final NowWorkRepository nowWorkRepository;

    @Transactional(readOnly = true)
    public List<MyWorkDto> getMyWork(String userUuid, String constructorId) {
        List<WorkDetail> workDetailList = workDetailRepository
                .findByUserConstructorIdAndWorkingConstructorId(userUuid, constructorId)
                .orElseThrow(() -> new NullPointerException("오늘 작업이 존재하지 않습니다."));
        List<MyWorkDto> workDtos = new ArrayList<>();

        workDetailList.stream().forEach(workDetail -> {
            String workId = workDetail.getWorking().getId();
            String workDetailId = workDetail.getId();
            String workLevel = workDetail.getName();
            String workLevelId = workDetail.getId();
            String productName = workDetail.getWorking().getConstructorProduct().getName();
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
            MyWorkDto myWorkDto = new MyWorkDto(workId, workDetailId, workLevel, workLevelId, productName, homeName, homeDong, homeHosu, estimateWorkDate, estimateWorkTime, isInFileIn, isIsComplete);
            workDtos.add(myWorkDto);
        });
        return workDtos;
    }

    @Transactional
    public void completeWorkNoFile(String workDetailId) {
        //완료처리
        WorkDetail workDetail = workDetailRepository
                .findById(workDetailId)
                .orElseThrow(() -> new NullPointerException("존재하지 않는 작업입니다."));

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
        if(nextWork != null){
            nowWorkInfo.setWorkDetail(nextWork);
            nowWorkRepository.save(nowWorkInfo);
        }else{
            workingRepository.completeWorking(workId, DateUtil.getNowDateTime());
        }
    }

    @Transactional
    public void completeWork(List<MultipartFile> images, String workDetailId) throws IOException {
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

                WorkDetail workDetail = WorkDetail.builder()
                        .id(workDetailId)
                        .build();

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
        completeWorkNoFile(workDetailId);
    }
    @Transactional
    public void completeWorkingPay(String workId, String method){
        workingRepository.completePaying(workId, method, DateUtil.getNowDateTime());
    }

    private void saveFile(MultipartFile file, String storage) throws IOException {
        File saveFile = new File(storage);
        file.transferTo(saveFile);
    }

}
