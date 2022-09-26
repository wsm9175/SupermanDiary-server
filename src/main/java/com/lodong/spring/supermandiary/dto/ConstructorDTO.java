package com.lodong.spring.supermandiary.dto;

import lombok.Data;

@Data
public class ConstructorDTO {
    private String id;
    private String name;
    private String introduction;
    private boolean payActivation;
    private boolean orderManage;
    private boolean payManage;
    private boolean webAdminActive;
}
