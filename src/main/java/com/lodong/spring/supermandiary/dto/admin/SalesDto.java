package com.lodong.spring.supermandiary.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class SalesDto {
    private String workId;
    private String name;
    private int price;
    private LocalDateTime completeConstructDate;
    private boolean isPay;
    private LocalDateTime completePayDate;
    private String constructWorkerId;
    private String constructWorkerName;
    private String homeName;
    private String dong;
    private String hosu;
}
