package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.constructor.ConstructorTechDetail;
import com.lodong.spring.supermandiary.repo.ConstructorTechDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConstructorInfoService {
    private final ConstructorTechDetailRepository constructorTechDetailRepository;

    public void registerService(ConstructorTechDetail constructorTechDetail){
        if(constructorTechDetail != null){
            constructorTechDetailRepository.save(constructorTechDetail);
        }else{
            throw new NullPointerException();
        }
    }
}
