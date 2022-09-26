package com.lodong.spring.supermandiary.dto;

import lombok.Data;
import lombok.ToString;

@Data @ToString
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
    private boolean agreeTerm;
    private int ageGroup;
    private int career;

}
