package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

@Data
public class OrderManageDto {
    private boolean isActivate;
    private String orderMethod;
    private String placeOrder;
}
