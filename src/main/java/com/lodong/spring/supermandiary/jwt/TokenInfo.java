package com.lodong.spring.supermandiary.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class TokenInfo {
    private String grantType; //JWT 인증타입
    private String accessToken;
    private String refreshToken;
}
