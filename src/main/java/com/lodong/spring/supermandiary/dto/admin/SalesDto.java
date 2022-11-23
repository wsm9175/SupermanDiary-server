package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SalesDto {
    private String workId;
    private String name;
    private int price;
    private LocalDateTime completeConstructDate;
    private boolean isPay;
    private LocalDateTime completePayDate;
}
