package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.exhibition.ExhibitionBoard;
import com.lodong.spring.supermandiary.domain.exhibition.ExhibitionComment;
import com.lodong.spring.supermandiary.dto.exhibition.ExhibitionBoardDTO;
import com.lodong.spring.supermandiary.dto.exhibition.ExhibitionBoardListDTO;
import com.lodong.spring.supermandiary.dto.exhibition.ExhibitionBoardSummaryDTO;
import com.lodong.spring.supermandiary.dto.exhibition.ExhibitionCommentDTO;
import com.lodong.spring.supermandiary.repo.ExhibitionBoardRepository;
import com.lodong.spring.supermandiary.repo.ExhibitionCommentRepository;
import com.lodong.spring.supermandiary.util.DateUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExhibitionService {
    private final ExhibitionBoardRepository exhibitionBoardRepository;
    private final ExhibitionCommentRepository exhibitionCommentRepository;

    public ExhibitionBoardListDTO getBoardList(String constructorId) {
        List<ExhibitionBoard> exhibitionBoardList = exhibitionBoardRepository.findByConstructorId(constructorId).orElseThrow(() -> new NullPointerException("해당 시공사는 박람회 게시판이 없습니다."));
        List<ExhibitionBoardSummaryDTO> exhibitionBoardSummaryDTOList = new ArrayList<>();
        exhibitionBoardList.forEach(exhibitionBoard -> {
            ExhibitionBoardSummaryDTO exhibitionBoardSummaryDTO = new ExhibitionBoardSummaryDTO();
            exhibitionBoardSummaryDTO.setExhibitionName(exhibitionBoard.getExhibition().getName());
            exhibitionBoardSummaryDTO.setBoardId(exhibitionBoard.getId());
            exhibitionBoardSummaryDTO.setTag(exhibitionBoard.getTag());
            exhibitionBoardSummaryDTO.setStartDateTime(exhibitionBoard.getExhibition().getStartDateTime());
            exhibitionBoardSummaryDTO.setEndDateTime(exhibitionBoard.getExhibition().getEndDateTime());
            exhibitionBoardSummaryDTOList.add(exhibitionBoardSummaryDTO);
        });
        return new ExhibitionBoardListDTO(exhibitionBoardSummaryDTOList);
    }

    public ExhibitionBoardDTO getBoardInfo(String boardId) {
        ExhibitionBoard exhibitionBoard = exhibitionBoardRepository.findById(boardId).orElseThrow(() -> new NullPointerException("해당 게시판은 삭제되었습니다."));

        List<ExhibitionCommentDTO> exhibitionCommentDTOList = new ArrayList<>();
        for (ExhibitionComment exhibitionComment : exhibitionBoard.getExhibitionCommentList()) {
            ExhibitionCommentDTO exhibitionCommentDTO = null;
            if (exhibitionComment.getConstructor() != null) {
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionBoard.getId(), exhibitionComment.getCommentGroupId(), exhibitionComment.getSequence(), exhibitionComment.getComment(), exhibitionComment.getConstructor().getName(), null, exhibitionComment.getCreateAt());
            } else if (exhibitionComment.getUserCustomer() != null) {
                exhibitionCommentDTO = new ExhibitionCommentDTO(exhibitionBoard.getId(), exhibitionComment.getCommentGroupId(), exhibitionComment.getSequence(), exhibitionComment.getComment(), null, exhibitionComment.getUserCustomer().getName(), exhibitionComment.getCreateAt());
            }
            exhibitionCommentDTOList.add(exhibitionCommentDTO);
        }
        return new ExhibitionBoardDTO(exhibitionBoard.getId(), exhibitionBoard.getConstructor().getId(), exhibitionBoard.getVideoLink(), exhibitionBoard.getTag(), exhibitionBoard.getConstructorContent(), exhibitionCommentDTOList);
    }

    @Transactional
    public void addComment(String constructorId, String boardId, String comment) {
        ExhibitionBoard exhibitionBoard = ExhibitionBoard.builder().id(boardId).build();
        Constructor constructor = Constructor.builder().id(constructorId).build();
        ExhibitionComment exhibitionComment = ExhibitionComment.builder().id(UUID.randomUUID().toString()).exhibitionBoard(exhibitionBoard).commentGroupId(UUID.randomUUID().toString()).sequence(0).comment(comment).constructor(constructor).createAt(DateUtil.getNowDateTime()).build();
        exhibitionCommentRepository.save(exhibitionComment);
    }

    @Transactional
    public void addReply(String constructorId, String boardId, String comment, String commentGroupId, int sequence) {
        ExhibitionBoard exhibitionBoard = ExhibitionBoard.builder().id(boardId).build();
        Constructor constructor = Constructor.builder().id(constructorId).build();
        ExhibitionComment exhibitionComment = ExhibitionComment.builder().id(UUID.randomUUID().toString()).exhibitionBoard(exhibitionBoard).commentGroupId(commentGroupId).sequence(sequence).comment(comment).constructor(constructor).createAt(DateUtil.getNowDateTime()).build();
        exhibitionCommentRepository.save(exhibitionComment);
    }

}
