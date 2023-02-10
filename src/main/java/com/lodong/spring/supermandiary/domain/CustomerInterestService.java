package com.lodong.spring.supermandiary.domain;

import com.lodong.spring.supermandiary.domain.constructor.Product;
import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserCustomer userCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
