package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class ProductDto {
    private String name;
    private List<ProductWorkDto> productWorkList;
}
