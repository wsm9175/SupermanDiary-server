package com.lodong.spring.supermandiary.domain.address;

import lombok.*;

import javax.persistence.*;

@Entity @Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
@Table(name = "sido_areas")
public class SidoAreas {
    @Id
    private int code;
    @Column(nullable = false)
    private String name;

    @PrePersist
    public void prePersist(){

    }
}
