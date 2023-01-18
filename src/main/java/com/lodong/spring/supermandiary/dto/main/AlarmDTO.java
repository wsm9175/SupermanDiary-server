package com.lodong.spring.supermandiary.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDTO {
    private String alarmId;
    private String alarmKind;
    private String contentId;
    private String content;
    private LocalDateTime createAt;
}
