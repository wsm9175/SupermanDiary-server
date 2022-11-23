package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class WorkerInfoDto {
    private String workerId;
    private String name;
    private String phoneNumber;
    private List<WorkerTechDto> workerTechDtoList;
    private boolean isIsActive;
}
