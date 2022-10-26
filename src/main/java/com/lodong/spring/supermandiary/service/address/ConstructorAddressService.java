package com.lodong.spring.supermandiary.service.address;

import com.lodong.spring.supermandiary.domain.constructor.ConstructorWorkArea;
import com.lodong.spring.supermandiary.repo.address.ConstructorWorkAreaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConstructorAddressService {
    private final ConstructorWorkAreaRepository constructorWorkAreaRepository;

    public List<ConstructorWorkArea> getWorkAreas(String id) throws NullPointerException {
        List<ConstructorWorkArea> constructorWorkAreas = constructorWorkAreaRepository.findByConstructorId(id).orElseThrow(
                () -> {
                    throw new NullPointerException();
                });

        return constructorWorkAreas;
    }

    @Transactional
    public void addWorkAreas(List<ConstructorWorkArea> constructorWorkAreas) {
        constructorWorkAreaRepository.saveAll(constructorWorkAreas);
    }
}
