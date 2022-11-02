package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class ConstructorProductWorkDto {
    private String id;
    private String productName;
    private List<WorkListDto> workList;

}
