package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.working.Working;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.dto.working.UserConstructorDto;
import com.lodong.spring.supermandiary.dto.working.WorkApartmentDto;
import com.lodong.spring.supermandiary.dto.working.WorkDetailDto;
import com.lodong.spring.supermandiary.dto.working.WorkLevelDto;
import com.lodong.spring.supermandiary.repo.AffiliatedInfoRepository;
import com.lodong.spring.supermandiary.repo.WorkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WorkingService {
    private final WorkingRepository workingRepository;
    private final AffiliatedInfoRepository affiliatedInfoRepository;

    public HashMap<String, List<WorkApartmentDto>> getWorkList(String constructorId, int siggCode) {
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();
        List<Working> workingList = workingRepository
                .findByConstructor(constructor)
                .orElseThrow(() -> new NullPointerException("working list가 존재하지 않음"));


        HashMap<String, List<WorkApartmentDto>> workList = new HashMap<>();
        for (Working working : workingList) {
            if (working.getApartment() != null) {
                if (working.getApartment().getSiggCode() == siggCode) {
                    String key = working.getApartment().getName();
                    WorkApartmentDto workApartmentDto = new WorkApartmentDto();
                    workApartmentDto.setApartment(working.getApartment().getName());
                    workApartmentDto.setHosu(working.getApartmentHosu());
                    workApartmentDto.setDong(working.getApartmentDong());
                    workApartmentDto.setWorkId(working.getId());

                    if (workList.get(working.getApartment().getName()) == null) {
                        List<WorkApartmentDto> list = new ArrayList<>();
                        list.add(workApartmentDto);
                        workList.put(key, list);
                    } else {
                        workList.get(key).add(workApartmentDto);
                    }

                }
            } else if (working.getOtherHome() != null) {
                if (working.getOtherHome().getSiggAreas().getCode() == siggCode) {
                    String key = working.getOtherHome().getName();
                    WorkApartmentDto workApartmentDto = new WorkApartmentDto();
                    workApartmentDto.setApartment(working.getOtherHome().getName());
                    workApartmentDto.setHosu(working.getOtherHomeHosu());
                    workApartmentDto.setDong(working.getOtherHomeDong());
                    workApartmentDto.setWorkId(working.getId());

                    if (workList.get(working.getOtherHome().getName()) == null) {
                        List<WorkApartmentDto> list = new ArrayList<>();
                        list.add(workApartmentDto);
                        workList.put(key, list);
                    } else {
                        workList.get(key).add(workApartmentDto);
                    }
                }
            }
        }
        return workList;
    }

    public List<WorkApartmentDto> findWork(String constructorId, String phoneNumber, String dong, String hosu) {
        //회원
        List<Working> memberWorkingList = null;
        //비회원
        List<Working> noneMemberWokringList = null;
        List<Working> joined = new ArrayList<>();
        if(phoneNumber != null){
            //회원
            memberWorkingList = workingRepository
                    .findByConstructorIdAndUserCustomer_PhoneNumbers_phoneNumber(constructorId, phoneNumber)
                    .orElse(new ArrayList<>());
            //비회원
            noneMemberWokringList = workingRepository
                    .findByConstructorIdAndNonMemberPhoneNumber(constructorId, phoneNumber)
                    .orElse(new ArrayList<>());
        }else if(dong != null && hosu != null){
            //아파트
            memberWorkingList = workingRepository
                    .findByConstructorIdAndApartmentDongAndApartmentHosu(constructorId, dong, hosu)
                    .orElse(new ArrayList<>());
            //그외 건물
            noneMemberWokringList = workingRepository
                    .findByConstructorIdAndOtherHomeDongAndOtherHomeHosu(constructorId, dong, hosu)
                    .orElse(new ArrayList<>());
        }else if(dong == null){
            //아파트
            memberWorkingList = workingRepository
                    .findByConstructorIdAndApartmentHosu(constructorId, hosu)
                    .orElse(new ArrayList<>());
            //그외 건물
            noneMemberWokringList = workingRepository
                    .findByConstructorIdAndOtherHomeHosu(constructorId, hosu)
                    .orElse(new ArrayList<>());
        }else {
            memberWorkingList = workingRepository
                    .findByConstructorIdAndApartmentDong(constructorId, dong)
                    .orElse(new ArrayList<>());
            //그외 건물
            noneMemberWokringList = workingRepository
                    .findByConstructorIdAndOtherHomeDong(constructorId, dong)
                    .orElse(new ArrayList<>());
        }
        joined.addAll(memberWorkingList);
        joined.addAll(noneMemberWokringList);

        List<WorkApartmentDto> workApartmentDtos = new ArrayList<>();

        for (Working working : joined) {
            WorkApartmentDto workApartmentDto = new WorkApartmentDto();
            workApartmentDto.setWorkId(working.getId());
            if (working.getApartment() != null) {
                workApartmentDto.setApartment(working.getApartment().getName());
                workApartmentDto.setDong(working.getApartmentDong());
                workApartmentDto.setHosu(working.getApartmentHosu());
            } else if (working.getOtherHome() != null) {
                workApartmentDto.setApartment(working.getOtherHome().getName());
                workApartmentDto.setDong(working.getOtherHomeDong());
                workApartmentDto.setHosu(working.getApartmentHosu());
            }
            workApartmentDtos.add(workApartmentDto);
        }
        return workApartmentDtos;

    }

    public WorkDetailDto getWorkDetailByWork(String constructorId, String workId) {
        Working working = workingRepository
                .findByIdAndConstructorId(workId, constructorId)
                .orElseThrow(() -> new NullPointerException("해당 작업은 존재하지 않습니다."));

        WorkDetailDto workDetailDto = new WorkDetailDto();
        //apart or other 건물
        if (working.getApartment() != null) {
            workDetailDto.setHomeName(working.getApartment().getName());
            workDetailDto.setHomeDong(working.getApartmentDong());
            workDetailDto.setHomeHosu(working.getApartmentHosu());
        } else if (working.getOtherHome().getName() != null) {
            workDetailDto.setHomeName(working.getOtherHome().getName());
            workDetailDto.setHomeDong(working.getOtherHomeDong());
            workDetailDto.setHomeHosu(working.getOtherHomeHosu());
        }
        //회원 or 비회원
        if (working.getUserCustomer() != null) {
            workDetailDto.setCustomerName(working.getUserCustomer().getName());
            workDetailDto.setCustomerPhoneNumber(working.getUserCustomer().getName());
            workDetailDto.setMember(true);
        } else if (working.getNonMemberName() != null) {
            workDetailDto.setCustomerName(working.getNonMemberName());
            workDetailDto.setCustomerPhoneNumber(working.getNonMemberPhoneNumber());
            workDetailDto.setMember(false);
        }

        // 상품 이름, 가격, 비고
        workDetailDto.setProductName(working.getConstructorProduct().getName());
        workDetailDto.setPrice(working.getEstimate().getPrice());
        workDetailDto.setNote(working.getEstimate().getNote());
        workDetailDto.setRemark(working.getEstimate().getRemark());
        // 현재 작업자 및 현재 작업
        if(working.getNowWorkInfo().getWorkDetail().getConstructorProductWorkList() != null){
            workDetailDto.setCurrentWorkLevel(working.getNowWorkInfo().getWorkDetail().getConstructorProductWorkList().getName());
            workDetailDto.setCurrentWorkLevelId(working.getNowWorkInfo().getWorkDetail().getConstructorProductWorkList().getId());
            workDetailDto.setCurrentWorkNote(working.getNowWorkInfo().getWorkDetail().getNote());
        }
        if(working.getNowWorkInfo().getWorkDetail().getUserConstructor() != null){
            workDetailDto.setCurrentWorker(working.getNowWorkInfo().getWorkDetail().getUserConstructor().getName());
            workDetailDto.setCurrentWorkerId(working.getNowWorkInfo().getWorkDetail().getUserConstructor().getId());
        }

        return workDetailDto;
    }

    public List<UserConstructorDto> getConstructorMember(String constructorId) {
        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository
                .findByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 작업 가능한 사람이 없습니다."));

        List<UserConstructorDto> userConstructorDtoList = new ArrayList<>();
        affiliatedInfos.forEach(affiliatedInfo -> {
            if(affiliatedInfo.getUserConstructor().isActive()){
                UserConstructorDto userConstructorDto = new UserConstructorDto();
                userConstructorDto.setId(affiliatedInfo.getUserConstructor().getId());
                userConstructorDto.setName(affiliatedInfo.getUserConstructor().getName());
                userConstructorDtoList.add(userConstructorDto);
            }
        });

        return userConstructorDtoList;
    }

    public List<WorkLevelDto> getWorkLevelList(String constructorId, String workId){
        Working working = workingRepository
                .findByIdAndConstructorId(workId, constructorId)
                .orElseThrow(()-> new NullPointerException("작업이 존재하지 않습니다."));

        List<WorkLevelDto> workLevelDtos = new ArrayList<>();

        working.getWorkDetails()
                .forEach(workDetail -> {
                    WorkLevelDto workLevelDto = new WorkLevelDto();
                    workLevelDto.setId(workDetail.getId());
                    workLevelDto.setName(workDetail.getConstructorProductWorkList().getName());
                    workLevelDto.setNote(workDetail.getNote());
                    workLevelDto.setActualDate(workDetail.getActualWorkDate());
                    workLevelDto.setComplete(workDetail.isComplete());
                    workLevelDto.setSequence(workDetail.getConstructorProductWorkList().getSequence());
                    if(workDetail.getUserConstructor() != null){
                        workLevelDto.setCurrentAssignedTaskManager(workDetail.getUserConstructor().getName());
                        workLevelDto.setCurrentAssignedTaskManagerId(workDetail.getUserConstructor().getId());
                    }
                    if(workDetail.getWorking().getNowWorkInfo().getWorkDetail() != null){
                        workLevelDto.setCurrentAssignedTask(workDetail.getWorking().getNowWorkInfo().getWorkDetail().getConstructorProductWorkList().getName());
                        workLevelDto.setCurrentAssignedTaskId(workDetail.getWorking().getNowWorkInfo().getWorkDetail().getConstructorProductWorkList().getId());
                    }
                    workLevelDto.setFileIn(workDetail.getConstructorProductWorkList().isFileIn());

                    workLevelDtos.add(workLevelDto);
                });

        return workLevelDtos;

    }

   /* public List<?> allocateMember(String constructorId, String workId, String memberId){

    }*/
}

