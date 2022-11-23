package com.lodong.spring.supermandiary.domain.userconstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    @Column(nullable = false)
    private String techName;


    @PrePersist
    public void prePersist() {

    }

}
