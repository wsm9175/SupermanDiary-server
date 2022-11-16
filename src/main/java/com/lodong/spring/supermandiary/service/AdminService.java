package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import com.lodong.spring.supermandiary.domain.admin.Invite;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.dto.admin.*;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.repo.ConstructorProductRepository;
import com.lodong.spring.supermandiary.repo.ConstructorProductWorkListRepository;
import com.lodong.spring.supermandiary.repo.InviteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminService {
    private final ConstructorProductRepository constructorProductRepository;
    private final ConstructorProductWorkListRepository constructorProductWorkListRepository;
    private final InviteRepository inviteRepository;
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
                workListDto.setFileIn(constructorProductWorkList.isFileIn());
                workListDtos.add(workListDto);
            }

            constructorProductWorkDto.setWorkList(workListDtos);
            constructorProductWorkDtos.add(constructorProductWorkDto);
        }

        return constructorProductWorkDtos;
    }

    public void setProduct(String constructorId, ProductDto product){
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();

        ConstructorProduct constructorProduct = new ConstructorProduct();
        constructorProduct.setId(UUID.randomUUID().toString());
        constructorProduct.setConstructor(constructor);
        constructorProduct.setName(product.getName());


        List<ConstructorProductWorkList> constructorProductWorkLists = new ArrayList<>();
        for(ProductWorkDto productWorkDto:product.getProductWorkList()){
            ConstructorProductWorkList constructorProductWorkList = ConstructorProductWorkList.builder()
                    .id(UUID.randomUUID().toString())
                    .constructorProduct(constructorProduct)
                    .sequence(productWorkDto.getSequence())
                    .name(productWorkDto.getName())
                    .build();
            constructorProductWorkLists.add(constructorProductWorkList);
        }
        constructorProduct.setConstructorProductWorkLists(constructorProductWorkLists);

        constructorProductRepository.save(constructorProduct);
        //constructorProductWorkListRepository.saveAll(constructorProductWorkLists);
    }

    public void inviteMember(String constructorId, InviteDto inviteDto){
        Constructor constructor = Constructor.builder()
                .id(constructorId)
                .build();

        Invite invite = Invite.builder()
                .id(UUID.randomUUID().toString())
                .constructor(constructor)
                .name(inviteDto.getName())
                .phoneNumber(inviteDto.getPhoneNumber())
                .createAt(LocalDateTime.now())
                .build();

        inviteRepository.save(invite);
        /////////////// SMS 전송기능 추가 예정
    }
}
