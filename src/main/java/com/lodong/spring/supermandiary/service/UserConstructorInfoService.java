package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructorTech;
import com.lodong.spring.supermandiary.repo.UserConstructorTechRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConstructorInfoService {
    private final UserConstructorTechRepository userConstructorTechRepository;

    public void registerService(UserConstructorTech userConstructorTech){
        if(userConstructorTech!=null){
            userConstructorTechRepository.save(userConstructorTech);
        }else{
            throw new NullPointerException();
        }

    }
}
