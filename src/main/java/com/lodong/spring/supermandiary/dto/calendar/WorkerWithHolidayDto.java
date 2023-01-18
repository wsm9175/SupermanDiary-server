package com.lodong.spring.supermandiary.dto.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class WorkerWithHolidayDto {
    private String id;
    private String name;
    private List<WorkerHolidayDto> workerHolidayList;
}
