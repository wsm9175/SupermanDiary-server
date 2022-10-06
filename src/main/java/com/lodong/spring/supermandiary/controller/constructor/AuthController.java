package com.lodong.spring.supermandiary.controller.constructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.lodong.spring.supermandiary.domain.*;
import com.lodong.spring.supermandiary.domain.file.FileList;
import com.lodong.spring.supermandiary.dto.*;
import com.lodong.spring.supermandiary.dto.address.AddressDTO;
import com.lodong.spring.supermandiary.service.*;
import com.lodong.spring.supermandiary.service.address.AddressService;
import com.lodong.spring.supermandiary.service.file.SaveFileService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("rest/v1/auth/construct")
public class AuthController {
    private final AuthService authService;
    private final CertifiedPhoneNumberService certifiedPhoneNumberService;
    private final SaveFileService saveFileService;
    private final ConstructorInfoService constructorInfoService;
    private final UserConstructorInfoService userConstructorInfoService;
    private final AddressService addressService;

    public AuthController(AuthService authService, CertifiedPhoneNumberService certifiedPhoneNumberService,
                          SaveFileService saveFileService, ConstructorInfoService constructorInfoService, UserConstructorInfoService userConstructorInfoService,
                            AddressService addressService) {
        this.authService = authService;
        this.certifiedPhoneNumberService = certifiedPhoneNumberService;
        this.saveFileService = saveFileService;
        this.constructorInfoService = constructorInfoService;
        this.userConstructorInfoService = userConstructorInfoService;
        this.addressService = addressService;
    }

    @PostMapping("/registration-user")
    public ResponseEntity<?> registration(@RequestPart UserConstructorDTO user, @RequestPart JSONArray jsonArray) {
        log.info("user data received");
        log.info("user : "+user.toString());

        UserConstructor userConstructor = UserConstructor.builder()
                .id(UUID.randomUUID().toString())
                .pw(user.getPw())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .isCeo(user.isCeo())
                .active(false)
                .accept(false)
                .isCertification(user.isCertification())
                .agreeTerm(user.isAgreeTerm())
                .ageGroup(user.getAgeGroup())
                .career(user.getCareer())
                .build();

        authService.register(userConstructor);

        for (int i = 0; i < jsonArray.size(); i++) {
            LinkedHashMap<String, Object> jsonObject = (LinkedHashMap<String, Object>) jsonArray.get(i);
            String name = (String) jsonObject.get("name");

            UserConstructorTechDTO userConstructorTechDto = new UserConstructorTechDTO();
            userConstructorTechDto.setName(name);

            UserConstructorTech userConstructorTech = UserConstructorTech.builder()
                    .id(UUID.randomUUID().toString())
                    .userConstructorId(userConstructor.getId())
                    .techName(userConstructorTechDto.getName())
                    .build();
            userConstructorInfoService.registerService(userConstructorTech);
        }

        return ResponseEntity.ok(user);
    }

    @PostMapping(value = "/registration-constructor", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registrationConstructor(@RequestPart("file") MultipartFile file, @RequestPart ConstructorDTO constructorDTO,
                                                     @RequestPart JSONArray jsonArray, @RequestPart AddressDTO addressDTO) throws JsonProcessingException {
        log.info(constructorDTO.toString());
        log.info(file.getOriginalFilename());

        //시공사 정보 세팅
        Constructor constructor = Constructor.builder()
                .id(UUID.randomUUID().toString())
                .name(constructorDTO.getName())
                .payActivation(false)
                .orderManage(false)
                .payManage(false)
                .webAdminActive(false)
                .build();
        authService.registerConstructor(constructor);

        //파일 이름을 사업장 UUID + "-" + businessLicense 형태로 지음
        FileList fileList = FileList.builder()
                .id(UUID.randomUUID().toString())
                .name(constructor.getId() + "-businessLicense" + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")))
                .extension(file.getContentType())
                .build();

        saveFileService.saveBusinessLicense(fileList, constructor.getId(), file);

        //사업자 보유 기술 세팅
        for (int i = 0; i < jsonArray.size(); i++) {
            LinkedHashMap<String, Object> jsonObject = (LinkedHashMap<String, Object>) jsonArray.get(i);
            String name = (String) jsonObject.get("name");
            ConstructorTechDetailDTO constructorTechDetailDTO = new ConstructorTechDetailDTO();
            constructorTechDetailDTO.setName(name);

            ConstructorTechDetail constructorTechDetail = ConstructorTechDetail.builder()
                    .id(UUID.randomUUID().toString())
                    .constructorId(constructor.getId())
                    .name(name)
                    .build();
            constructorInfoService.registerService(constructorTechDetail);
        }

        //주소 정보 세팅
        addressService.settingConstructorAddress(addressDTO, constructor.getId());


        return ResponseEntity.ok(constructor);
    }

    @PostMapping("/duplicate-check")
    public @ResponseBody boolean duplicateCheck(@RequestBody PhoneNumberDTO phoneNumber) {
        log.info("phone number received");
        return authService.isDuplicated(phoneNumber.getPhoneNumber());
    }

    @GetMapping("/send-sms")
    public @ResponseBody
    String sendSMS(String phoneNumber) {
        Random rand = new Random();
        String numstr = "";
        for (int i = 0; i < 6; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numstr += ran;
        }
        System.out.println("수신자 번호 : " + phoneNumber);
        System.out.println("인증번호 : " + numstr);
        certifiedPhoneNumberService.certifiedPhoneNumber(phoneNumber, numstr);
        return numstr;
    }

    @PostMapping("/do")
    public TokenInfo auth(@RequestBody UserConstructorDTO user) throws Exception {
        log.info("user data received {}", user);

        assert !user.getPhoneNumber().equals("");
        assert !user.getPw().equals("");
        UserConstructor userConstructor = UserConstructor.builder()
                .phoneNumber(user.getPhoneNumber())
                .pw(user.getPw())
                .build();

        TokenInfo tokenInfo = authService.auth(userConstructor);
        log.info(tokenInfo.toString());
        if (tokenInfo != null) return tokenInfo;
        else throw new Exception();
    }
}
