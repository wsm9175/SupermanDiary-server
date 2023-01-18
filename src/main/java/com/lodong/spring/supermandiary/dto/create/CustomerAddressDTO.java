package com.lodong.spring.supermandiary.dto.create;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class CustomerAddressDTO {
    private String apartmentId;
    private String apartmentName;
    private String apartmentDong;
    private String apartmentHosu;
    private String otherHomeId;
    private String otherHomeName;
    private String otherHomeDong;
    private String otherHomeHosu;

    public CustomerAddressDTO(String apartmentId, String apartmentName, String apartmentDong, String apartmentHosu) {
        this.apartmentId = apartmentId;
        this.apartmentName = apartmentName;
        this.apartmentDong = apartmentDong;
        this.apartmentHosu = apartmentHosu;
    }
}
