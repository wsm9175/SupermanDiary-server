package com.lodong.spring.supermandiary.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApartmentDTO {
    private String id;
    private String apartmentName;
    private String apartmentAddress;
}
