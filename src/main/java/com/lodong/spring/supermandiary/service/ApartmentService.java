package com.lodong.spring.supermandiary.service;

import com.lodong.spring.supermandiary.domain.Apartment;
import com.lodong.spring.supermandiary.repo.ApartmentRepository;
import kotlin.jvm.Throws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApartmentService {
    private final ApartmentRepository apartmentRepository;
    public List<Apartment> getApartmentBySigg(int siggCode){
        return apartmentRepository.findBySiggCode(siggCode).orElseThrow(()-> new NullPointerException("해당 시군구 코드에 속한 아파트가 존재하지 않습니다."));
    }
}
