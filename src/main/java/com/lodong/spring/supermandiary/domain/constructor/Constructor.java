package com.lodong.spring.supermandiary.domain.constructor;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class Constructor {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column
    private String introduction;
    @Column(nullable = false)
    private boolean payActivation;
    @Column(nullable = false)
    private boolean orderManage;
    @Column(nullable = false)
    private boolean payManage;
    @Column(nullable = false)
    private boolean webAdminActive;

    @PrePersist
    public void prePersist() {

    }

    public Constructor(String id, String name, boolean payActivation, boolean orderManage, boolean payManage, boolean webAdminActive) {
        this.id = id;
        this.name = name;
        this.payActivation = payActivation;
        this.orderManage = orderManage;
        this.payManage = payManage;
        this.webAdminActive = webAdminActive;
    }

    public static Constructor getPublicProfile(Constructor constructor){
        return new Constructor(constructor.getId(), constructor.getName(), constructor.isPayActivation(), constructor.isOrderManage(), constructor.isPayManage(), constructor.isWebAdminActive());
    }
}
