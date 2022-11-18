package com.lodong.spring.supermandiary.domain.userconstructor;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;

@Entity()
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class UserConstructorTech {
    @Id
    private String id;
    @Column(nullable = false)
    private String userConstructorId;
    @Column(nullable = false)
    private String techName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id",insertable = false, updatable = false)
    @JsonBackReference
    private UserConstructor userConstructor;

    @PrePersist
    public void prePersist() {

    }

}
