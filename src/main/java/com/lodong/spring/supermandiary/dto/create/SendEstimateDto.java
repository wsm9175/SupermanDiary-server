package com.lodong.spring.supermandiary.dto.create;

import com.lodong.spring.supermandiary.domain.EstimateDetail;
import lombok.Data;

import java.util.List;

@Data
public class SendEstimateDto {
    //회원용
    private String requestOrderId;

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
    private int discount;
    private String discountCri;
    private List<EstimateDetailDto> estimateDetails;
}
