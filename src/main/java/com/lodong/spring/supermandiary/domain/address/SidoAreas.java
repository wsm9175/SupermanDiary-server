package com.lodong.spring.supermandiary.domain.address;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity @Getter @Setter
@ToString @Builder
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
