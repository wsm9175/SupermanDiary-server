package com.lodong.spring.supermandiary.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NextWorkDetailDTO {
    private String nextWorkDetailId;
    private String nextAssignedWorkerName;
    private String nextWorkNote;
    private LocalDate nextWorkEstimateWorkDate;
    private LocalTime nextWorkEstimateWorkTime;
    private String nextWorkName;
    private int nextWorkSequence;
}
