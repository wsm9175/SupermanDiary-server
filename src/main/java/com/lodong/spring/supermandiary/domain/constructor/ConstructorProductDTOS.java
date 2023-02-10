package com.lodong.spring.supermandiary.domain.constructor;

import com.lodong.spring.supermandiary.dto.auth.ConstructorProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstructorProductDTOS {
    private List<ConstructorProductDTO> constructorProductDTOList;
}
