package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import com.lodong.spring.supermandiary.dto.calendar.*;
import com.lodong.spring.supermandiary.repo.AffiliatedInfoRepository;
import com.lodong.spring.supermandiary.repo.ConstructorProductRepository;
import com.lodong.spring.supermandiary.repo.ConstructorProductWorkListRepository;
import com.lodong.spring.supermandiary.repo.WorkDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {
    private final WorkDetailRepository workDetailRepository;
    private final AffiliatedInfoRepository affiliatedInfoRepository;
    private final ConstructorProductRepository constructorProductRepository;
    private final ConstructorProductWorkListRepository constructorProductWorkListRepository;


    @Transactional(readOnly = true)
    public WorkListDto getWorkList(String constructorId) throws NullPointerException{
        List<WorkDetail> workDetailList = workDetailRepository
                .findByWorkingConstructorId(constructorId)
                .orElse(new ArrayList<>());
        List<WorkerWithHolidayDto> workerList = getWorkerList(constructorId);
        if(workDetailList.size()==0 && workerList.size() == 0){
            throw new NullPointerException("작업자 및 작업이 없음");
        }

        WorkListDto workListDto = new WorkListDto();
        List<WorkDetailByConstructorDto> workDetailByConstructorDtoList = new ArrayList<>();
        workListDto.setWorkerList(workerList);

        for (WorkDetail workDetail : workDetailList) {
            if (workDetail.getUserConstructor() != null) {
                WorkDetailByConstructorDto workDetailByConstructorDto = new WorkDetailByConstructorDto();
                // 공통 변수
                workDetailByConstructorDto.setWorkId(workDetail.getWorking().getId());
                workDetailByConstructorDto.setWorkDetailId(workDetail.getId());
                workDetailByConstructorDto.setWorkerId(workDetail.getUserConstructor().getId());
                workDetailByConstructorDto.setWorkerName(workDetail.getUserConstructor().getName());
                workDetailByConstructorDto.setProductName(workDetail.getWorking().getConstructorProduct().getProduct().getName());
                workDetailByConstructorDto.setWorkLevelName(workDetail.getName());
                workDetailByConstructorDto.setEstimateWorkDate(workDetail.getEstimateWorkDate());
                workDetailByConstructorDto.setEstimateWorkTime(workDetail.getEstimateWorkTime());
                workDetailByConstructorDto.setComplete(workDetail.isComplete());
                // 주소 구분
                if (workDetail.getWorking().getApartment() != null) {
                    workDetailByConstructorDto.setHomeName(workDetail.getWorking().getApartment().getName());
                    workDetailByConstructorDto.setDong(workDetail.getWorking().getApartmentDong());
                    workDetailByConstructorDto.setHosu(workDetail.getWorking().getApartmentHosu());
                } else if (workDetail.getWorking().getOtherHome() != null) {
                    workDetailByConstructorDto.setHomeName(workDetail.getWorking().getOtherHome().getName());
                    workDetailByConstructorDto.setDong(workDetail.getWorking().getOtherHomeDong());
                    workDetailByConstructorDto.setHosu(workDetail.getWorking().getOtherHomeHosu());
                }
                workDetailByConstructorDtoList.add(workDetailByConstructorDto);
            }
        }
        workListDto.setWorkDetailByConstructorDtoList(workDetailByConstructorDtoList);

        return workListDto;
    }

    @Transactional(readOnly = true)
    public FilterDataDto getFilterData(String constructorId) {
        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository
                .findByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 작업 가능한 사람이 없습니다."));
        List<ConstructorProduct> constructorProducts = constructorProductRepository
                .findConstructorProductByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 등록된 작업 목록이 없습니다."));
        List<ConstructorProductWorkList> constructorProductWorkLists = constructorProductWorkListRepository
                .findByConstructorProduct_Constructor_Id(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 등록된 현재 작업 상태 목록이 없습니다."));

        FilterDataDto filterDataDto = new FilterDataDto();
        List<WorkFilterDto> workFilterDtos = new ArrayList<>();
        List<WorkerFilterDto> workerFilterDtos = new ArrayList<>();
        List<WorkDetailFilterDto> workDetailFilterDtos = new ArrayList<>();

        affiliatedInfos.forEach(affiliatedInfo -> {
            if (affiliatedInfo.getUserConstructor().isActive()) {
                WorkFilterDto workFilterDto = new WorkFilterDto();
                workFilterDto.setId(affiliatedInfo.getUserConstructor().getId());
                workFilterDto.setName(affiliatedInfo.getUserConstructor().getName());
                workFilterDtos.add(workFilterDto);
            }
        });

        constructorProducts.forEach(constructorProduct -> {
            WorkerFilterDto workerFilterDto = new WorkerFilterDto(constructorProduct.getId(), constructorProduct.getProduct().getName());
            workerFilterDtos.add(workerFilterDto);
        });
        constructorProductWorkLists.forEach(constructorProductWorkList -> {
            String name = constructorProductWorkList.getName();
            boolean isIn = false;
            for (WorkDetailFilterDto workDetailFilterDto : workDetailFilterDtos) {
                if (workDetailFilterDto.getName().equals(name)) {
                    isIn = true;
                    break;
                }
            }
            if (!isIn) {
                WorkDetailFilterDto workDetailFilterDto = new WorkDetailFilterDto();
                workDetailFilterDto.setName(constructorProductWorkList.getName());
                workDetailFilterDtos.add(workDetailFilterDto);
            }
        });

        filterDataDto.setWorkerFilterList(workerFilterDtos);
        filterDataDto.setWorkFilterList(workFilterDtos);
        filterDataDto.setWorkDetailFilterList(workDetailFilterDtos);
        return filterDataDto;
    }

    @Transactional
    public void allocateWorkDetail(String workDetailId, LocalDate date, LocalTime time, String workerId, String note) throws NullPointerException {
        //이전 작업이 등록돼있는가 sequence가 2이상인 경우
        WorkDetail workDetail = workDetailRepository
                .findById(workDetailId)
                .orElseThrow(() -> new NullPointerException("해당 작업은 존재하지 않습니다."));

        String workId = workDetail.getWorking().getId();
        int sequence = workDetail.getSequence();
        if (sequence >= 2) {
            WorkDetail beforeWorkDetail = workDetailRepository
                    .findByWorkingIdAndSequence(workId, sequence - 1)
                    .orElseThrow(() -> new NullPointerException("그 전 작업이 없음"));
            if (beforeWorkDetail.getUserConstructor() == null) {
                throw new NullPointerException("전단계 작업에 담당자가 할당 되지 않았습니다. 전단계 : " + beforeWorkDetail.getName() + " 할당하려는 작업 단계: " + workDetail.getName());
            }
        }
        workDetailRepository.updateWorkDetail(workDetailId, date, time, workerId, note);
    }

    @Transactional(readOnly = true)
    public List<WorkerWithHolidayDto> getWorkerList(String constructorId){
        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository
                .findByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 작업 가능한 사람이 없습니다."));
        List<WorkerWithHolidayDto> workerWithHolidayDtos = new ArrayList<>();
        affiliatedInfos.forEach(affiliatedInfo -> {
            if (affiliatedInfo.getUserConstructor().isActive()) {
                List<WorkerHolidayDto> workerHolidayDtoList = new ArrayList<>();
                affiliatedInfo.getUserConstructor().getUserConstructorHolidayList().forEach(userConstructorHoliday -> {
                    WorkerHolidayDto workerHolidayDto = new WorkerHolidayDto(userConstructorHoliday.getDate());
                    workerHolidayDtoList.add(workerHolidayDto);
                });
                WorkerWithHolidayDto workerWithHolidayDto = new WorkerWithHolidayDto(affiliatedInfo.getUserConstructor().getId(),affiliatedInfo.getUserConstructor().getName(),workerHolidayDtoList);

                workerWithHolidayDtos.add(workerWithHolidayDto);
            }
        });
        return workerWithHolidayDtos;
    }
}
