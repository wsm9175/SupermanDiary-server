package com.lodong.spring.supermandiary.domain.admin;

import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import lombok.*;

import javax.persistence.*;

@Entity @Getter @Setter
@ToString @Builder @AllArgsConstructor @NoArgsConstructor
public class ConstructorProductWorkList {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ConstructorProduct constructorProduct;
    @Column(nullable = false)
    private int sequence;
    @Column(nullable = false)
    private String name;

    @PrePersist
    public void prePersist() {

    }
}
