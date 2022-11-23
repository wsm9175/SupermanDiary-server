package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

@Data
public class PayManageDto {
    private boolean isActivate;
    private String bank;
    private String bankAccountNumber;
    private String payTemplate;
}
