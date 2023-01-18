package com.lodong.spring.supermandiary.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@AllArgsConstructor
@Data
public class ExhibitionBoardListDTO {
    private List<ExhibitionBoardSummaryDTO> exhibitionBoardSummaryDTOList;
}
