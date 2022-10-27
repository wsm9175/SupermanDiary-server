package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.MyInfoService;
import com.lodong.spring.supermandiary.service.address.ConstructorAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/working/construct")

public class WorkingController {
    private final MyInfoService myInfoService;
    private final ConstructorAddressService constructorAddressService;
    private final JwtTokenProvider jwtTokenProvider;

    public WorkingController(MyInfoService myInfoService, ConstructorAddressService constructorAddressService, JwtTokenProvider jwtTokenProvider) {
        this.myInfoService = myInfoService;
        this.constructorAddressService = constructorAddressService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @GetMapping("/work-area")
    private ResponseEntity<?> getWorkArea(@RequestHeader(name = "Authorization") String token){
        log.info("get/work-area");
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

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }
}
