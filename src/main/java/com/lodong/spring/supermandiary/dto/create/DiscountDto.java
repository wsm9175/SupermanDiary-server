package com.lodong.spring.supermandiary.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DiscountDto {
    private String discountCri;
    private int discount;
}
