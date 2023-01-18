package com.lodong.spring.supermandiary.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ExhibitionBoardDTO {
    private String boardId;
    private String constructorName;
    private String videoLink;
    private String tag;
    private String constructorInfo;
    private List<ExhibitionCommentDTO> commentDTOList;
}
