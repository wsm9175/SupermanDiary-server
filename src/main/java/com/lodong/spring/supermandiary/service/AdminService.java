package com.lodong.spring.supermandiary.service;


import com.lodong.spring.supermandiary.domain.ConstructorProductWorkList;
import com.lodong.spring.supermandiary.dto.admin.ConstructorProductWorkDto;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.dto.admin.WorkListDto;
import com.lodong.spring.supermandiary.repo.ConstructorProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final ConstructorProductRepository constructorProductRepository;

    public List<ConstructorProductWorkDto> getWorkList(String constructorId) throws NullPointerException{
        List<ConstructorProduct> constructorProducts = constructorProductRepository
                .findConstructorProductByConstructorId(constructorId)
                .orElseThrow(()->new NullPointerException("조회값 없음"));

        List<ConstructorProductWorkDto> constructorProductWorkDtos = new ArrayList<>();

        for(ConstructorProduct constructorProduct : constructorProducts){
            ConstructorProductWorkDto constructorProductWorkDto = new ConstructorProductWorkDto();
            constructorProductWorkDto.setId(constructorProduct.getId());
            constructorProductWorkDto.setProductName(constructorProduct.getName());
            List<WorkListDto> workListDtos = new ArrayList<>();
            for(ConstructorProductWorkList constructorProductWorkList:constructorProduct.getConstructorProductWorkLists()){
                WorkListDto workListDto = new WorkListDto();
                workListDto.setId(constructorProductWorkList.getId());
                workListDto.setSequence(constructorProductWorkList.getSequence());
                workListDto.setWorkName(constructorProductWorkList.getName());
                workListDtos.add(workListDto);
            }
            constructorProductWorkDto.setWorkList(workListDtos);
            constructorProductWorkDtos.add(constructorProductWorkDto);
        }

        return constructorProductWorkDtos;
    }
}
