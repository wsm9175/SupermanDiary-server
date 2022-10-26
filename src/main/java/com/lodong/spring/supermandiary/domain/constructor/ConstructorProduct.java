package com.lodong.spring.supermandiary.domain.constructor;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ConstructorProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String introduction;
    @Column(nullable = false)
    private int price;
}
