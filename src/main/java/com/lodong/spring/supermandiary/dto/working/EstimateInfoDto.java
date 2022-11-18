package com.lodong.spring.supermandiary.dto.working;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data @AllArgsConstructor
public class EstimateInfoDto {
    private boolean isIsMember;
    private String customerName;
    private List<String> phoneNumber;
    private String homeName;
    private String dong;
    private String hosu;
    private String type;
    private LocalDate livedIn;
    private boolean isConfirmationLiveIn;
    private LocalDate requestConstructorDate;
    private boolean isConfirmationConstruct;
    private boolean isCashReceipt;
    private String customerNote;
    private String productName;
    private List<EstimateDetailDto> estimateDetailList;
    private List<DiscountDto> discountDtoList;
    private int totalPrice;
    private boolean isVat;
    private String constructorNote;

}
