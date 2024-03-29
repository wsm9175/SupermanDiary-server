package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.Estimate;
import com.lodong.spring.supermandiary.domain.create.RequestOrder;
import com.lodong.spring.supermandiary.domain.file.WorkFile;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.working.Working;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.dto.working.*;
import com.lodong.spring.supermandiary.enumvalue.WorkingStatusEnum;
import com.lodong.spring.supermandiary.repo.AffiliatedInfoRepository;
import com.lodong.spring.supermandiary.repo.WorkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkingService {
    private final WorkingRepository workingRepository;
    private final AffiliatedInfoRepository affiliatedInfoRepository;

    @Transactional(readOnly = true)
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
                    workApartmentDto.setStatus(working.getNowWorkInfo().getWorkDetail().getUserConstructor() != null ? WorkingStatusEnum.ASSIGNED.label() : WorkingStatusEnum.NOTASSIGNED.label());
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

    @Transactional(readOnly = true)
    public List<WorkApartmentDto> findWork(String constructorId, String phoneNumber, String dong, String hosu) {
        //회원
        List<Working> memberWorkingList = null;
        //비회원
        List<Working> noneMemberWokringList = null;
        List<Working> joined = new ArrayList<>();
        if (phoneNumber != null) {
            //회원
            memberWorkingList = workingRepository
                    .findByConstructorIdAndUserCustomer_PhoneNumbers_phoneNumber(constructorId, phoneNumber)
                    .orElse(new ArrayList<>());
            //비회원
            noneMemberWokringList = workingRepository
                    .findByConstructorIdAndNonMemberPhoneNumber(constructorId, phoneNumber)
                    .orElse(new ArrayList<>());
        } else if (dong != null && hosu != null) {
            //아파트
            memberWorkingList = workingRepository
                    .findByConstructorIdAndApartmentDongAndApartmentHosu(constructorId, dong, hosu)
                    .orElse(new ArrayList<>());
            //그외 건물
            noneMemberWokringList = workingRepository
                    .findByConstructorIdAndOtherHomeDongAndOtherHomeHosu(constructorId, dong, hosu)
                    .orElse(new ArrayList<>());
        } else if (dong == null) {
            //아파트
            memberWorkingList = workingRepository
                    .findByConstructorIdAndApartmentHosu(constructorId, hosu)
                    .orElse(new ArrayList<>());
            //그외 건물
            noneMemberWokringList = workingRepository
                    .findByConstructorIdAndOtherHomeHosu(constructorId, hosu)
                    .orElse(new ArrayList<>());
        } else {
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
    @Transactional(readOnly = true)
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
            workDetailDto.setCustomerPhoneNumber(working.getEstimate().getRequestOrder().getPhoneNumber());
            workDetailDto.setMember(true);
        } else if (working.getNonMemberName() != null) {
            workDetailDto.setCustomerName(working.getNonMemberName());
            workDetailDto.setCustomerPhoneNumber(working.getNonMemberPhoneNumber());
            workDetailDto.setMember(false);
        }

        // 상품 이름, 가격, 비고
        workDetailDto.setProductName(working.getConstructorProduct().getProduct().getName());
        workDetailDto.setPrice(working.getEstimate().getPrice());
        workDetailDto.setNote(working.getEstimate().getNote());
        workDetailDto.setRemark(working.getEstimate().getRemark());
        workDetailDto.setCurrentWorkDetail(working.getNowWorkInfo().getWorkDetail().getId());
        // 현재 작업자 및 현재 작업
        if (working.getNowWorkInfo().getWorkDetail().getName() != null) {
            workDetailDto.setCurrentWorkLevel(working.getNowWorkInfo().getWorkDetail().getName());
            workDetailDto.setCurrentWorkLevelId(working.getNowWorkInfo().getWorkDetail().getId());
            workDetailDto.setCurrentWorkNote(working.getNowWorkInfo().getWorkDetail().getNote());
        }
        if (working.getNowWorkInfo().getWorkDetail().getUserConstructor() != null) {
            workDetailDto.setCurrentWorker(working.getNowWorkInfo().getWorkDetail().getUserConstructor().getName());
            workDetailDto.setCurrentWorkerId(working.getNowWorkInfo().getWorkDetail().getUserConstructor().getId());
        }

        return workDetailDto;
    }

    @Transactional(readOnly = true)
    public List<UserConstructorDto> getConstructorMember(String constructorId) {
        List<AffiliatedInfo> affiliatedInfos = affiliatedInfoRepository
                .findByConstructorId(constructorId)
                .orElseThrow(() -> new NullPointerException("해당 시공사에 작업 가능한 사람이 없습니다."));

        List<UserConstructorDto> userConstructorDtoList = new ArrayList<>();
        affiliatedInfos.forEach(affiliatedInfo -> {
            if (affiliatedInfo.getUserConstructor().isActive()) {
                UserConstructorDto userConstructorDto = new UserConstructorDto();
                userConstructorDto.setId(affiliatedInfo.getUserConstructor().getId());
                userConstructorDto.setName(affiliatedInfo.getUserConstructor().getName());
                userConstructorDtoList.add(userConstructorDto);
            }
        });
        return userConstructorDtoList;
    }

    @Transactional(readOnly = true)
    public WorkLevelDetailDto getWorkLevelList(String constructorId, String workId) {
        Working working = workingRepository
                .findByIdAndConstructorId(workId, constructorId)
                .orElseThrow(() -> new NullPointerException("작업이 존재하지 않습니다."));

        WorkLevelDetailDto workLevelDetailDto = new WorkLevelDetailDto();
        workLevelDetailDto.setIsPayComplete(working.isCompletePay());
        workLevelDetailDto.setCompleteConstructorDate(working.getCompleteConstructDate());
        workLevelDetailDto.setIsConstructorComplete(working.isCompleteConstruct());
        workLevelDetailDto.setCompletePayDate(working.getCompletePayDate());

        List<WorkLevelDto> workLevelDtos = new ArrayList<>();

        working.getWorkDetails()
                .forEach(workDetail -> {
                    WorkLevelDto workLevelDto = new WorkLevelDto();
                    workLevelDto.setId(workDetail.getId());
                    workLevelDto.setName(workDetail.getName());
                    workLevelDto.setNote(workDetail.getNote());
                    workLevelDto.setActualDate(workDetail.getActualWorkDate());
                    workLevelDto.setComplete(workDetail.isComplete());
                    workLevelDto.setSequence(workDetail.getSequence());
                    if (workDetail.getUserConstructor() != null) {
                        workLevelDto.setCurrentAssignedTaskManager(workDetail.getUserConstructor().getName());
                        workLevelDto.setCurrentAssignedTaskManagerId(workDetail.getUserConstructor().getId());
                    }
                    if (workDetail.getWorking().getNowWorkInfo().getWorkDetail() != null) {
                        workLevelDto.setCurrentAssignedTask(workDetail.getWorking().getNowWorkInfo().getWorkDetail().getName());
                        workLevelDto.setCurrentAssignedTaskId(workDetail.getWorking().getNowWorkInfo().getWorkDetail().getId());
                    }
                    workLevelDto.setFileIn(workDetail.isFileIn());
                    if (workDetail.isFileIn()) {
                        List<String> fileNameList = new ArrayList<>();
                        for (WorkFile workFile : workDetail.getWorkFileList()) {
                            fileNameList.add(workFile.getFileList().getName());
                        }
                        workLevelDto.setFileNameList(fileNameList);
                    }
                    workLevelDto.setStatus(workDetail.getStatus());
                    workLevelDtos.add(workLevelDto);
                });

        workLevelDetailDto.setWorkDetailList(workLevelDtos);
        return workLevelDetailDto;
    }

    @Transactional(readOnly = true)
    public EstimateInfoDto getEstimate(String constructorId, String workId) {
        Working working = workingRepository
                .findByIdAndConstructorId(workId, constructorId)
                .orElseThrow(() -> new NullPointerException("해당 작업은 더이상 존재하지 않습니다."));

        Estimate estimate = working.getEstimate();
        RequestOrder requestOrder = estimate.getRequestOrder();

        //회원 비회원 구분
        boolean isIsMember;
        String customerName = null;
        List<String> phoneNumber = new ArrayList<>();
        String homeName = null;
        String dong = null;
        String hosu = null;
        String type = null;
        LocalDate livedIn = null;
        boolean isConfirmationLiveIn = false;
        LocalDate requestConstructorDate = null;
        boolean isConfirmationConstruct = false;
        boolean isCashReceipt = false;
        String customerNote = null;
        String productName = null;
        List<EstimateDetailDto> estimateDetailList = new ArrayList<>();
        List<DiscountDto> discountDtoList = new ArrayList<>();
        int totalPrice;
        boolean isVat;
        String constructorNote = null;

        if (requestOrder != null) { // 회원
            isIsMember = true;
            customerName = requestOrder.getCustomer().getName();
            requestOrder.getCustomer().getPhoneNumbers().forEach(customerPhoneNumber -> {
                phoneNumber.add(customerPhoneNumber.getPhoneNumber());
            });
            //아파트 기타 건물 구분
            if (requestOrder.getApartment() != null) {
                homeName = requestOrder.getApartment().getName();
                dong = requestOrder.getDong();
                hosu = requestOrder.getHosu();
                type = requestOrder.getApartment_type();
            } else {
                homeName = requestOrder.getOtherHome().getName();
                dong = requestOrder.getOtherHomeDong();
                hosu = requestOrder.getOtherHomeHosu();
                type = requestOrder.getOtherHomeType();
            }
            livedIn = requestOrder.getLiveInDate();
            isConfirmationLiveIn = requestOrder.isConfirmationLiveIn();
            isCashReceipt = requestOrder.isCashReceipt();
            customerNote = requestOrder.getNote();
            productName = requestOrder.getConstructorProduct().getProduct().getName();
            ////////////////////////////////////////////////////////////
        } else { // 비회원
            isIsMember = false;
            customerName = estimate.getName();
            phoneNumber.add(estimate.getPhoneNumber());
            //아파트 기타 건물 구분
            if (estimate.getApartment() != null) {
                homeName = estimate.getApartment().getName();
                dong = estimate.getApartment_dong();
                hosu = estimate.getApartment_hosu();
                type = estimate.getApartment_type();
            } else if (estimate.getOtherHome() != null) {
                homeName = estimate.getOtherHome().getName();
                dong = estimate.getOtherHomeDong();
                hosu = estimate.getOtherHomeHosu();
                type = estimate.getOtherHomeType();
            }
            isCashReceipt = estimate.isCashReceipt();
            customerNote = estimate.getNote();
            productName = estimate.getConstructorProduct().getProduct().getName();
        }

        estimate.getEstimateDetails().stream().forEach(estimateDetail -> {
            EstimateDetailDto estimateDetailDto = new EstimateDetailDto();
            estimateDetailDto.setName(estimateDetail.getProductName());
            estimateDetailDto.setCount(estimateDetail.getCount());
            estimateDetailDto.setPrice(estimateDetail.getPrice());
            estimateDetailList.add(estimateDetailDto);
        });
        estimate.getDiscountList().stream().forEach(discount -> {
            DiscountDto dto = new DiscountDto();
            dto.setContent(discount.getDiscountContent());
            dto.setDiscountPrice(discount.getDiscount());
            discountDtoList.add(dto);
        });
        isVat = estimate.isVat();
        totalPrice = estimate.getPrice();
        constructorNote = estimate.getRemark();

        EstimateInfoDto estimateInfoDto = new EstimateInfoDto(isIsMember, customerName, phoneNumber, homeName, dong, hosu, type, livedIn,
                isConfirmationLiveIn, requestConstructorDate, isConfirmationConstruct, isCashReceipt, customerNote, productName,
                estimateDetailList, discountDtoList, totalPrice, isVat, constructorNote);

        return estimateInfoDto;

    }

}

