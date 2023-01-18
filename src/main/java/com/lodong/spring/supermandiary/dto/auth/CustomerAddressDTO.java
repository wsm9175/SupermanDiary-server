package com.lodong.spring.supermandiary.dto.auth;

import lombok.Data;

@Data
public class CustomerAddressDTO {
    private String apartmentId;
    private String apartmentDong;
    private String apartmentHosu;
    private String otherHomeId;
    private String otherHomeDong;
    private String otherHomeHosu;
}
