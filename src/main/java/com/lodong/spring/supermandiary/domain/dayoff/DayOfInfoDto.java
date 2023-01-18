package com.lodong.spring.supermandiary.domain.dayoff;

import com.lodong.spring.supermandiary.dto.calendar.WorkerFilterDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class DayOfInfoDto {
    private List<WorkerFilterDto> workerFilterDtos;
    private List<DayOfWorkerDto> dayOfWorkerList;
}
