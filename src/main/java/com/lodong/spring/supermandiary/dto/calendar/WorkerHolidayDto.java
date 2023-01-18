package com.lodong.spring.supermandiary.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@AllArgsConstructor
@Data
public class WorkerHolidayDto {
    private LocalDate holiday;
}
