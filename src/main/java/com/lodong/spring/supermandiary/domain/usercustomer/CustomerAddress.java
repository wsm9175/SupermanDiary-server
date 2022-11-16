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

public class CustomerAddress {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "customer_id")
    private UserCustomer userCustomer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    private String apartmentDong;
    private String apartmentHosu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_home_id")
    private OtherHome otherHome;
    private String otherHomeDong;
    private String otherHomeHosu;

}
