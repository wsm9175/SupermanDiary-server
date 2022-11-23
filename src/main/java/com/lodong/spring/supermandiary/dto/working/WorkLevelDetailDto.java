package com.lodong.spring.supermandiary.dto.working;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WorkLevelDetailDto {
    private boolean isIsConstructorComplete;
    private LocalDateTime completeConstructorDate;
    private boolean isIsPayComplete;
    private LocalDateTime completePayDate;
    private List<WorkLevelDto> workDetailList;
}
