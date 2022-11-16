package com.lodong.spring.supermandiary.dto.calendar;

import lombok.Data;

import java.util.List;

@Data
public class FilterDataDto {
    private List<WorkFilterDto> workFilterList;
    private List<WorkerFilterDto> workerFilterList;
    private List<WorkDetailFilterDto> workDetailFilterList;
}
