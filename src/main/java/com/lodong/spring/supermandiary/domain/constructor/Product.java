package com.lodong.spring.supermandiary.domain.constructor;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Product {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column
    private String detail;
}
