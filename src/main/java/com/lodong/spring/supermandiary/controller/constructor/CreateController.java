package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.AffiliatedInfo;
import com.lodong.spring.supermandiary.dto.create.ConstructorProductDto;
import com.lodong.spring.supermandiary.dto.create.RequestOrderDto;
import com.lodong.spring.supermandiary.dto.create.SendEstimateDto;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.CreateService;
import com.lodong.spring.supermandiary.service.MyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.*;

@Slf4j
@RestController
@RequestMapping("rest/v1/create/construct")
public class CreateController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MyInfoService myInfoService;

    private final CreateService createService;

    public CreateController(JwtTokenProvider jwtTokenProvider, MyInfoService myInfoService, CreateService createService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myInfoService = myInfoService;
        this.createService = createService;
    }

    @GetMapping("/req-order")
    public ResponseEntity<?> getRequestOrder(@RequestHeader(name = "Authorization") String token) {
        String constructorId = getConstructorId(token);

        try {
            List<RequestOrderDto> requestOrderList = createService.getRequestOrderList(constructorId);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "소속된 시공사 전자계약서 요청건 목록";
            return getResponseMessage(statusEnum, message, requestOrderList);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }

    @GetMapping("/product/list")
    public ResponseEntity<?> getProductList(@RequestHeader(name = "Authorization") String token) {
        String constructorId = getConstructorId(token);

        try {
            List<ConstructorProductDto> constructorProductDtos = createService
                    .getProductList(constructorId);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "소속된 시공사의 상품 목록";
            return getResponseMessage(statusEnum, message, constructorProductDtos);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @PostMapping("/send/estimate/member")
    public ResponseEntity<?> sendEstimateMember(@RequestHeader(name = "Authorization") String token, @RequestBody SendEstimateDto sendEstimate) {
        String constructorId = getConstructorId(token);

        try {
            createService.sendEstimateMember(constructorId,sendEstimate);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "회원 견적서 발송 성공";
            return getResponseMessage(statusEnum, message, null);
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "데이터 베이스 삽입중 오류가 발생했습니다." + dataIntegrityViolationException.getMessage();
            return getResponseMessage(statusEnum, message);
        } catch (Exception e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @PostMapping("/send/estimate/non-member")
    public ResponseEntity<?> sendEstimateNoneMember(@RequestHeader(name = "Authorization") String token, @RequestBody SendEstimateDto sendEstimate){
        String constructorId = getConstructorId(token);
        log.info("들어온 정보 : " + sendEstimate.toString());
        try {
            createService.sendEstimateNoneMember(constructorId,sendEstimate);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "비회원 견적서 발송 성공";
            return getResponseMessage(statusEnum, message, null);
        }catch (DataIntegrityViolationException dataIntegrityViolationException){
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "데이터 베이스 삽입중 오류가 발생했습니다." + dataIntegrityViolationException.getMessage();
            return getResponseMessage(statusEnum, message);
        } catch (Exception e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }


}
