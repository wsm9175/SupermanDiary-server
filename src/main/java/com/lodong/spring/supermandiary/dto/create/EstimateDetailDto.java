package com.lodong.spring.supermandiary.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EstimateDetailDto {
    private String productName;
    private int count;
    private int price;
}
