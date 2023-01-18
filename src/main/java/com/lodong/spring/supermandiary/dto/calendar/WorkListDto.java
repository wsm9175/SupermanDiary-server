package com.lodong.spring.supermandiary.dto.calendar;

import lombok.Data;

import java.util.List;

@Data
public class WorkListDto {
    private List<WorkerWithHolidayDto> workerList;
    private List<WorkDetailByConstructorDto> workDetailByConstructorDtoList;
}
