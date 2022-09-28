package com.lodong.spring.supermandiary.domain.address;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.domain.UserConstructorTech;
import lombok.*;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity @Getter @Setter
@ToString @Builder
@AllArgsConstructor @NoArgsConstructor

public class SidoAreas {
    @Id
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int code;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="sidoAreas")
    private Set<SiggAreas> siggAreas = new LinkedHashSet<>();

    @PrePersist
    public void prePersist(){

    }
}
