package com.lodong.spring.supermandiary.dto.working;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class WorkApartmentDto {
    private String workId;
    private String apartment;
    private String status;
    private String dong;
    private String hosu;
}
