package com.lodong.spring.supermandiary.controller.constructor;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.dto.main.AlarmDTO;
import com.lodong.spring.supermandiary.dto.main.MyInfoDTO;
import com.lodong.spring.supermandiary.dto.main.MyWorkDto;
import com.lodong.spring.supermandiary.dto.main.ReadAllAlarmDTO;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.MyInfoService;
import com.lodong.spring.supermandiary.service.main.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/main/construct")
public class MainController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MyInfoService myInfoService;

    private final MainService mainService;

    public MainController(JwtTokenProvider jwtTokenProvider, MyInfoService myInfoService, MainService mainService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myInfoService = myInfoService;
        this.mainService = mainService;
    }
    @GetMapping("/my/work")
    public ResponseEntity<?> getMyWork(@RequestHeader("Authorization") String token){
        String myUuid = getMyUuid(token);
        String constructorId = getConstructorId(myUuid);

        List<MyWorkDto> myWorkDtos =  mainService.getMyWork(myUuid, constructorId);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "나의 작업 목록";
        return getResponseMessage(statusEnum, message, myWorkDtos);
    }

    @PostMapping("/complete/work/no-file")
    public ResponseEntity<?> completeWorkNoFile(String workDetail){
        try{
            mainService.completeWorkNoFile(workDetail);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = workDetail + " 작업 완료 성공";
            return getResponseMessage(statusEnum, message, null);
        }catch (Exception e){
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "작업 완료 실패 " + e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }
    @PostMapping("/complete/work")
    public ResponseEntity<?> completeWork(@RequestPart(value = "images", required = false) List<MultipartFile> files,
                                          @RequestPart(value = "workDetail") String workDetail){
        try{
            mainService.completeWork(files,workDetail.replace("\"", ""));
            StatusEnum statusEnum = StatusEnum.OK;
            String message = workDetail + " 작업 완료 성공";
            return getResponseMessage(statusEnum, message, null);
        }catch (IOException ioException){
            ioException.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "작업 완료 실패 : 파일저장 오류";
            return getResponseMessage(statusEnum, message);
        } catch (Exception e){
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "작업 완료 실패 " + e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @PutMapping("/complete/working/pay")
    public ResponseEntity<?> completePay(String workId, String method){
        mainService.completeWorkingPay(workId, method);
        return getResponseMessage(StatusEnum.OK, "결제완료", null);
    }

    @GetMapping("/alarm")
    public ResponseEntity<?> getAlarm(@RequestHeader(name = "Authorization") String token){
        try{
            String myUuid = getMyUuid(token);
            String constructorId = getConstructorId(myUuid);
            List<AlarmDTO> alarmDTOS = mainService.getAlarmList(constructorId);
            return getResponseMessage(StatusEnum.OK, "알림 목록", alarmDTOS);
        }catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PatchMapping("/alarm/read")
    public ResponseEntity<?> readAlarm(@RequestHeader(name = "Authorization") String token, String alarmId){
        try{
            mainService.readAlarm(alarmId);
            return getResponseMessage(StatusEnum.OK, "알림 읽음 처리", null);
        }catch (NullPointerException nullPointerException){
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyInfo(@RequestHeader(name = "Authorization") String token){
        try{
            MyInfoDTO myInfoDTO = mainService.getMyInfo(getMyUuid(token));
            return getResponseMessage(StatusEnum.OK, "내정보", myInfoDTO);
        }catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, nullPointerException.getMessage());
        }
    }

    @PatchMapping("/alarm/read-all")
    public ResponseEntity<?> readAllAlarm(@RequestHeader(name = "Authorization") String token, @RequestBody ReadAllAlarmDTO readAllAlarmDTO){
        mainService.readAllAlarm(readAllAlarmDTO);
        return getResponseMessage(StatusEnum.OK, "모든 알림 읽음 처리", null);
    }

    private String getConstructorId(String userUuid) throws NullPointerException {
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }

    private String getMyUuid(String token) throws NullPointerException{
        return jwtTokenProvider.getUserUuid(token.substring(7));
    }
}
