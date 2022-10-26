package com.lodong.spring.supermandiary.domain.constructor;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
@Getter @Setter @ToString
@Builder
@AllArgsConstructor @NoArgsConstructor
public class ConstructorTechDetail {
    @Id
    private String id;
    @Column(nullable = false)
    private String constructorId;
    @Column(nullable = false)
    private String name;

    @PrePersist
    public void prePersist() {

    }

}
