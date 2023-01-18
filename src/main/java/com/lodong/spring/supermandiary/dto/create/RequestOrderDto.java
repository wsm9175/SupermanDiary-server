package com.lodong.spring.supermandiary.dto.create;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class RequestOrderDto {
    private String id;
    private String orderer;
    private String phoneNumber;
    private String note;

    private String apartmentName;
    private String dong;
    private String hosu;
    private String apartmentType;

    private String otherHomeName;
    private String otherHomeDong;
    private String otherHomeHosu;
    private String status;

    private LocalDate liveInDate;
    private boolean isConfirmationLiveIn;
    private LocalDate requestConstructorDate;
    private boolean isConfirmationConstructorDate;

    private ChoiceProductDto choiceProducts;

    private String rejectMessage;

    //상태가 반려인 경우 해당 값이 존재
    private RejectEstimateDTO rejectEstimateInfo;
}
