package com.lodong.spring.supermandiary.domain;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class Apartment {
    @Id
    private String id;
    @Column(nullable = false)
    private int siggCode;
    @Column(nullable = false)
    private long bjdCode;
    @Column(nullable = false)
    private String name;

    @PrePersist
    public void prePersist() {

    }

}
