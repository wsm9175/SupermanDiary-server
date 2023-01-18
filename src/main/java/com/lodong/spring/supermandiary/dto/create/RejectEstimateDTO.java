package com.lodong.spring.supermandiary.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class RejectEstimateDTO {
    private String estimateId;
    private boolean isVat;
    private String constructorNote;
    private int price;
    private List<EstimateDetailDto> estimateDetails;
    private List<DiscountDto> discounts;
}
