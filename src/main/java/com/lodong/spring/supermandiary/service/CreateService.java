package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.*;
import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.create.RequestOrder;
import com.lodong.spring.supermandiary.domain.create.RequestOrderProduct;
import com.lodong.spring.supermandiary.domain.create.RequestOrderStatusDto;
import com.lodong.spring.supermandiary.domain.working.NowWorkInfo;
import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import com.lodong.spring.supermandiary.domain.working.Working;
import com.lodong.spring.supermandiary.dto.create.*;
import com.lodong.spring.supermandiary.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CreateService {
    private final RequestOrderRepository requestOrderRepository;
    private final RequestOrderProductRepository requestOrderProductRepository;
    private final ConstructorProductRepository constructorProductRepository;
    private final EstimateRepository estimateRepository;
    private final EstimateDetailRepository estimateDetailRepository;
    private final WorkingRepository workingRepository;

    private final String STATUS_DELETE = "삭제";
    private final String STATUS_COMPLETE = "처리완료";
    private final String STATUS_WORKING_COMPLETE = "완료";
    private final String STATUS_DEFERE = "보류";
    private final String STATUS_WORKING = "작업중";
    private final String STATUS_UN_ASSIGNED = "미배정";

    public List<RequestOrderDto> getRequestOrderList(String token) throws NullPointerException {
        Constructor constructor = Constructor.builder()
                .id(token)
                .build();

        List<RequestOrder> requestOrderList = requestOrderRepository.findByConstructor(constructor).
                orElseThrow(() -> new NullPointerException("전자계약서 요청건이 없습니다."));

        List<RequestOrderDto> requestOrderDtoList = new ArrayList<>();

        for (RequestOrder requestOrder : requestOrderList) {
            RequestOrderDto requestOrderDto = new RequestOrderDto();
            if(requestOrder.getStatus().equals(STATUS_DELETE) || requestOrder.getStatus().equals(STATUS_COMPLETE)){
                continue;
            }
            List<ChoiceProductDto> choiceProducts = new ArrayList<>();

            List<RequestOrderProduct> requestOrderProductList =
                    requestOrderProductRepository.findRequestOrderProductByRequestOrder(requestOrder).orElseThrow(() -> new NullPointerException("주문 상품이 없음."));

            for (RequestOrderProduct requestOrderProduct : requestOrderProductList) {
                ChoiceProductDto choiceProduct = new ChoiceProductDto();
                choiceProduct.setId(requestOrderProduct.getConstructorProduct().getId());
                choiceProduct.setName(requestOrderProduct.getConstructorProduct().getName());
                choiceProducts.add(choiceProduct);
            }
            requestOrderDto.setId(requestOrder.getId());
            requestOrderDto.setOrderer(requestOrder.getCustomer().getName());
            requestOrderDto.setNote(requestOrder.getNote());
            requestOrderDto.setChoiceProducts(choiceProducts);
            requestOrderDto.setStatus(requestOrder.getStatus());
            if (requestOrder.getApartment() != null) {
                requestOrderDto.setApartmentName(requestOrder.getApartment().getName());
                requestOrderDto.setDong(requestOrder.getDong());
                requestOrderDto.setHosu(requestOrder.getHosu());
                requestOrderDto.setApartmentType(requestOrder.getApartment_type());
            } else {
                requestOrderDto.setOtherHomeName(requestOrder.getOtherHome().getName());
                requestOrderDto.setOtherHomeDong(requestOrder.getOtherHomeDong());
                requestOrderDto.setOtherHomeHosu(requestOrder.getOtherHomeHosu());
            }
            requestOrderDto.setLiveInDate(requestOrder.getLiveInDate());
            requestOrderDto.setRequestConstructorDate(requestOrder.getRequestConstructDate());
            requestOrderDto.setConfirmationLiveIn(requestOrder.isConfirmationLiveIn());
            requestOrderDto.setConfirmationConstructorDate(requestOrder.isConfirmationConstruct());
            requestOrderDtoList.add(requestOrderDto);
        }
        return requestOrderDtoList;
    }

    public List<ConstructorProductDto> getProductList(String constructorId) throws NullPointerException {
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();

        List<ConstructorProduct> constructorProducts = constructorProductRepository
                .findConstructorProductByConstructor(constructor)
                .orElseThrow(() -> new NullPointerException("소속된 시공사의 등록된 상품정보가 존재하지 않습니다."));

        List<ConstructorProductDto> constructorProductDtos = new ArrayList<>();

        for (ConstructorProduct constructorProduct : constructorProducts) {
            ConstructorProductDto constructorProductDto = new ConstructorProductDto();
            constructorProductDto.setId(constructorProduct.getId());
            constructorProductDto.setName(constructorProduct.getName());
            constructorProductDtos.add(constructorProductDto);
        }

        return constructorProductDtos;
    }

    @Transactional
    public void sendEstimateMember(String constructorId, SendEstimateDto sendEstimateDto) {
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();
        ConstructorProduct constructorProduct = ConstructorProduct.builder()
                .id(sendEstimateDto.getProductId())
                .build();

        if (sendEstimateDto.getRequestOrderId() != null) { //회원 견적서
            RequestOrder requestOrder = RequestOrder.builder()
                    .id(sendEstimateDto.getRequestOrderId())
                    .build();
            List<EstimateDetail> estimateDetails = new ArrayList<>();

            Estimate estimate = Estimate.builder()
                    .id(UUID.randomUUID().toString())
                    .constructorProduct(constructorProduct)
                    .constructor(constructor)
                    .requestOrder(requestOrder)
                    .note(sendEstimateDto.getNote())
                    .remark(sendEstimateDto.getRemark())
                    .price(sendEstimateDto.getPrice())
                    .isVat(sendEstimateDto.isVat())
                    .build();

            for (EstimateDetailDto estimateDetailDto : sendEstimateDto.getEstimateDetails()) {
                EstimateDetail estimateDetail = EstimateDetail.builder()
                        .id(UUID.randomUUID().toString())
                        .estimate(estimate)
                        .productName(estimateDetailDto.getProductName())
                        .count(estimateDetailDto.getCount())
                        .price(estimateDetailDto.getPrice())
                        .build();
                estimateDetails.add(estimateDetail);
            }
            List<Discount> discountList = new ArrayList<>();
            for (DiscountDto discountDto : sendEstimateDto.getDiscounts()) {
                Discount discount = Discount.builder()
                        .id(UUID.randomUUID().toString())
                        .estimate(estimate)
                        .discountContent(discountDto.getDiscountCri())
                        .discount(discountDto.getDiscount())
                        .build();
                discountList.add(discount);
            }

            estimate.setEstimateDetails(estimateDetails);
            estimate.setDiscountList(discountList);
            estimateRepository.save(estimate);
        } else {
            throw new NullPointerException("잘못된 값입니다.");
        }

    }

    public void sendEstimateNoneMember(String constructorId, SendEstimateDto sendEstimateDto) {
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();
        ConstructorProduct constructorProduct = constructorProductRepository
                .findById(sendEstimateDto.getProductId())
                .orElseThrow(() -> new NullPointerException("없는 상품입니다."));

        Estimate estimate = null;

        //비회원 주문이므로 바로 작업 생성
        Working working = null;

        if (sendEstimateDto.getRequestOrderId() == null) { //비회원 견적서
            if (sendEstimateDto.getApartmentCode() != null) {
                Apartment apartment = Apartment.builder()
                        .id(sendEstimateDto.getApartmentCode())
                        .build();

                estimate = Estimate.builder()
                        .id(UUID.randomUUID().toString())
                        .constructorProduct(constructorProduct)
                        .constructor(constructor)
                        .apartment(apartment)
                        .apartment_dong(sendEstimateDto.getApartmentDong())
                        .apartment_hosu(sendEstimateDto.getApartmentHosu())
                        .apartment_type(sendEstimateDto.getApartmentType())
                        .name(sendEstimateDto.getName())
                        .phoneNumber(sendEstimateDto.getPhoneNumber())
                        .note(sendEstimateDto.getNote())
                        .remark(sendEstimateDto.getRemark())
                        .price(sendEstimateDto.getPrice())
                        .isVat(sendEstimateDto.isVat())
                        .build();

                working = Working.builder()
                        .id(UUID.randomUUID().toString())
                        .constructor(constructor)
                        .constructorProduct(constructorProduct)
                        .estimate(estimate)
                        .isCompleteConstruct(false)
                        .isCompletePay(false)
                        .apartment(apartment)
                        .apartmentDong(estimate.getApartment_dong())
                        .apartmentHosu(estimate.getApartment_hosu())
                        .apartmentType(estimate.getApartment_type())
                        .nonMemberName(estimate.getName())
                        .nonMemberPhoneNumber(estimate.getPhoneNumber())
                        .build();

            } else if (sendEstimateDto.getOtherHomeId() != null) {
                OtherHome otherHome = OtherHome.builder()
                        .id(sendEstimateDto.getOtherHomeId())
                        .build();

                estimate = Estimate.builder()
                        .id(UUID.randomUUID().toString())
                        .constructorProduct(constructorProduct)
                        .constructor(constructor)
                        .otherHome(otherHome)
                        .otherHomeDong(sendEstimateDto.getOtherHomeDong())
                        .otherHomeHosu(sendEstimateDto.getOtherHomeHosu())
                        .otherHomeType(sendEstimateDto.getOtherHomeType())
                        .name(sendEstimateDto.getName())
                        .phoneNumber(sendEstimateDto.getPhoneNumber())
                        .note(sendEstimateDto.getNote())
                        .remark(sendEstimateDto.getRemark())
                        .price(sendEstimateDto.getPrice())
                        .build();

                working = Working.builder()
                        .id(UUID.randomUUID().toString())
                        .constructor(constructor)
                        .constructorProduct(constructorProduct)
                        .estimate(estimate)
                        .isCompleteConstruct(false)
                        .isCompletePay(false)
                        .otherHome(otherHome)
                        .otherHomeDong(sendEstimateDto.getOtherHomeDong())
                        .otherHomeHosu(sendEstimateDto.getOtherHomeHosu())
                        .otherHomeType(sendEstimateDto.getOtherHomeType())
                        .nonMemberName(estimate.getName())
                        .nonMemberPhoneNumber(estimate.getPhoneNumber())
                        .build();
            }


            List<EstimateDetail> estimateDetails = new ArrayList<>();


            for (EstimateDetailDto estimateDetailDto : sendEstimateDto.getEstimateDetails()) {
                EstimateDetail estimateDetail = EstimateDetail.builder()
                        .id(UUID.randomUUID().toString())
                        .estimate(estimate)
                        .productName(estimateDetailDto.getProductName())
                        .count(estimateDetailDto.getCount())
                        .price(estimateDetailDto.getPrice())
                        .build();
                estimateDetails.add(estimateDetail);
            }

            List<Discount> discountList = new ArrayList<>();
            for (DiscountDto discountDto : sendEstimateDto.getDiscounts()) {
                Discount discount = Discount.builder()
                        .id(UUID.randomUUID().toString())
                        .estimate(estimate)
                        .discountContent(discountDto.getDiscountCri())
                        .discount(discountDto.getDiscount())
                        .build();
                discountList.add(discount);
            }

            estimate.setEstimateDetails(estimateDetails);
            estimate.setDiscountList(discountList);

            estimateRepository.save(estimate);

            List<ConstructorProductWorkList> constructorProductWorkLists = estimate.getConstructorProduct()
                    .getConstructorProductWorkLists();

            System.out.println("size + " + constructorProductWorkLists.size());
            List<WorkDetail> workDetails = new ArrayList<>();

            Working finalWorking = working;
            constructorProductWorkLists.forEach(constructorProductWorkList -> {
                WorkDetail workDetail = WorkDetail.builder()
                        .id(UUID.randomUUID().toString())
                        .working(finalWorking)
                        .constructorProductWorkList(constructorProductWorkList)
                        .isComplete(false)
                        .build();
                workDetails.add(workDetail);
            });

            WorkDetail firstWorkDetail = null;
            for(WorkDetail workDetail:workDetails){
                if(workDetail.getConstructorProductWorkList().getSequence() == 1){
                    firstWorkDetail = workDetail;
                    break;
                }
            }

            NowWorkInfo nowWorkInfo = NowWorkInfo.builder()
                    .id(UUID.randomUUID().toString())
                    .working(working)
                    .workDetail(firstWorkDetail)
                    .build();

            working.setWorkDetails(workDetails);
            working.setNowWorkInfo(nowWorkInfo);
            workingRepository.save(working);
        } else {
            throw new NullPointerException("잘못된 값입니다.");
        }
    }
    public void updateRequestStatus(RequestOrderStatusDto requestOrderStatus){
        requestOrderRepository.updateStatus(requestOrderStatus.getStatus(), requestOrderStatus.getRequestOrderId());
    }
}
