package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiary.dto.working.*;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.MyInfoService;
import com.lodong.spring.supermandiary.service.WorkingService;
import com.lodong.spring.supermandiary.service.address.ConstructorAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/find-work")
    private ResponseEntity<?> findWork(@RequestHeader(name = "Authorization") String token, String phoneNumber, String dong, String hosu) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "속한 시공사가 없습니다.";
            return getResponseMessage(statusEnum, message);
        }
        try {
            String message = null;
            if (phoneNumber != null) {
                message = phoneNumber + " 에 대한 작업 목록";
            } else if (dong != null && hosu != null) {
                message = dong + " " + hosu + "에 대한 작업 목록";
            } else if (dong != null) {
                message = dong + " 에 대한 작업 목록";
            } else if (hosu != null) {
                message = hosu + " 에 대한 작업 목록";
            } else {
                StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
                message = "전달 파라미터 없음";
                return getResponseMessage(statusEnum, message);
            }
            List<WorkApartmentDto> workApartmentDtos = workingService.findWork(constructorId, phoneNumber, dong, hosu);
            StatusEnum statusEnum = StatusEnum.OK;
            return getResponseMessage(statusEnum, message, workApartmentDtos);
        } catch (Exception e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "에러 발생" + e.getMessage();
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
            String message = workId + " 작업 상세 정보";
            return getResponseMessage(statusEnum, message, workDetailDto);
        } catch (NullPointerException e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        } catch (Exception exception) {
            exception.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "알수 없는 에러가 발생했습니다. " + exception.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @GetMapping("/work-level")
    private ResponseEntity<?> getWorkLevelByWorkId(@RequestHeader(name = "Authorization") String token, String workId) {
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
            WorkLevelDetailDto workLevelDetailDto = workingService.getWorkLevelList(constructorId, workId);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "작업 진행";
            return getResponseMessage(statusEnum, message, workLevelDetailDto);
        } catch (NullPointerException e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "해당 작업은 작업 레벨이 존재하지 않습니다." + e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @GetMapping("/member-list")
    private ResponseEntity<?> getConstructorMember(@RequestHeader(name = "Authorization") String token) {
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
            List<UserConstructorDto> userConstructorDtoList = workingService.getConstructorMember(constructorId);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "시공사 멤버 목록";
            return getResponseMessage(statusEnum, message, userConstructorDtoList);
        } catch (NullPointerException e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.NOT_FOUND;
            String message = "해당 시공사에 작업 가능한 사람이 없습니다.";
            return getResponseMessage(statusEnum, message);
        }
    }
    @GetMapping("/get/estimate")
    private ResponseEntity<?> getEstimate(@RequestHeader(name = "Authorization") String token, String workId) {
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
            EstimateInfoDto estimateInfoDto = workingService.getEstimate(constructorId, workId);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "견적서 정보 불러오기 성공";
            return getResponseMessage(statusEnum, message, estimateInfoDto);
        } catch (Exception e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "견적서 정보를 불러오는데 오류가 발생했습니다." + e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }

}
