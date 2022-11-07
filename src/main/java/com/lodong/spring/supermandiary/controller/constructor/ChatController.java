package com.lodong.spring.supermandiary.controller.constructor;

import com.lodong.spring.supermandiary.domain.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.chat.ChatMessage;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.service.MyInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("rest/v1/chat/construct")
public class ChatController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MyInfoService myInfoService;

    public ChatController(JwtTokenProvider jwtTokenProvider, MyInfoService myInfoService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.myInfoService = myInfoService;
    }


    @MessageMapping("/msg")
    public void sendMessage(@Payload ChatMessage message, @RequestHeader("Authorization") String token)  {
        log.info("message received : {}", message);
        String constructorId = getConstructorId(token);

    }

    @PostMapping("/new")
    public ResponseEntity<?> newChat(@RequestHeader("Authorization") String token){
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    private String getConstructorId(String token) throws NullPointerException {
        String userUuid = jwtTokenProvider.getUserUuid(token.substring(7));
        AffiliatedInfo affiliatedInfo = myInfoService.getAffiliatedInfo(userUuid);
        return affiliatedInfo.getConstructor().getId();
    }
}
