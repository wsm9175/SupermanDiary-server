package com.lodong.spring.supermandiary.dto.admin;

import lombok.Data;

import java.util.List;

@Data
public class WorkerManageDto {
    private List<InvitationWorkerDto> invitationWorkerDtoList;
    private List<WorkerInfoDto> workerInfoDtoList;
}
