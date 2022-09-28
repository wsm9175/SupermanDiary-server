package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.repo.UserConstructorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserConstructorRepository userConstructorRepository;
    private final PasswordEncoder passwordEncoder;

    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        return userConstructorRepository.findByPhoneNumber(phoneNumber)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    }
    private UserDetails createUserDetails(UserConstructor userConstructor) {
        log.info("createUserDetails");
        log.info(userConstructor.toString());
        return User.builder()
                .username(userConstructor.getId())
                .password(passwordEncoder.encode(userConstructor.getPassword()))
                .roles(userConstructor.getRoles().toArray(new String[0]))
                .build();
    }

}
