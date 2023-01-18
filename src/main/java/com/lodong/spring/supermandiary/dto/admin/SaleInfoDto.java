package com.lodong.spring.supermandiary.dto.admin;

import com.lodong.spring.supermandiary.dto.calendar.WorkerFilterDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class SaleInfoDto {
    private List<WorkerFilterDto> workerList;
    private List<SalesDto> salesList;
}
