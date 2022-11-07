package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiary.dto.WorkApartmentDto;
import com.lodong.spring.supermandiary.dto.WorkDetailDto;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.MyInfoService;
import com.lodong.spring.supermandiary.service.WorkingService;
import com.lodong.spring.supermandiary.service.address.ConstructorAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/working/construct")

public class WorkingController {
    private final MyInfoService myInfoService;
    private final ConstructorAddressService constructorAddressService;
    private final WorkingService workingService;
    private final JwtTokenProvider jwtTokenProvider;

    public WorkingController(MyInfoService myInfoService, ConstructorAddressService constructorAddressService, WorkingService workingService, JwtTokenProvider jwtTokenProvider) {
        this.myInfoService = myInfoService;
        this.constructorAddressService = constructorAddressService;
        this.workingService = workingService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping("/work-area")
    private ResponseEntity<?> getWorkArea(@RequestHeader(name = "Authorization") String token) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
            List<ConstructorWorkArea> constructorWorkAreas = constructorAddressService.getWorkAreas(constructorId);
            List<SiggAreas> siggAreas = new ArrayList<>();
            for (ConstructorWorkArea constructorWorkArea : constructorWorkAreas) {
                siggAreas.add(constructorWorkArea.getSiggAreas());
            }
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "success";
            return getResponseMessage(statusEnum, message, siggAreas);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @GetMapping("/work-list")
    private ResponseEntity<?> getWorkList(@RequestHeader(name = "Authorization") String token, int siggCode) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "속한 시공사가 없습니다.";
            return getResponseMessage(statusEnum, message);
        }

        HashMap<String, List<WorkApartmentDto>> workList = workingService.getWorkList(constructorId, siggCode);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = siggCode + "에 해당하는 작업 리스트";
        return getResponseMessage(statusEnum, message, workList);
    }

    @GetMapping("/find-work")
    private ResponseEntity<?> findWorkByPhoneNumber(@RequestHeader(name = "Authorization") String token, String phoneNumber) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "속한 시공사가 없습니다.";
            return getResponseMessage(statusEnum, message);
        }
        List<WorkApartmentDto> workApartmentDtos = workingService.getWorkByPhoneNumber(constructorId, phoneNumber);

        StatusEnum statusEnum = StatusEnum.OK;
        if (workApartmentDtos.size() == 0) {
            String message = phoneNumber + "에 해당하는 작업이 없습니다.";
            return getResponseMessage(statusEnum, message, null);
        }

        String message = phoneNumber + "에 해당하는 작업입니다.";
        return getResponseMessage(statusEnum, message, workApartmentDtos);
    }

    @GetMapping("/work-detail")
    private ResponseEntity<?> getWorkDetailByWorkId(@RequestHeader(name = "Authorization") String token, String workId) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "속한 시공사가 없습니다.";
            return getResponseMessage(statusEnum, message);
        }

        try {
            WorkDetailDto workDetailDto = workingService.getWorkDetailByWork(constructorId, workId);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = workId + "작업 상세 정보";
            return getResponseMessage(statusEnum, message, workDetailDto);
        } catch (NullPointerException e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        } catch (Exception exception){
            exception.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "알수 없는 에러가 발생했습니다. " + exception.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }

}
