package com.lodong.spring.supermandiary.dto.exhibition;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ExhibitionBoardSummaryDTO {
    private String tag;
    private String boardId;
    private String exhibitionName;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
}
