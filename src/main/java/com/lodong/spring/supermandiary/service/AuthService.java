package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.Constructor;
import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.domain.TokenInfo;
import com.lodong.spring.supermandiary.jwt.JwtTokenProvider;
import com.lodong.spring.supermandiary.repo.ConstructorRepository;
import com.lodong.spring.supermandiary.repo.UserConstructorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserConstructorRepository userConstructorRepository;
    private final ConstructorRepository constructorRepository;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    public void register(UserConstructor user) {
        if (isDuplicated(user.getEmail())) throw new IllegalStateException();
        UserConstructor savedUser = userConstructorRepository.save(user);
        //채팅관련 유저 등록 코드 포함 예정 using savedUser
    }

    public void registerConstructor(Constructor constructor) {
        if (isDuplicated(constructor.getId())) throw new IllegalStateException();
        //시공사 save
        constructorRepository.save(constructor);
    }

    /* public UserConstructor auth(UserConstructor user){
         UserConstructor userConstructor = userConstructorRepository.findByPhoneNumber(user.getPhoneNumber()).orElseThrow();
         if(userConstructor.getPw().equals(user.getPw())){
             return UserConstructor.getPublicProfile(userConstructor);
         }else{
             return null;
         }
     }
 */
    @Transactional
    public TokenInfo auth(UserConstructor user) {
        log.info("UsernamePasswordAuthenticationToken");
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getPhoneNumber(), user.getPassword());
        log.info("Authentication");
        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        log.info("TokenInfo");
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        log.info("return");
        return tokenInfo;
    }

    public boolean isDuplicated(String phoneNumber) {
        return userConstructorRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean isDuplicatedConstructor(String id) {
        return userConstructorRepository.existsById(id);
    }
}
