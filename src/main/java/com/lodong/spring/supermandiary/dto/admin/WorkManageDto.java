package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class WorkManageDto {
    private List<ConstructorProductWorkDto> constructorProductWorkDtoList;
    private CallingNumberDto callingNumber;
    private OrderManageDto orderManage;
    private PayManageDto payManage;
}
