package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

@Data
public class WorkListDto {
    private String id;
    private int sequence;
    private String workName;
    private boolean isFileIn;
    private String status;
}
