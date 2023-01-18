package com.lodong.spring.supermandiary.dto.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDate;
import java.util.List;

@Data
public class SendEstimateDto {
    //회원용
    private String requestOrderId;
    //회원 - 대면
    private String customerId;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate liveInDate;
    private boolean isConfirmationLiveIn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate requestConstructDate;
    private boolean isConfirmationConstruct;
    private boolean isCashReceipt;

    //공통
    private String productId;
    private String note;
    private boolean isVat;
    private String remark;
    private int price;
    private List<EstimateDetailDto> estimateDetails;
    private List<DiscountDto> discounts;
}
