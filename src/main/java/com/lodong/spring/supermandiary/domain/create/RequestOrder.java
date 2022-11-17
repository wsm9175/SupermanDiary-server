package com.lodong.spring.supermandiary.domain.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodong.spring.supermandiary.domain.Apartment;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.OtherHome;
import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;

@Slf4j
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class RequestOrder {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private UserCustomer customer;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_code")
    private Apartment apartment;
    @Column(nullable = true)
    private String apartment_type;
    @Column(nullable = true)
    private String dong;
    @Column(nullable = true)
    private String hosu;
    @Column(nullable = false)
    private String note;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "other_home_id")
    private OtherHome otherHome;
    @Column(nullable = true)
    private String otherHomeDong;
    @Column(nullable = true)
    private String otherHomeHosu;
    @Column(nullable = true)
    private String status;
    @Column(nullable = true)
    private String otherHomeType;

    /////////////////////////////
    @Column(nullable = false)
    private LocalDate liveInDate;
    @Column(nullable = false)
    private boolean isConfirmationLiveIn;
    @Column(nullable = false)
    private LocalDate requestConstructDate;
    @Column(nullable = false)
    private boolean isConfirmationConstruct;
    @Column(nullable = false)
    private boolean isCashReceipt;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "requestOrder")
    private RequestOrderProduct requestOrderProduct;

}
