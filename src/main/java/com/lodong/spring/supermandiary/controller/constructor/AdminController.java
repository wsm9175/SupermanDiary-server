package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.Apartment;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import com.lodong.spring.supermandiary.dto.ConstructorWorkAreaDTO;
import com.lodong.spring.supermandiary.dto.admin.*;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.AdminService;
import com.lodong.spring.supermandiary.service.ApartmentService;
import com.lodong.spring.supermandiary.service.CertifiedPhoneNumberService;
import com.lodong.spring.supermandiary.service.MyInfoService;
import com.lodong.spring.supermandiary.service.address.AddressService;
import com.lodong.spring.supermandiary.service.address.ConstructorAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private final CertifiedPhoneNumberService certifiedPhoneNumberService;

    public AdminController(AddressService addressService, AdminService adminService, ConstructorAddressService constructorAddressService, JwtTokenProvider jwtTokenProvider, ApartmentService apartmentService, MyInfoService myInfoService, CertifiedPhoneNumberService certifiedPhoneNumberService) {
        this.addressService = addressService;
        this.adminService = adminService;
        this.constructorAddressService = constructorAddressService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.apartmentService = apartmentService;
        this.myInfoService = myInfoService;
        this.certifiedPhoneNumberService = certifiedPhoneNumberService;
    }

    @GetMapping("/get/work-manage")
    public ResponseEntity<?> getWorkList(@RequestHeader(name = "Authorization") String token) {
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }

        WorkManageDto workManageDto = adminService.getWorkList(constructorId);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "작업 목록";
        return getResponseMessage(statusEnum, message, workManageDto);
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
    @PutMapping("/pay/activate")
    public ResponseEntity<?> activatePayManage(@RequestHeader(name = "Authorization") String token, boolean activate){
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        adminService.activatePayManage(constructorId, activate);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "결제 관리 활성화 여부 반영 성공";
        return getResponseMessage(statusEnum, message, null);
    }

    @PutMapping("/pay/update")
    public ResponseEntity<?> payInfoUpdate(@RequestHeader(name = "Authorization") String token, String bank, String bankAccount){
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        adminService.payInfoUpdate(constructorId, bank, bankAccount);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "결제 관리 정보 업데이트 성공";
        return getResponseMessage(statusEnum, message, null);
    }

    @PutMapping("/order/activate")
    public ResponseEntity<?> activateOrderManage(@RequestHeader(name = "Authorization") String token, boolean activate){
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        adminService.activateOrderManage(constructorId, activate);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "발주 관리 활성화 여부 반영 성공";
        return getResponseMessage(statusEnum, message, null);
    }

    @PutMapping("/calling-number/update")
    public ResponseEntity<?> updateCallingNumber(@RequestHeader(name = "Authorization") String token, String phoneNumber){
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        adminService.updateCallingNumber(constructorId, phoneNumber);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "발신번호 변경 성공";
        return getResponseMessage(statusEnum, message, null);
    }

    @GetMapping("/calling-number/certification")
    public ResponseEntity<?> sendSMS(String phoneNumber) {
        Random rand = new Random();
        StringBuilder numstr = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numstr.append(ran);
        }
        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numstr);
        certifiedPhoneNumberService.certifiedPhoneNumber(phoneNumber, numstr.toString());
        return getResponseMessage(StatusEnum.OK, "인증번호", numstr.toString());
    }

    @PutMapping("/pay-template/update")
    public ResponseEntity<?> updatePayTemplate(@RequestHeader(name = "Authorization") String token, @RequestBody PayTemplateDto payTemplate){
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        adminService.updatePayTemplate(constructorId, payTemplate.getContent());
        return getResponseMessage(StatusEnum.OK, "결제 요청 템플릿 업데이트 성공", null);
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

    @GetMapping("/get/worker-manage")
    public ResponseEntity<?> getWorkerManage(@RequestHeader(name = "Authorization") String token) {
        //초대 기술자, 기술자 목록(전화 번호)
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }

        try {
            WorkerManageDto workerManageDto = adminService.getWorkerManage(constructorId);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "기술자 관리 데이터";
            return getResponseMessage(statusEnum, message, workerManageDto);
        } catch (Exception e) {
            e.printStackTrace();
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = "서버 에러 발생" + e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @PutMapping("/control/worker/activate")
    public ResponseEntity<?> controlWorkerActivation(String userId, boolean isActivate) {
        try {
            adminService.controlWorkerActivation(userId, isActivate);
            StatusEnum statusEnum = StatusEnum.OK;
            String message = "유저 activate :" + isActivate + " 반영 성공";
            return getResponseMessage(statusEnum, message, null);
        } catch (NullPointerException nullPointerException) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = nullPointerException.getMessage();
            return getResponseMessage(statusEnum, message);
        }
    }

    @PostMapping("/invite")
    public ResponseEntity<?> inviteMember(@RequestHeader(name = "Authorization") String token, @RequestBody InviteDto invite) {
        adminService.inviteMember(getConstructorId(token), invite);
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "초대 성공";
        return getResponseMessage(statusEnum, message, null);
    }

    @GetMapping("/sales")
    public ResponseEntity<?> getSales(@RequestHeader(name = "Authorization") String token){
        String constructorId;
        try {
            constructorId = getConstructorId(token);
        } catch (NullPointerException e) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        
        try{
            SaleInfoDto saleInfoDto = adminService.getSales(constructorId);
            String message = "완료된 작업 목록(결제 안된 데이터 포함)";
            return getResponseMessage(StatusEnum.OK, message, saleInfoDto);
        }catch (NullPointerException e){
            e.printStackTrace();
            return getResponseMessage(StatusEnum.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/get/product-list")
    public ResponseEntity<?> getProductList(){
        List<ProductInfoDTO> productInfoDTOS = adminService.getProductInfoDTOS();
        return getResponseMessage(StatusEnum.OK, "product list", productInfoDTOS);
    }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }
}
