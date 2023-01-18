package com.lodong.spring.supermandiary.domain.usercustomer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraph(
        name = "get-all-data-user_customer", attributeNodes = {
        @NamedAttributeNode(value = "customerAddressList"),
        @NamedAttributeNode(value = "phoneNumbers")
})

public class UserCustomer implements UserDetails {
    @Id
    private String id;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String sex;
    @Column(nullable = false)
    private boolean interestInInterior;
    @Column(nullable = false)
    private boolean isCertification;
    @Column(nullable = false)
    private boolean isTerm;
    @Column
    private String refreshToken;
    @Column
    private String fcm;
    @ElementCollection(fetch = FetchType.LAZY)
    private List<String> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "userCustomer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CustomerAddress> customerAddressList;

    @JsonIgnore
    @OneToMany(mappedBy = "userCustomer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<CustomerPhoneNumber> phoneNumbers;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return getId();
    }
    @Override
    public String getPassword() {
        return password;
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
