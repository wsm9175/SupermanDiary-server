package com.lodong.spring.supermandiary.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.List;

@Slf4j
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EstimateDetail {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estimate_id")
    private Estimate estimate;
    @Column(nullable = false)
    private String productName;
    @Column(nullable = false)
    private int count;
    @Column(nullable = false)
    private int price;
    @Column(nullable = true)
    private String note;

}
