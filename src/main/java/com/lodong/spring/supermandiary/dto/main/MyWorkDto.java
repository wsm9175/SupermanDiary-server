package com.lodong.spring.supermandiary.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data @AllArgsConstructor
public class MyWorkDto {
    private String workId;
    private String workDetailId;
    private String workLevel;
    private String workLevelId;
    private String productName;
    private String homeName;
    private String homeDong;
    private String homeHosu;
    private LocalDate estimateWorkDate;
    private LocalTime estimateWorkTime;
    private boolean isIsFileIn;
    private boolean isIsComplete;
    private boolean isIsMine;
    private String workerName;
    private String workerId;
    private String status;
}
