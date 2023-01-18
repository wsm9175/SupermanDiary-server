package com.lodong.spring.supermandiary.dto.create;

import lombok.Data;

import java.util.List;

@Data
public class ReSendEstimateDTO {
    //회원용
    private String requestOrderId;
    private String estimateId;

    //비회원용
    private String apartmentCode;
    private String apartmentDong;
    private String apartmentHosu;
    private String apartmentType;

    private String otherHomeId;
    private String otherHomeDong;
    private String otherHomeHosu;
    private String otherHomeType;
    private String name;
    private String phoneNumber;

    //공통
    private String productId;
    private String note;
    private boolean isVat;
    private String remark;
    private int price;
    private List<EstimateDetailDto> estimateDetails;
    private List<DiscountDto> discounts;
}
