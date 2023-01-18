package com.lodong.spring.supermandiary.dto.create;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
public class CustomerDTO {
    private String customerId;
    private String name;
    private String phoneNumber;
    private List<String> subPhoneNumber;
    private List<CustomerAddressDTO> customerAddressDTOS;

}
