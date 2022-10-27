package com.lodong.spring.supermandiary.dto.create;

import lombok.Data;

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
    private boolean isCompletion;

    private List<ChoiceProductDto> choiceProducts;
}
