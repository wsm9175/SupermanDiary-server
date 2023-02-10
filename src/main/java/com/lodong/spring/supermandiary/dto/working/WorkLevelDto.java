package com.lodong.spring.supermandiary.dto.working;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class WorkLevelDto {
    private String id;
    private String name;
    private String note;
    private int sequence;
    private LocalDate actualDate;
    private boolean isComplete;
    private String currentAssignedTaskManager;
    private String currentAssignedTaskManagerId;
    private String currentAssignedTask;
    private String currentAssignedTaskId;
    private boolean isFileIn;
    private String status;
    private List<String> fileNameList = new ArrayList<>();
}
