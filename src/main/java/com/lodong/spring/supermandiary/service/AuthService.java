package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.repo.UserConstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserConstructorRepository userConstructorRepository;

    public UserConstructor auth(UserConstructor user){
        UserConstructor userConstructor = userConstructorRepository.findById(user.getId()).orElseThrow();
        if(userConstructor.getPw().equals(user.getPw())){
            return UserConstructor.getPublicProfile(userConstructor);
        }else{
            return null;
        }
    }
}
