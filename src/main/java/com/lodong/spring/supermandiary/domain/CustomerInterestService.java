package com.lodong.spring.supermandiary.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerInterestService {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private UserCustomer userCustomer;

    @Column(name = "service_name")
    private String serviceName;
}
