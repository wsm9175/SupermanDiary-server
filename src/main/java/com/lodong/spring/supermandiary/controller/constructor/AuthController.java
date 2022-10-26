package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.*;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorTechDetail;
import com.lodong.spring.supermandiary.domain.file.FileList;
import com.lodong.spring.supermandiary.dto.*;
import com.lodong.spring.supermandiary.dto.address.AddressDTO;
import com.lodong.spring.supermandiary.dto.auth.ConstructorIdDTO;
import com.lodong.spring.supermandiary.dto.jwt.TokenRequestDTO;
import com.lodong.spring.supermandiary.jwt.TokenInfo;
import com.lodong.spring.supermandiary.responseentity.StatusEnum;
import com.lodong.spring.supermandiary.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.ValidationException;
import java.util.*;

import static com.lodong.spring.supermandiary.util.MakeResponseEntity.getResponseMessage;

@Slf4j
@RestController
@RequestMapping("rest/v1/auth/construct")
public class AuthController {
    private final AuthService authService;
    private final CertifiedPhoneNumberService certifiedPhoneNumberService;

    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthService authService, CertifiedPhoneNumberService certifiedPhoneNumberService, PasswordEncoder passwordEncoder) {
        this.authService = authService;
        this.certifiedPhoneNumberService = certifiedPhoneNumberService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/registration-user")
    public ResponseEntity<?> registration(@RequestPart UserConstructorDTO user, @RequestPart List<UserConstructorTechDTO> jsonArray
            , @RequestPart ConstructorIdDTO constructorId) {
        log.info("user data received");
        log.info("user : " + user.toString());

        UserConstructor userConstructorForLogin = UserConstructor.builder()
                .pw(user.getPw())
                .phoneNumber(user.getPhoneNumber())
                .build();

        UserConstructor userConstructorEncode = UserConstructor.builder()
                .id(UUID.randomUUID().toString())
                .pw(passwordEncoder.encode(user.getPw()))
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
                .roles(Collections.singletonList("USER"))
                .sex(user.getSex())
                .wireService(user.getWireService())
                .build();
        List<UserConstructorTech> userConstructorTechList = new ArrayList<>();

        for (UserConstructorTechDTO userConstructorTechDTO : jsonArray) {
            log.info(userConstructorTechDTO.toString());
            UserConstructorTech userConstructorTech = UserConstructorTech.builder()
                    .id(UUID.randomUUID().toString())
                    .userConstructorId(userConstructorEncode.getId())
                    .techName(userConstructorTechDTO.getName())
                    .build();
            userConstructorTechList.add(userConstructorTech);
        }

        Constructor constructor = Constructor.builder()
                .id(constructorId.getConstructorId())
                .build();

        AffiliatedInfo affiliatedInfo = AffiliatedInfo.builder()
                .constructor(constructor)
                .userConstructor(userConstructorEncode)
                .build();

        try {
            authService.register(userConstructorEncode, userConstructorTechList, affiliatedInfo);
        } catch (IllegalStateException illegalStateException) {
            log.info(illegalStateException.getMessage());
            StatusEnum status = StatusEnum.BAD_REQUEST;
            String message = illegalStateException.getMessage();
            return getResponseMessage(status, message);
        } catch (Exception e) {
            log.info(e.getMessage());
            StatusEnum status = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(status, message);
        }

        try {
            TokenInfo tokenInfo = loginAfterRegister(userConstructorForLogin);
            return ResponseEntity.ok(tokenInfo);
        } catch (Exception e) {
            StatusEnum status = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(status, message);
        }
    }

    @PostMapping(value = "/registration-constructor", consumes = {"multipart/form-data"})
    public ResponseEntity<?> registrationConstructor(@RequestPart("file") MultipartFile file, @RequestPart ConstructorDTO constructorDTO,
                                                     @RequestPart List<ConstructorTechDetailDTO> jsonArray, @RequestPart AddressDTO addressDTO, @RequestPart UserConstructorDTO user) {
        log.info(constructorDTO.toString());
        log.info(file.getOriginalFilename());

        //유저 정보 세팅
        UserConstructor userConstructorEncode = UserConstructor.builder()
                .id(UUID.randomUUID().toString())
                .pw(passwordEncoder.encode(user.getPw()))
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
                .sex(user.getSex())
                .wireService(user.getWireService())
                .roles(Collections.singletonList("ADMIN"))
                .build();

        UserConstructor userConstructorForLogin = UserConstructor.builder()
                .pw(user.getPw())
                .phoneNumber(user.getPhoneNumber())
                .build();

        //시공사 정보 세팅
        Constructor constructor = Constructor.builder()
                .id(UUID.randomUUID().toString())
                .name(constructorDTO.getName())
                .payActivation(false)
                .orderManage(false)
                .payManage(false)
                .webAdminActive(false)
                .build();

        //파일 이름을 사업장 UUID + "-" + businessLicense 형태로 지음
        FileList fileList = FileList.builder()
                .id(UUID.randomUUID().toString())
                .name(constructor.getId() + "-businessLicense" + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")))
                .extension(file.getContentType())
                .build();

        List<ConstructorTechDetail> constructorTechDetailList = new ArrayList<>();

        //사업자 보유 기술 세팅
        for (ConstructorTechDetailDTO constructorTechDetailDTO : jsonArray) {
            log.info(constructorTechDetailDTO.toString());
            ConstructorTechDetail constructorTechDetail = ConstructorTechDetail.builder()
                    .id(UUID.randomUUID().toString())
                    .constructorId(constructor.getId())
                    .name(constructorDTO.getName())
                    .build();

            constructorTechDetailList.add(constructorTechDetail);
        }

        AffiliatedInfo affiliatedInfo = AffiliatedInfo.builder()
                .constructor(constructor)
                .userConstructor(userConstructorEncode)
                .build();

        try {
            authService.registerConstructor(userConstructorEncode, constructor, fileList, file, addressDTO, constructorTechDetailList, affiliatedInfo);
        } catch (IllegalStateException illegalStateException) {
            StatusEnum status = StatusEnum.BAD_REQUEST;
            String message = illegalStateException.getMessage();
            return getResponseMessage(status, message);
        } catch (Exception e) {
            StatusEnum status = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(status, message);
        }

        try {
            TokenInfo tokenInfo = loginAfterRegister(userConstructorForLogin);
            return ResponseEntity.ok(tokenInfo);
        } catch (Exception e) {
            StatusEnum status = StatusEnum.BAD_REQUEST;
            String message = e.getMessage();
            return getResponseMessage(status, message);
        }
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
        if (tokenInfo != null) {
            String refreshToken = tokenInfo.getRefreshToken();
            authService.insertRefreshToken(refreshToken, user.getPhoneNumber());
            return tokenInfo;
        } else throw new Exception();
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> validateRefreshToken(@RequestBody TokenRequestDTO token) {
        TokenInfo tokenInfo;
        try {
            tokenInfo = authService.reissue(token);
        } catch (NullPointerException nullPointerException) {
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = nullPointerException.getMessage();
            return getResponseMessage(statusEnum, message);
        } catch (ValidationException validationException){
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = validationException.getMessage();
            return getResponseMessage(statusEnum, message);
        } catch (RuntimeException runtimeException){
            StatusEnum statusEnum = StatusEnum.BAD_REQUEST;
            String message = runtimeException.getMessage();
            return getResponseMessage(statusEnum, message);
        }
        StatusEnum statusEnum = StatusEnum.OK;
        String message = "토근 재발급 성공";
        System.out.println(tokenInfo.toString());
        return getResponseMessage(statusEnum, message, tokenInfo);
    }

    private TokenInfo loginAfterRegister(UserConstructor userConstructor) throws Exception {
        TokenInfo tokenInfo = authService.auth(userConstructor);
        log.info(tokenInfo.toString());
        if (tokenInfo != null) {
            String refreshToken = tokenInfo.getRefreshToken();
            authService.insertRefreshToken(refreshToken, userConstructor.getPhoneNumber());
            return tokenInfo;
        } else throw new Exception();
    }
}
