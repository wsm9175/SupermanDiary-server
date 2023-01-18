package com.lodong.spring.supermandiary.dto.auth;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CustomerInfoDTO {
    private String name;
    private String sex;
    private LocalDate birth;
    private String phoneNumber;
    private List<String> subPhoneNumberList;
    private String pw;
    private String email;
    private List<CustomerAddressDTO> customerAddressList;
    private boolean isInterestInInterior;
    private List<String> interestTech;
    private boolean isCertification;
    private boolean isAgreeTerm;
}
