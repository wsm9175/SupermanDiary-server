package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.dto.UserConstructorDTO;
import com.lodong.spring.supermandiary.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("rest/v1/auth/construct")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/do")
    public ResponseEntity<?> auth(@RequestBody UserConstructorDTO user) {
        log.info("user data received {}", user);

        assert !user.getId().equals("");
        assert !user.getPw().equals("");

        UserConstructor userConstructor = UserConstructor.builder()
                .id(user.getId())
                .pw(user.getPw())
                .build();

        UserConstructor authResult = authService.auth(userConstructor);
        if (authResult != null) return ResponseEntity.ok(authResult);
        else return ResponseEntity.badRequest().body(userConstructor.getId());
    }
}
