package com.lodong.spring.supermandiary.dto;

import lombok.Data;

@Data
public class UserConstructorDTO {
    private String id;
    private String pw;
    private String name;
    private String phoneNumber;
    private String email;
    private boolean isCeo;
    private boolean active;
    private boolean accept;
    private boolean isCertification;

}
