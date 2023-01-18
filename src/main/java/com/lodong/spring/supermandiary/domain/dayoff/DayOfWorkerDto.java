package com.lodong.spring.supermandiary.domain.dayoff;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class DayOfWorkerDto {
    private String workerId;
    private String workerName;
    private LocalDate DayOffDate;
}
