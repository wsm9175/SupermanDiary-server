package com.lodong.spring.supermandiary.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DuplicateCheckDTO {
    private boolean isIsDuplicate;
    private boolean isIsInvite;
    private List<ConstructorInfoDTO> constructorInfoDTO;
}
