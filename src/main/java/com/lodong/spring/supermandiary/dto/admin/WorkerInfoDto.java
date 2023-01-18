package com.lodong.spring.supermandiary.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class WorkerInfoDto {
    private String workerId;
    private String name;
    private String phoneNumber;
    private List<WorkerTechDto> workerTechDtoList;
    private boolean isIsActive;
}
