package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.create.RequestOrderStatusDto;
import com.lodong.spring.supermandiary.dto.create.*;
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
            createService.sendEstimateMember(constructorId, sendEstimate);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "회원 견적서 발송 성공";
            return getResponseMessage(statusEnum, message, null);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message;
            if (dataIntegrityViolationException.getMessage().contains("ConstraintViolationException")) {
                message = "해당 전자계약서 요청건은 이미 견적서가 등록돼있습니다.";
            } else {
                message = "DB삽입중 오류가 발생했습니다." + dataIntegrityViolationException.getMessage();
            }
            return getResponseMessage(statusEnum, message);
        } catch (Exception e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }
    @PostMapping("/send/estimate/non-member")
    public ResponseEntity<?> sendEstimateNoneMember(@RequestHeader(name = "Authorization") String token, @RequestBody SendEstimateDto sendEstimate) {
        String constructorId = getConstructorId(token);
        log.info("들어온 정보 : " + sendEstimate.toString());
        try {
            createService.sendEstimateNoneMember(constructorId, sendEstimate);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "비회원 견적서 발송 성공";
            return getResponseMessage(statusEnum, message, null);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
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

    @PutMapping("/re-send/estimate")
    public ResponseEntity<?> reSendEstimate(@RequestHeader(name = "Authorization") String token, @RequestBody ReSendEstimateDTO reSendEstimate){
        String constructorId = getConstructorId(token);
        log.info("들어온 정보 : " + reSendEstimate.toString());
        try {
            createService.reSendEstimate(constructorId, reSendEstimate);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "견적서 재전송 성공";
            return getResponseMessage(statusEnum, message, null);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
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

    @PostMapping("/send/estimate/member-meet")
    public ResponseEntity<?> sendEstimateMemberMeet(@RequestHeader(name = "Authorization") String token, @RequestBody SendEstimateDto sendEstimateDto){
        try{
            createService.sendEstimateMemberMeet(getConstructorId(token), sendEstimateDto);
            return getResponseMessage(StatusEnum.OK, "계약서 전송 성공", null);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PostMapping("/update/req-order/state")
    public ResponseEntity<?> updateRequestOrderStatus(@RequestBody RequestOrderStatusDto requestOrderStatus){
        try {
            log.info(requestOrderStatus.toString());
            createService.updateRequestStatus(requestOrderStatus);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "전자계약서 상태 수정 성공 : " + requestOrderStatus.getStatus();
            return getResponseMessage(statusEnum, message, null);
        } catch (DataIntegrityViolationException dataIntegrityViolationException) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "전자계약서 상태 수정중 오류가 발생했습니다." + dataIntegrityViolationException.getMessage();
            return getResponseMessage(statusEnum, message);
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = nullPointerException.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @GetMapping("/get/customer-info")
    public ResponseEntity<?> getCustomerData(String customerPhoneNumber){
        try{
            CustomerDTO customerDTO = createService.getCustomerInfo(customerPhoneNumber);
            return getResponseMessage(StatusEnum.OK, "회원정보 가져오기 성공", customerDTO);
        }catch (NullPointerException nullPointerException){
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }
}
