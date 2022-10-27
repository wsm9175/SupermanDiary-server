package com.lodong.spring.supermandiary.dto.create;

import lombok.Data;

@Data
public class EstimateDetailDto {
    private String productName;
    private int count;
    private int price;
    private String note;
}
