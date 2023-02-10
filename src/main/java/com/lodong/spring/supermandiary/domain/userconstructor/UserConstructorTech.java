package com.lodong.spring.supermandiary.domain.userconstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lodong.spring.supermandiary.domain.constructor.Product;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class UserConstructorTech {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_constructor_id")
    private UserConstructor userConstructor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @PrePersist
    public void prePersist() {

    }

}
