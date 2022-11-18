package com.lodong.spring.supermandiary.domain.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;

@Entity @Getter @Setter
@ToString @Builder @AllArgsConstructor @NoArgsConstructor
@BatchSize(size = 10)
public class ConstructorProductWorkList {
    @Id
    private String id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ConstructorProduct constructorProduct;
    @Column(nullable = false)
    private int sequence;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private boolean isFileIn;

    @PrePersist
    public void prePersist() {

    }
}
