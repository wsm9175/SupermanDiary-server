package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import com.lodong.spring.supermandiary.dto.calendar.*;
import com.lodong.spring.supermandiary.dto.working.UserConstructorDto;
import com.lodong.spring.supermandiary.repo.AffiliatedInfoRepository;
import com.lodong.spring.supermandiary.repo.ConstructorProductRepository;
import com.lodong.spring.supermandiary.repo.ConstructorProductWorkListRepository;
import com.lodong.spring.supermandiary.repo.WorkDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CalendarService {
    private final WorkDetailRepository workDetailRepository;
    private final AffiliatedInfoRepository affiliatedInfoRepository;
    private final ConstructorProductRepository constructorProductRepository;
    private final ConstructorProductWorkListRepository constructorProductWorkListRepository;


    public HashMap<String, List<WorkDetailByConstructorDto>> getWorkListByDate(String constructorId, LocalDate date) {
        List<WorkDetail> workDetailList = workDetailRepository
                .findByWorkingConstructorIdAndEstimateWorkDate(constructorId, date)
                .orElseThrow(() -> new NullPointerException("해당 일자에 작업이 없습니다."));

        HashMap<String, List<WorkDetailByConstructorDto>> map = new HashMap<>();
        for (WorkDetail workDetail : workDetailList) {
            // 키 값
            String worker = workDetail.getUserConstructor().getName();
            // 공통 변수
            WorkDetailByConstructorDto workDetailByConstructorDto = new WorkDetailByConstructorDto();
            workDetailByConstructorDto.setWorkId(workDetail.getWorking().getId());
            workDetailByConstructorDto.setWorkDetailId(workDetail.getId());
            workDetailByConstructorDto.setProductName(workDetail.getWorking().getConstructorProduct().getName());
            workDetailByConstructorDto.setWorkLevelName(workDetail.getConstructorProductWorkList().getName());
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
            if (map.get(worker) == null) {
                List<WorkDetailByConstructorDto> list = new ArrayList<>();
                list.add(workDetailByConstructorDto);
                map.put(worker, list);
            } else {
                map.get(worker).add(workDetailByConstructorDto);
            }
        }

        // 미배정 태스크
        List<WorkDetail> unAssignedWorkList = workDetailRepository
                .findByWorkingConstructorIdAndEstimateWorkDateIsNull(constructorId)
                .orElseThrow(() -> new NullPointerException("미배정 작업이 없습니다."));

        for (WorkDetail workDetail : unAssignedWorkList) {
            // 키 값
            String worker = "미배정 작업";
            // 공통 변수
            WorkDetailByConstructorDto workDetailByConstructorDto = new WorkDetailByConstructorDto();
            workDetailByConstructorDto.setWorkId(workDetail.getWorking().getId());
            workDetailByConstructorDto.setWorkDetailId(workDetail.getId());
            workDetailByConstructorDto.setProductName(workDetail.getWorking().getConstructorProduct().getName());
            workDetailByConstructorDto.setWorkLevelName(workDetail.getConstructorProductWorkList().getName());
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
            if (map.get(worker) == null) {
                List<WorkDetailByConstructorDto> list = new ArrayList<>();
                list.add(workDetailByConstructorDto);
                map.put(worker, list);
            } else {
                map.get(worker).add(workDetailByConstructorDto);
            }
        }

        return map;
    }

    public FilterDataDto getFilterData(String constructorId) {
        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository
                .findByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 작업 가능한 사람이 없습니다."));
        List<ConstructorProduct> constructorProducts = constructorProductRepository
                .findConstructorProductByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 등록된 작업 목록이 없습니다."));
        List<ConstructorProductWorkList> constructorProductWorkLists  = constructorProductWorkListRepository
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
            WorkerFilterDto workerFilterDto = new WorkerFilterDto();
            workerFilterDto.setId(constructorProduct.getId());
            workerFilterDto.setName(constructorProduct.getName());
            workerFilterDtos.add(workerFilterDto);
        });
        constructorProductWorkLists.forEach(constructorProductWorkList -> {
            String name = constructorProductWorkList.getName();
            boolean isIn = false;
            for(WorkDetailFilterDto workDetailFilterDto:workDetailFilterDtos){
                if(workDetailFilterDto.getName().equals(name)){
                    isIn = true;
                    break;
                }
            }
            if(!isIn){
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
}
