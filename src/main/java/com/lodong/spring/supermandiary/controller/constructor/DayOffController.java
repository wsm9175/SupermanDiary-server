package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.dayoff.DayOfInfoDto;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.DayOffService;
import com.lodong.spring.supermandiary.service.MyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/dayoff/construct")
public class DayOffController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MyInfoService myInfoService;
    private DayOffService dayOffService;

    public DayOffController(JwtTokenProvider jwtTokenProvider, MyInfoService myInfoService, DayOffService dayOffService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myInfoService = myInfoService;
        this.dayOffService = dayOffService;
    }

    @GetMapping("/get/worker")
    public ResponseEntity<?> getWorkerList(@RequestHeader(name = "Authorization") String token){
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        DayOfInfoDto dayOfInfoDto = dayOffService.getWorkerList(constructorId);
        return getResponseMessage(StatusEnum.OK, "작업자 리스트", dayOfInfoDto);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addDayOff(String workerId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date){
        dayOffService.addDayOff(workerId, date);
        return getResponseMessage(StatusEnum.OK, "휴무일 지정 성공", null);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDayOff(String workerId, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)LocalDate date){
        dayOffService.deleteDayOff(workerId, date);
        return getResponseMessage(StatusEnum.OK, "휴무일 삭제 성공", null);
    }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }

}
