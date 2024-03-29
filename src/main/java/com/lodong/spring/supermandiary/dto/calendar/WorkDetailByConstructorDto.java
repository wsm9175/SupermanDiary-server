package com.lodong.spring.supermandiary.dto.calendar;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class WorkDetailByConstructorDto {
    private String workId;
    private String workDetailId;
    private String workerId;
    private String workerName;
    private LocalDate estimateWorkDate;
    private LocalTime estimateWorkTime;
    private String workLevelName;
    private String productName;
    private String homeName;
    private String dong;
    private String hosu;
    private boolean isComplete;

}
