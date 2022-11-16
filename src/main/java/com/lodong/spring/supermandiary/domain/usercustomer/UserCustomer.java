package com.lodong.spring.supermandiary.domain.usercustomer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    private String password;
    @Column(nullable = false)
    private String name;
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

    @JsonIgnore
    @OneToMany(mappedBy = "userCustomer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CustomerAddress> customerAddressList = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "userCustomer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CustomerPhoneNumber> phoneNumbers = new ArrayList<>();
}
