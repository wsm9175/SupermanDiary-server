package com.lodong.spring.supermandiary.domain.address;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor

public class ConstructorAddress {
    @Id
    private String constructorId;
    @Column(nullable = false)
    private String address;
    @Column(nullable = false)
    private String addressDetail;

    @PrePersist
    public void prePersist(){

    }
}
