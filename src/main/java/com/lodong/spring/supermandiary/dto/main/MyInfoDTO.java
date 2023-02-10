package com.lodong.spring.supermandiary.dto.main;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyInfoDTO {
    private String name;
    private String phoneNumber;
    private String email;
    private int career;
    private String constructorName;
    private List<String> techList;
    private boolean isIsActivate;

    private boolean isIsAdmin;
}
