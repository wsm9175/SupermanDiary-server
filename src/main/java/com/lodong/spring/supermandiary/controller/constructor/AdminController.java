package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.Apartment;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import com.lodong.spring.supermandiary.dto.ConstructorWorkAreaDTO;
import com.lodong.spring.supermandiary.dto.admin.ConstructorProductWorkDto;
import com.lodong.spring.supermandiary.dto.admin.InviteDto;
import com.lodong.spring.supermandiary.dto.admin.ProductDto;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.AdminService;
import com.lodong.spring.supermandiary.service.ApartmentService;
import com.lodong.spring.supermandiary.service.MyInfoService;
import com.lodong.spring.supermandiary.service.address.AddressService;
import com.lodong.spring.supermandiary.service.address.ConstructorAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/admin/construct")
public class AdminController {
    private final AddressService addressService;
    private final AdminService adminService;
    private final ConstructorAddressService constructorAddressService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ApartmentService apartmentService;
    private final MyInfoService myInfoService;

    public AdminController(AddressService addressService, AdminService adminService, ConstructorAddressService constructorAddressService, JwtTokenProvider jwtTokenProvider, ApartmentService apartmentService, MyInfoService myInfoService) {
        this.addressService = addressService;
        this.adminService = adminService;
        this.constructorAddressService = constructorAddressService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.apartmentService = apartmentService;
        this.myInfoService = myInfoService;
    }

    @GetMapping("/get/work-list")
    public ResponseEntity<?> getWorkList(@RequestHeader(name = "Authorization") String token) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }

        List<ConstructorProductWorkDto> constructorProductWorkDtos = adminService.getWorkList(constructorId);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "작업 목록";
        return getResponseMessage(statusEnum, message, constructorProductWorkDtos);
    }

    @PostMapping("/add/work")
    public ResponseEntity<?> getWorkArea(@RequestHeader(name = "Authorization") String token, @RequestBody ProductDto product) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
            adminService.setProduct(constructorId, product);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "작업 삽입 성공";
            return getResponseMessage(statusEnum, message, null);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        } catch (DataIntegrityViolationException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "DB 삽입중 오류가 발생했습니다.";
            return getResponseMessage(statusEnum, message);
        }
    }

    //모든 시군구 지역 요청
    @GetMapping("/get/all-area")
    public List<SiggAreas> getAllArea() {
        log.info("get/all-area");
        List<SiggAreas> siggAreas = addressService.getAllArea();
        return siggAreas;
    }

    //작업 가능 지역 요청
    @GetMapping("/get/work-area")
    public ResponseEntity<?> getArea(@RequestHeader(name = "Authorization") String token) {
        log.info("get/work-area");
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }

        List<ConstructorWorkArea> constructorWorkAreas = constructorAddressService.getWorkAreas(constructorId);
        List<SiggAreas> siggAreas = new ArrayList<>();
        for (ConstructorWorkArea constructorWorkArea : constructorWorkAreas) {
            siggAreas.add(constructorWorkArea.getSiggAreas());
        }

        StatusEnum statusEnum = StatusEnum.OK;
        String message = "success";

        return getResponseMessage(statusEnum, message, siggAreas);
    }

    @PostMapping("/add/work-area")
    public ResponseEntity<?> addWorkArea(@RequestHeader(name = "Authorization") String token, @RequestBody List<ConstructorWorkAreaDTO> constructorWorkAreaDTOS) {
        log.info("/add/work-area");
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }

        List<ConstructorWorkArea> constructorWorkAreas = new ArrayList<>();
        System.out.println("constructorId :" + constructorId);
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();

        for (ConstructorWorkAreaDTO constructorWorkAreaDTO : constructorWorkAreaDTOS) {
            int siggCode = constructorWorkAreaDTO.getSiggCode();
            System.out.println("siggCode : " + siggCode);
            SiggAreas siggAreas = SiggAreas.builder()
                    .code(siggCode)
                    .build();

            ConstructorWorkArea constructorWorkArea = ConstructorWorkArea.builder()
                    .siggAreas(siggAreas)
                    .constructor(constructor)
                    .build();

            System.out.println(constructorWorkArea.toString());
            constructorWorkAreas.add(constructorWorkArea);
        }
        try {
            constructorAddressService.addWorkAreas(constructorWorkAreas);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "success";
            return getResponseMessage(statusEnum, message, null);
        } catch (DataIntegrityViolationException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "해당 지역은 이미 등록되어있습니다.";
            return getResponseMessage(statusEnum, message);
        }
    }

    @GetMapping("/get/work-apartment")
    public ResponseEntity<?> getArea(int siggCode) {
        log.info("get/work-apartment");
        try {
            List<Apartment> apartmentList = apartmentService.getApartmentBySigg(siggCode);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "시군구 코드를 이용해 아파트 목록 받아오기 성공";
            return getResponseMessage(statusEnum, message, apartmentList);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteMember(@RequestHeader(name = "Authorization") String token, @RequestBody InviteDto invite){
        adminService.inviteMember(getConstructorId(token), invite);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "초대 성공";
        return getResponseMessage(statusEnum, message, null);
    }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }


}
