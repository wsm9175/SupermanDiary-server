package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class ConstructorProductAlterDTO {
    private String constructorProductId;
    private List<ProductWorkDto> productWorkList;
}
