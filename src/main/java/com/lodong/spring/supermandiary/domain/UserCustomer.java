package com.lodong.spring.supermandiary.domain;

import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCustomer {
    @Id
    private String id;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String name;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sigg_code")
    private SiggAreas siggAreas;
    @Column(nullable = false)
    private String detailAddress;
    @Column(nullable = false)
    private boolean interestInInterior;
    @Column(nullable = false)
    private boolean isConstructor;
    @Column(nullable = false)
    private boolean isOperator;
    @Column(nullable = false)
    private boolean isCertification;
    @Column(nullable = false)
    private boolean isTerm;
}
