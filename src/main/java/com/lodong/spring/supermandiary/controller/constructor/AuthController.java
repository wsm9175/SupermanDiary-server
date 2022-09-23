package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.dto.PhoneNumberDTO;
import com.lodong.spring.supermandiary.dto.UserConstructorDTO;
import com.lodong.spring.supermandiary.service.AuthService;
import com.lodong.spring.supermandiary.service.CertifiedPhoneNumberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("rest/v1/auth/construct")
public class AuthController {

    private final AuthService authService;
    private final CertifiedPhoneNumberService certifiedPhoneNumberService;

    public AuthController(AuthService authService, CertifiedPhoneNumberService certifiedPhoneNumberService) {
        this.authService = authService;
        this.certifiedPhoneNumberService = certifiedPhoneNumberService;
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserConstructorDTO user) {
        log.info("user data received");
        ArrayList<String> arr = new ArrayList<>();

        UserConstructor userConstructor = UserConstructor.builder()
                .id(UUID.randomUUID().toString())
                .pw(user.getPw())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .email(user.getEmail())
                .isCeo(user.isCeo())
                .active(user.isActive())
                .accept(user.isAccept())
                .isCertification(user.isCertification())
                .agreeTerm(user.isAgreeTerm())
                .build();

        authService.register(userConstructor);

        return ResponseEntity.ok(user);
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
    public ResponseEntity<?> auth(@RequestBody UserConstructorDTO user) {
        log.info("user data received {}", user);

        assert !user.getId().equals("");
        assert !user.getPw().equals("");

        UserConstructor userConstructor = UserConstructor.builder()
                .phoneNumber(user.getPhoneNumber())
                .pw(user.getPw())
                .build();

        UserConstructor authResult = authService.auth(userConstructor);
        if (authResult != null) return ResponseEntity.ok(authResult);
        else return ResponseEntity.badRequest().body(userConstructor.getId());
    }

}
