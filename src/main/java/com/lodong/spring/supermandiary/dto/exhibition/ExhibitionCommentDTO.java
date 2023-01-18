package com.lodong.spring.supermandiary.dto.exhibition;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ExhibitionCommentDTO {
    private String id;
    private String commentGroupId;
    private int sequence;
    private String comment;
    private String constructorName;
    private String userCustomerName;
    private LocalDateTime createAt;
}
