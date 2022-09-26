package com.lodong.spring.supermandiary.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString
@Builder
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class UserConstructorTech {
    @Id
    private String id;
    @Column(nullable = false)
    private String userConstructorId;
    @Column(nullable = false)
    private String techName;

    @PrePersist
    public void prePersist() {

    }

}
