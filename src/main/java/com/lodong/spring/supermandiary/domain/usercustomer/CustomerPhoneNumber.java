package com.lodong.spring.supermandiary.domain.usercustomer;


import com.lodong.spring.supermandiary.domain.Apartment;
import com.lodong.spring.supermandiary.domain.OtherHome;
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

public class CustomerPhoneNumber {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "customer_id")
    private UserCustomer userCustomer;

    private String phoneNumber;
}
