package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.Constructor;
import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.repo.ConstructorRepository;
import com.lodong.spring.supermandiary.repo.UserConstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserConstructorRepository userConstructorRepository;
    private final ConstructorRepository constructorRepository;

    public void register(UserConstructor user){
      if(isDuplicated(user.getEmail())) throw  new IllegalStateException();
      UserConstructor savedUser = userConstructorRepository.save(user);
      //채팅관련 유저 등록 코드 포함 예정 using savedUser
    }
    public void registerConstructor(Constructor constructor){
        if(isDuplicated(constructor.getId())) throw new IllegalStateException();
        //시공사 save
        constructorRepository.save(constructor);
    }

    public UserConstructor auth(UserConstructor user){
        UserConstructor userConstructor = userConstructorRepository.findByPhoneNumber(user.getPhoneNumber()).orElseThrow();
        if(userConstructor.getPw().equals(user.getPw())){
            return UserConstructor.getPublicProfile(userConstructor);
        }else{
            return null;
        }
    }

    public boolean isDuplicated(String phoneNumber){
        return userConstructorRepository.existsByPhoneNumber(phoneNumber);
    }

    public boolean isDuplicatedConstructor(String id){
        return userConstructorRepository.existsById(id);
    }
}
