package com.lodong.spring.supermandiary.domain;

import lombok.*;
import org.springframework.context.annotation.Primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class UserConstructor {
    @Id
    private String id;
    @Column(nullable = false)
    private String pw;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false, unique = true)
    private String phoneNumber;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private int ageGroup;
    @Column(nullable = false)
    private int career;
    @Column(nullable = false)
    private boolean isCeo;
    @Column(nullable = false)
    private boolean active;
    @Column(nullable = false)
    private boolean accept;
    @Column(nullable = false)
    private boolean isCertification;
    @Column(nullable = false)
    private boolean agreeTerm;

    @PrePersist
    public void prePersist() {

    }
    public UserConstructor(String id, String name, String phoneNumber, String email, boolean isCeo, boolean active, boolean accept, int ageGroup, int career) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.isCeo = isCeo;
        this.active = active;
        this.accept = accept;
        this.ageGroup = ageGroup;
        this.career = career;
    }

    public static UserConstructor getPublicProfile(UserConstructor userConstructor) {
        return new UserConstructor(userConstructor.getId(), userConstructor.getName(), userConstructor.getPhoneNumber(),userConstructor.getEmail(), userConstructor.isCeo, userConstructor.isActive(), userConstructor.isAccept(), userConstructor.getAgeGroup(), userConstructor.getCareer());
    }
}
