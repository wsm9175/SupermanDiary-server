package com.lodong.spring.supermandiary.dto.working;

import lombok.Data;

@Data
public class WorkDetailDto {
    private String homeName;
    private String homeDong;
    private String homeHosu;

    private String customerName;
    private String customerPhoneNumber;

    private String productName;
    private int price;
    private String note;
    private String remark;

    private boolean isMember;
    private String currentWorkLevel;
    private String currentWorkLevelId;
    private String currentWorkNote;
    private String currentWorker;
    private String currentWorkerId;
}
