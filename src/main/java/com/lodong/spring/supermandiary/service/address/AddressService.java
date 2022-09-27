package com.lodong.spring.supermandiary.service.address;

import com.lodong.spring.supermandiary.domain.address.ConstructorAddress;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import com.lodong.spring.supermandiary.dto.address.AddressDTO;
import com.lodong.spring.supermandiary.repo.address.ConstructorAddressRepository;
import com.lodong.spring.supermandiary.repo.address.SiggAreasRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressService {
    private final SiggAreasRepository siggAreasRepository;
    private final ConstructorAddressRepository constructorAddressRepository;

    public void settingConstructorAddress(AddressDTO addressDTO, String constructorId){
        String address = addressDTO.getAddress();
        String addressDetail = addressDTO.getAddressDetail();

        String[] words = address.split(" ");
        StringBuilder sigg = new StringBuilder();
        //시도-시군구-읍면-도로명-건물번호
        for(int i=1;i<=2;i++){
            if(words[i].charAt(words[i].length()-1) == '시' || words[i].charAt(words[i].length()-1) == '군'
                    || words[i].charAt(words[i].length()-1) == '구'){
                sigg.append(i == 1 ? words[i] + " " : words[i]);
            }
        }

        log.info(sigg.toString());

        SiggAreas siggAreas = siggAreasRepository.findByName(sigg.toString()).orElseThrow();
        int siggCode = siggAreas.getCode();

        ConstructorAddress constructorAddress = ConstructorAddress.builder()
                .constructorId(constructorId)
                .siggCode(siggCode)
                .address(address)
                .addressDetail(addressDetail)
                .build();

        constructorAddressRepository.save(constructorAddress);
    }
}
