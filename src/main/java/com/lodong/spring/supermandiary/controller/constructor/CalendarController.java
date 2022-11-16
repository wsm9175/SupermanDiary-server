package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.dto.calendar.FilterDataDto;
import com.lodong.spring.supermandiary.dto.calendar.WorkDetailByConstructorDto;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.CalendarService;
import com.lodong.spring.supermandiary.service.MyInfoService;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;


import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/calendar/construct")
public class CalendarController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MyInfoService myInfoService;
    private final CalendarService calendarService;

    public CalendarController(JwtTokenProvider jwtTokenProvider, MyInfoService myInfoService, CalendarService calendarService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myInfoService = myInfoService;
        this.calendarService = calendarService;
    }

    @GetMapping("/get/work-list")
    public ResponseEntity<?> getWorkListByDate(@RequestHeader(name = "Authorization") String token, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "속한 시공사가 없습니다.";
            return getResponseMessage(statusEnum, message);
        }
        try {
            HashMap<String, List<WorkDetailByConstructorDto>> map = calendarService
                    .getWorkListByDate(constructorId, date);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = date + "에 해당하는 작업 목록";
            return getResponseMessage(statusEnum, message, map);
        } catch (Exception e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "서버 에러 발생" + e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

     @GetMapping("/get/filter-data")
     public ResponseEntity<?> getFilterData(@RequestHeader(name = "Authorization") String token){
         String constructorId;
         try {
             constructorId = getConstructorId(token);
         } catch (NullPointerException e) {
             StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
             String message = "속한 시공사가 없습니다.";
             return getResponseMessage(statusEnum, message);
         }
        try{
           FilterDataDto filterDataDto = calendarService.getFilterData(constructorId);
           StatusEnum statusEnum = StatusEnum.OK;
           String message  = "필터 데이터 가져오기 성공";
           return getResponseMessage(statusEnum, message, filterDataDto);
        }catch (Exception e){
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "에러 발생 : " + e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
     }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }
}
