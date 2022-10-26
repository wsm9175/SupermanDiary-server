package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.repo.AffiliatedInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MyInfoService {
    private final AffiliatedInfoRepository affiliatedInfoRepository;

    public AffiliatedInfo getAffiliatedInfo(String uuid) throws NullPointerException{
        UserConstructor userConstructor = UserConstructor.builder()
                .id(uuid)
                .build();
        return affiliatedInfoRepository.findByUserConstructor(userConstructor).orElseThrow(
                () -> new NullPointerException());
    }
}
