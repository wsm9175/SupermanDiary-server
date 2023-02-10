package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.*;
import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.create.RequestOrder;
import com.lodong.spring.supermandiary.domain.create.RequestOrderStatusDto;
import com.lodong.spring.supermandiary.domain.usercustomer.CustomerAddress;
import com.lodong.spring.supermandiary.domain.usercustomer.CustomerPhoneNumber;
import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomerAlarm;
import com.lodong.spring.supermandiary.domain.working.NowWorkInfo;
import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import com.lodong.spring.supermandiary.domain.working.Working;
import com.lodong.spring.supermandiary.dto.create.*;
import com.lodong.spring.supermandiary.enumvalue.CustomerAlarmEnum;
import com.lodong.spring.supermandiary.enumvalue.EstimateEnum;
import com.lodong.spring.supermandiary.enumvalue.RequestOrderEnum;
import com.lodong.spring.supermandiary.repo.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateService {
    private final RequestOrderRepository requestOrderRepository;
    private final ConstructorProductRepository constructorProductRepository;
    private final EstimateRepository estimateRepository;
    private final WorkingRepository workingRepository;
    private final UserCustomerRepository userCustomerRepository;
    private final ConstructorRepository constructorRepository;
    private final ApartmentRepository apartmentRepository;
    private final OtherHomeRepository otherHomeRepository;
    private final ConstructorAlarmRepository constructorAlarmRepository;
    private final UserCustomerAlarmRepository userCustomerAlarmRepository;
    private final EstimateDetailRepository estimateDetailRepository;
    private final DiscountRepository discountRepository;

    @Transactional(readOnly = true)
    public List<RequestOrderDto> getRequestOrderList(String token) throws NullPointerException {
        Constructor constructor = Constructor.builder().id(token).build();

        List<RequestOrder> requestOrderList = requestOrderRepository.findByConstructor(constructor).orElseThrow(() -> new NullPointerException("전자계약서 요청건이 없습니다."));

        List<RequestOrderDto> requestOrderDtoList = new ArrayList<>();

        for (RequestOrder requestOrder : requestOrderList) {
            RequestOrderDto requestOrderDto = new RequestOrderDto();
            if (requestOrder.getStatus().equals(RequestOrderEnum.DELETE.label()) || requestOrder.getStatus().equals(RequestOrderEnum.PROCESSED.label())) {
                continue;
            }

            ChoiceProductDto choiceProducts = new ChoiceProductDto(requestOrder.getConstructorProduct().getId(), requestOrder.getConstructorProduct().getProduct().getName());

            requestOrderDto.setId(requestOrder.getId());
            requestOrderDto.setPhoneNumber(requestOrder.getCustomer().getPhoneNumber());

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
            requestOrderDto.setRejectMessage(requestOrder.getRejectMessage());

            if (requestOrder.getEstimate() != null) {
                Estimate estimate = requestOrder.getEstimate();
                List<EstimateDetailDto> estimateDetailDtos = new ArrayList<>();
                for (EstimateDetail estimateDetail : estimate.getEstimateDetails()) {
                    EstimateDetailDto estimateDetailDto = new EstimateDetailDto(estimateDetail.getProductName(), estimateDetail.getCount(), estimateDetail.getPrice());
                    estimateDetailDtos.add(estimateDetailDto);
                }
                List<DiscountDto> discountList = new ArrayList<>();
                for (Discount discount : estimate.getDiscountList()) {
                    DiscountDto discountDto = new DiscountDto(discount.getDiscountContent(), discount.getDiscount());
                    discountList.add(discountDto);
                }
                RejectEstimateDTO rejectEstimateDTO = new RejectEstimateDTO(estimate.getId(), estimate.isVat(), estimate.getRemark(),
                        estimate.getPrice(), estimateDetailDtos, discountList);
                requestOrderDto.setRejectEstimateInfo(rejectEstimateDTO);
            }
            requestOrderDtoList.add(requestOrderDto);
        }
        return requestOrderDtoList;
    }

    @Transactional(readOnly = true)
    public List<ConstructorProductDto> getProductList(String constructorId) throws NullPointerException {
        Constructor constructor = Constructor.builder().id(constructorId).build();

        List<ConstructorProduct> constructorProducts = constructorProductRepository.findConstructorProductByConstructor(constructor).orElseThrow(() -> new NullPointerException("소속된 시공사의 등록된 상품정보가 존재하지 않습니다."));

        List<ConstructorProductDto> constructorProductDtos = new ArrayList<>();

        for (ConstructorProduct constructorProduct : constructorProducts) {
            ConstructorProductDto constructorProductDto = new ConstructorProductDto();
            constructorProductDto.setId(constructorProduct.getId());
            constructorProductDto.setName(constructorProduct.getProduct().getName());
            constructorProductDtos.add(constructorProductDto);
        }
        return constructorProductDtos;
    }

    @Transactional
    public void sendEstimateMember(String constructorId, SendEstimateDto sendEstimateDto) throws NullPointerException {
        Constructor constructor = Constructor.builder().id(constructorId).build();
        ConstructorProduct constructorProduct = ConstructorProduct.builder().id(sendEstimateDto.getProductId()).build();
        if (sendEstimateDto.getRequestOrderId() != null) { //회원 견적서
            RequestOrder requestOrder = requestOrderRepository
                    .findById(sendEstimateDto.getRequestOrderId())
                    .orElseThrow(() -> new NullPointerException("해당 견적서는 존재하지 않습니다."));
            List<EstimateDetail> estimateDetails = new ArrayList<>();

            Estimate estimate = Estimate.builder().id(UUID.randomUUID().toString())
                    .constructorProduct(constructorProduct)
                    .constructor(constructor)
                    .requestOrder(requestOrder)
                    .note(sendEstimateDto.getNote())
                    .remark(sendEstimateDto.getRemark())
                    .price(sendEstimateDto.getPrice())
                    .isVat(sendEstimateDto.isVat())
                    .status(EstimateEnum.SEND.label())
                    .build();

            for (EstimateDetailDto estimateDetailDto : sendEstimateDto.getEstimateDetails()) {
                EstimateDetail estimateDetail = EstimateDetail.builder().id(UUID.randomUUID().toString()).estimate(estimate).productName(estimateDetailDto.getProductName()).count(estimateDetailDto.getCount()).price(estimateDetailDto.getPrice()).build();
                estimateDetails.add(estimateDetail);
            }
            List<Discount> discountList = new ArrayList<>();
            for (DiscountDto discountDto : sendEstimateDto.getDiscounts()) {
                Discount discount = Discount.builder().id(UUID.randomUUID().toString()).estimate(estimate).discountContent(discountDto.getDiscountCri()).discount(discountDto.getDiscount()).build();
                discountList.add(discount);
            }
            estimate.setEstimateDetails(estimateDetails);
            estimate.setDiscountList(discountList);
            estimate.setStatus(EstimateEnum.SEND.label());
            estimateRepository.save(estimate);
            //전자 계약서 요청건 상태 처리중으로 변경
            requestOrder.setStatus(RequestOrderEnum.PROCESSING.label());
            requestOrderRepository.save(requestOrder);

            sendAlarm(requestOrder.getCustomer(), CustomerAlarmEnum.RECEIVE_ESTIMATE, estimate.getId());
        } else {
            throw new NullPointerException("잘못된 값입니다.");
        }
    }

    @Transactional
    public void sendEstimateNoneMember(String constructorId, SendEstimateDto sendEstimateDto) {
        Constructor constructor = Constructor.builder().id(constructorId).build();
        ConstructorProduct constructorProduct = constructorProductRepository.findById(sendEstimateDto.getProductId()).orElseThrow(() -> new NullPointerException("없는 상품입니다."));

        Estimate estimate = null;

        //비회원 주문이므로 바로 작업 생성
        Working working = null;

        if (sendEstimateDto.getRequestOrderId() == null) { //비회원 견적서
            if (sendEstimateDto.getApartmentCode() != null) {
                Apartment apartment = Apartment.builder().id(sendEstimateDto.getApartmentCode()).build();

                estimate = Estimate.builder().id(UUID.randomUUID().toString()).constructorProduct(constructorProduct).constructor(constructor).apartment(apartment).apartment_dong(sendEstimateDto.getApartmentDong()).apartment_hosu(sendEstimateDto.getApartmentHosu()).apartment_type(sendEstimateDto.getApartmentType()).name(sendEstimateDto.getName()).phoneNumber(sendEstimateDto.getPhoneNumber()).note(sendEstimateDto.getNote()).remark(sendEstimateDto.getRemark()).price(sendEstimateDto.getPrice()).isVat(sendEstimateDto.isVat()).build();
                working = Working.builder().id(UUID.randomUUID().toString()).constructor(constructor).constructorProduct(constructorProduct).estimate(estimate).completeConstruct(false).completePay(false).apartment(apartment).apartmentDong(estimate.getApartment_dong()).apartmentHosu(estimate.getApartment_hosu()).apartmentType(estimate.getApartment_type()).nonMemberName(estimate.getName()).nonMemberPhoneNumber(estimate.getPhoneNumber()).build();

            } else if (sendEstimateDto.getOtherHomeId() != null) {
                OtherHome otherHome = OtherHome.builder().id(sendEstimateDto.getOtherHomeId()).build();
                estimate = Estimate.builder().id(UUID.randomUUID().toString()).constructorProduct(constructorProduct).constructor(constructor).otherHome(otherHome).otherHomeDong(sendEstimateDto.getOtherHomeDong()).otherHomeHosu(sendEstimateDto.getOtherHomeHosu()).otherHomeType(sendEstimateDto.getOtherHomeType()).name(sendEstimateDto.getName()).phoneNumber(sendEstimateDto.getPhoneNumber()).note(sendEstimateDto.getNote()).remark(sendEstimateDto.getRemark()).price(sendEstimateDto.getPrice()).build();
                working = Working.builder().id(UUID.randomUUID().toString()).constructor(constructor).constructorProduct(constructorProduct).estimate(estimate).completeConstruct(false).completePay(false).otherHome(otherHome).otherHomeDong(sendEstimateDto.getOtherHomeDong()).otherHomeHosu(sendEstimateDto.getOtherHomeHosu()).otherHomeType(sendEstimateDto.getOtherHomeType()).nonMemberName(estimate.getName()).nonMemberPhoneNumber(estimate.getPhoneNumber()).build();
            }

            List<EstimateDetail> estimateDetails = new ArrayList<>();

            for (EstimateDetailDto estimateDetailDto : sendEstimateDto.getEstimateDetails()) {
                EstimateDetail estimateDetail = EstimateDetail.builder().id(UUID.randomUUID().toString()).estimate(estimate).productName(estimateDetailDto.getProductName()).count(estimateDetailDto.getCount()).price(estimateDetailDto.getPrice()).build();
                estimateDetails.add(estimateDetail);
            }

            List<Discount> discountList = new ArrayList<>();
            for (DiscountDto discountDto : sendEstimateDto.getDiscounts()) {
                Discount discount = Discount.builder().id(UUID.randomUUID().toString()).estimate(estimate).discountContent(discountDto.getDiscountCri()).discount(discountDto.getDiscount()).build();
                discountList.add(discount);
            }

            estimate.setEstimateDetails(estimateDetails);
            estimate.setDiscountList(discountList);
            estimate.setStatus(EstimateEnum.PROCESSED.label());

            estimateRepository.save(estimate);

            List<ConstructorProductWorkList> constructorProductWorkLists = estimate.getConstructorProduct().getConstructorProductWorkLists();

            System.out.println("size + " + constructorProductWorkLists.size());
            List<WorkDetail> workDetails = new ArrayList<>();

            Working finalWorking = working;
            constructorProductWorkLists.forEach(constructorProductWorkList -> {
                WorkDetail workDetail = WorkDetail.builder().id(UUID.randomUUID().toString()).working(finalWorking).name(constructorProductWorkList.getName()).sequence(constructorProductWorkList.getSequence()).isFileIn(constructorProductWorkList.isFileIn()).isComplete(false).build();
                workDetails.add(workDetail);
            });

            WorkDetail firstWorkDetail = null;
            for (WorkDetail workDetail : workDetails) {
                if (workDetail.getSequence() == 1) {
                    firstWorkDetail = workDetail;
                    break;
                }
            }

            NowWorkInfo nowWorkInfo = NowWorkInfo.builder().id(UUID.randomUUID().toString()).working(working).workDetail(firstWorkDetail).build();

            working.setWorkDetails(workDetails);
            working.setNowWorkInfo(nowWorkInfo);
            workingRepository.save(working);
        } else {
            throw new NullPointerException("잘못된 값입니다.");
        }
    }

    @Transactional
    public void reSendEstimate(String constructorId, ReSendEstimateDTO reSendEstimateDto) throws NullPointerException {
        Constructor constructor = Constructor.builder().id(constructorId).build();
        ConstructorProduct constructorProduct = constructorProductRepository.findById(reSendEstimateDto.getProductId()).orElseThrow(() -> new NullPointerException("등록되지 않은 시공사 상품입니다."));
        if (reSendEstimateDto.getRequestOrderId() != null) { //회원 견적서
            RequestOrder requestOrder = requestOrderRepository
                    .findById(reSendEstimateDto.getRequestOrderId())
                    .orElseThrow(() -> new NullPointerException("해당 전자 계약서는 존재하지 않습니다."));
            List<EstimateDetail> estimateDetails = new ArrayList<>();

            Estimate estimate = estimateRepository.findById(reSendEstimateDto.getEstimateId()).orElseThrow(() -> new NullPointerException("해당 견적서는 존재하지 않습니다."));
            estimate.setConstructorProduct(constructorProduct);
            estimate.setRequestOrder(requestOrder);
            estimate.setNote(reSendEstimateDto.getNote());
            estimate.setRemark(reSendEstimateDto.getRemark());
            estimate.setPrice(reSendEstimateDto.getPrice());
            estimate.setVat(reSendEstimateDto.isVat());
            estimate.setStatus(EstimateEnum.SEND.label());
            estimateDetailRepository.deleteAllByEstimate(estimate);
            discountRepository.deleteAllByEstimate(estimate);
            for (EstimateDetailDto estimateDetailDto : reSendEstimateDto.getEstimateDetails()) {
                EstimateDetail estimateDetail = EstimateDetail.builder().id(UUID.randomUUID().toString()).estimate(estimate).productName(estimateDetailDto.getProductName()).count(estimateDetailDto.getCount()).price(estimateDetailDto.getPrice()).build();
                estimateDetails.add(estimateDetail);
            }
            List<Discount> discountList = new ArrayList<>();
            for (DiscountDto discountDto : reSendEstimateDto.getDiscounts()) {
                Discount discount = Discount.builder().id(UUID.randomUUID().toString()).estimate(estimate).discountContent(discountDto.getDiscountCri()).discount(discountDto.getDiscount()).build();
                discountList.add(discount);
            }
            estimate.setEstimateDetails(estimateDetails);
            estimate.setDiscountList(discountList);
            estimate.setStatus(EstimateEnum.SEND.label());

            //전자 계약서 요청건 상태 처리중으로 변경
            requestOrder.setStatus(RequestOrderEnum.PROCESSING.label());
            requestOrderRepository.save(requestOrder);

            sendAlarm(requestOrder.getCustomer(), CustomerAlarmEnum.RECEIVE_ESTIMATE, estimate.getId());
        } else {
            throw new NullPointerException("잘못된 값입니다.");
        }
    }

    @Transactional
    public void updateRequestStatus(RequestOrderStatusDto requestOrderStatus) {
        String status = Optional.ofNullable(getStatus(requestOrderStatus.getStatus())).orElseThrow(() -> new NullPointerException("해당 상태값은 존재하지 않음"));
        requestOrderRepository.updateStatus(status, requestOrderStatus.getRequestOrderId());
    }

    @Transactional(readOnly = true)
    public CustomerDTO getCustomerInfo(String phoneNumber) throws NullPointerException {
        UserCustomer userCustomer = userCustomerRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NullPointerException("해당 휴대폰 번호는 저장되어 있지 않습니다."));

        Set<CustomerPhoneNumber> customerPhoneNumberList = userCustomer.getPhoneNumbers();
        Set<CustomerAddress> customerAddressList = userCustomer.getCustomerAddressList();
        List<String> subPhoneNumberList = new ArrayList<>();
        List<CustomerAddressDTO> customerAddressDTOList = new ArrayList<>();
        for (CustomerPhoneNumber customerPhoneNumber : customerPhoneNumberList) {
            subPhoneNumberList.add(customerPhoneNumber.getPhoneNumber());
        }
        for (CustomerAddress customerAddress : customerAddressList) {
            if (customerAddress.getApartment() != null) {
                CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO(customerAddress.getApartment().getId(), customerAddress.getApartment().getName(), customerAddress.getApartmentDong(), customerAddress.getApartmentHosu());
                customerAddressDTOList.add(customerAddressDTO);
            } else if (customerAddress.getOtherHome() != null) {
                CustomerAddressDTO customerAddressDTO = new CustomerAddressDTO();
                customerAddressDTO.setOtherHomeId(customerAddress.getOtherHome().getId());
                customerAddressDTO.setOtherHomeName(customerAddress.getOtherHome().getName());
                customerAddressDTO.setOtherHomeDong(customerAddress.getOtherHomeDong());
                customerAddressDTO.setOtherHomeHosu(customerAddress.getOtherHomeHosu());
            }
        }
        CustomerDTO customerDTO = new CustomerDTO(userCustomer.getId(), userCustomer.getName(), userCustomer.getPhoneNumber(), subPhoneNumberList, customerAddressDTOList);
        return customerDTO;
    }

    @Transactional
    public void sendEstimateMemberMeet(String constructorId, SendEstimateDto sendEstimateDto) throws NullPointerException {
        //전자계약서 요청건 생성 후 견적서 생성
        log.info(sendEstimateDto.toString());
        Constructor constructor = constructorRepository
                .findById(constructorId)
                .orElseThrow(() -> new NullPointerException("시공사가 존재하지 않습니다."));
        UserCustomer userCustomer = userCustomerRepository
                .findById(sendEstimateDto.getCustomerId())
                .orElseThrow(() -> new NullPointerException("해당 고객은 존재하지 않습니다."));
        ConstructorProduct constructorProduct = constructorProductRepository
                .findById(sendEstimateDto.getProductId())
                .orElseThrow(() -> new NullPointerException("해당 물건은 존재하지 않습니다."));
        RequestOrder requestOrder = null;
        if (sendEstimateDto.getApartmentCode() != null) {
            Apartment apartment = apartmentRepository
                    .findById(sendEstimateDto.getApartmentCode())
                    .orElseThrow(() -> new NullPointerException("해당 아파트는 존재하지 않습니다."));
            requestOrder = RequestOrder.builder()
                    .id(UUID.randomUUID().toString())
                    .constructor(constructor)
                    .customer(userCustomer)
                    .phoneNumber(sendEstimateDto.getPhoneNumber())
                    .apartment(apartment)
                    .apartment_type(sendEstimateDto.getApartmentType())
                    .dong(sendEstimateDto.getApartmentDong())
                    .hosu(sendEstimateDto.getApartmentHosu())
                    .status(RequestOrderEnum.PROCESSING.label())
                    .note(sendEstimateDto.getNote())
                    .liveInDate(sendEstimateDto.getLiveInDate())
                    .isConfirmationLiveIn(sendEstimateDto.isConfirmationLiveIn())
                    .requestConstructDate(sendEstimateDto.getRequestConstructDate())
                    .isConfirmationConstruct(sendEstimateDto.isConfirmationConstruct())
                    .isCashReceipt(sendEstimateDto.isCashReceipt())
                    .constructorProduct(constructorProduct)
                    .createAt(LocalDateTime.now())
                    .build();
        } else if (sendEstimateDto.getOtherHomeId() != null) {
            OtherHome otherHome = otherHomeRepository
                    .findById(sendEstimateDto.getOtherHomeId())
                    .orElseThrow(() -> new NullPointerException("해당 건물은 존재하지 않습니다."));
            requestOrder = RequestOrder.builder()
                    .id(UUID.randomUUID().toString())
                    .constructor(constructor)
                    .customer(userCustomer)
                    .note(sendEstimateDto.getNote())
                    .phoneNumber(sendEstimateDto.getPhoneNumber())
                    .otherHome(otherHome)
                    .otherHomeType(sendEstimateDto.getOtherHomeType())
                    .otherHomeDong(sendEstimateDto.getOtherHomeDong())
                    .otherHomeHosu(sendEstimateDto.getOtherHomeHosu())
                    .status(RequestOrderEnum.PROCESSING.label())
                    .liveInDate(sendEstimateDto.getLiveInDate())
                    .isConfirmationLiveIn(sendEstimateDto.isConfirmationLiveIn())
                    .requestConstructDate(sendEstimateDto.getRequestConstructDate())
                    .isConfirmationConstruct(sendEstimateDto.isConfirmationConstruct())
                    .isCashReceipt(sendEstimateDto.isCashReceipt())
                    .constructorProduct(constructorProduct)
                    .createAt(LocalDateTime.now())
                    .build();
        }

        requestOrderRepository.save(requestOrder);
        //requestOrderProductRepository.save(requestOrderProduct);

        List<EstimateDetail> estimateDetails = new ArrayList<>();

        Estimate estimate = Estimate.builder().id(UUID.randomUUID().toString())
                .constructorProduct(constructorProduct)
                .constructor(constructor).requestOrder(requestOrder).note(sendEstimateDto.getNote()).remark(sendEstimateDto.getRemark()).price(sendEstimateDto.getPrice())
                .isVat(sendEstimateDto.isVat())
                .status(EstimateEnum.SEND.label())
                .build();

        for (EstimateDetailDto estimateDetailDto : sendEstimateDto.getEstimateDetails()) {
            EstimateDetail estimateDetail = EstimateDetail.builder().id(UUID.randomUUID().toString()).estimate(estimate).productName(estimateDetailDto.getProductName()).count(estimateDetailDto.getCount()).price(estimateDetailDto.getPrice()).build();
            estimateDetails.add(estimateDetail);
        }
        List<Discount> discountList = new ArrayList<>();
        for (DiscountDto discountDto : sendEstimateDto.getDiscounts()) {
            Discount discount = Discount.builder().id(UUID.randomUUID().toString()).estimate(estimate).discountContent(discountDto.getDiscountCri()).discount(discountDto.getDiscount()).build();
            discountList.add(discount);
        }
        estimate.setEstimateDetails(estimateDetails);
        estimate.setDiscountList(discountList);
        estimate.setStatus(EstimateEnum.SEND.label());
        estimateRepository.save(estimate);
        sendAlarm(userCustomer, CustomerAlarmEnum.RECEIVE_ESTIMATE, estimate.getId());
    }

    private void sendAlarm(UserCustomer userCustomer, CustomerAlarmEnum customerAlarmEnum, String content) {
        UserCustomerAlarm userCustomerAlarm = UserCustomerAlarm.builder()
                .id(UUID.randomUUID().toString())
                .userCustomer(userCustomer)
                .kind(customerAlarmEnum.label())
                .detail(customerAlarmEnum.label())
                .content(content)
                .createAt(LocalDateTime.now())
                .build();

        userCustomerAlarmRepository.save(userCustomerAlarm);
    }

    private String getStatus(String status) {
        if (status.equals(RequestOrderEnum.BASIC.label())) {
            return RequestOrderEnum.BASIC.label();
        } else if (status.equals(RequestOrderEnum.DEFER.label())) {
            return RequestOrderEnum.DEFER.label();
        } else if (status.equals(RequestOrderEnum.DELETE.label())) {
            return RequestOrderEnum.DELETE.label();
        } else if (status.equals(RequestOrderEnum.PROCESSED.label())) {
            return RequestOrderEnum.PROCESSED.label();
        } else {
            return null;
        }
    }
}
