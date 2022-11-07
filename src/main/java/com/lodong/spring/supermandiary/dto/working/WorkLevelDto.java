package com.lodong.spring.supermandiary.dto.working;

import lombok.Data;

import java.time.LocalDate;

@Data
public class WorkLevelDto {
    private String id;
    private String name;
    private String note;
    private int sequence;
    private LocalDate actualDate;
    private boolean isComplete;
    private String manager;
    private String managerId;
    private boolean isFileIn;
}
