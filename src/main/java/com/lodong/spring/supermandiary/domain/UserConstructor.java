package com.lodong.spring.supermandiary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodong.spring.supermandiary.domain.working.NowWorkInfo;
import lombok.*;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class UserConstructor implements UserDetails {
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
    @Column(nullable = false)
    private String sex;
    @Column
    private String refreshToken;
    @Column
    private String fcm;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return pw;
    }

    @Override
    public String getUsername() {
        return getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
