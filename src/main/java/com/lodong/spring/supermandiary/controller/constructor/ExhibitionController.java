package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.dto.exhibition.ExhibitionBoardDTO;
import com.lodong.spring.supermandiary.dto.exhibition.ExhibitionBoardListDTO;
import com.lodong.spring.supermandiary.dto.exhibition.ExhibitionConstructorCommentDto;
import com.lodong.spring.supermandiary.dto.exhibition.ExhibitionConstructorReplyDto;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.ExhibitionService;
import com.lodong.spring.supermandiary.service.MyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/exhibition/construct")
public class ExhibitionController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MyInfoService myInfoService;
    private final ExhibitionService exhibitionService;

    public ExhibitionController(JwtTokenProvider jwtTokenProvider, MyInfoService myInfoService, ExhibitionService exhibitionService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myInfoService = myInfoService;
        this.exhibitionService = exhibitionService;
    }

    @GetMapping("/board/list")
    public ResponseEntity<?> getBoardList(@RequestHeader("Authorization") String token) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        try {
            ExhibitionBoardListDTO exhibitionBoardListDTO = exhibitionService.getBoardList(constructorId);
            return getResponseMessage(StatusEnum.OK, "소속된 시공사의 박람회 게시판 목록", exhibitionBoardListDTO);
        } catch (NullPointerException e) {
            return getResponseMessage(StatusEnum.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/board")
    public ResponseEntity<?> getBoard(String boardId) {
        try {
            ExhibitionBoardDTO exhibitionBoardDTO = exhibitionService.getBoardInfo(boardId);
            return getResponseMessage(StatusEnum.OK, "선택한 게시판 정보", exhibitionBoardDTO);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PostMapping("/board/comment")
    public ResponseEntity<?> addComment(@RequestHeader("Authorization") String token, @RequestBody ExhibitionConstructorCommentDto exhibitionConstructorCommentDto) {
        exhibitionService.addComment(getConstructorId(token), exhibitionConstructorCommentDto.getBoardId(), exhibitionConstructorCommentDto.getComment());
        return getResponseMessage(StatusEnum.OK, "댓글 삽입 성공", null);
    }

    @PostMapping("/board/reply")
    public ResponseEntity<?> addReply(@RequestHeader("Authorization") String token, @RequestBody ExhibitionConstructorReplyDto exhibitionConstructorReplyDto) {
        try {
            exhibitionService.addReply(getConstructorId(token), exhibitionConstructorReplyDto.getBoardId(), exhibitionConstructorReplyDto.getComment(), exhibitionConstructorReplyDto.getCommentGroupId(), exhibitionConstructorReplyDto.getSequence());
            return getResponseMessage(StatusEnum.OK, "댓글 삽입 성공", null);
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            return getResponseMessage(StatusEnum.BAD_REQUEST, dataIntegrityViolationException.getMessage());
        }
    }

    private String getMyUuid(String token) throws NullPointerException {
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }
}
