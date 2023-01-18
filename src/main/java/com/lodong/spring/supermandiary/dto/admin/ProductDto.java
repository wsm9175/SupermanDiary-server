package com.lodong.spring.supermandiary.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String productId;
    private List<ProductWorkDto> productWorkList;
}
