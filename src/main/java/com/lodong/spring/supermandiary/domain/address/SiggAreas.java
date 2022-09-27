package com.lodong.spring.supermandiary.domain.address;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity @Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor

public class SiggAreas {
    @Id
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int code;
    @Column(nullable = false)
    private int sidoCode;

    @PrePersist
    public void prePersist(){

    }
}
