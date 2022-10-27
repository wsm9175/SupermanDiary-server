package com.lodong.spring.supermandiary.domain;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j @Entity @ToString
@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor

public class Working {
    //공통변수
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id", nullable = false)
    private Constructor constructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ConstructorProduct constructorProduct;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estimate_id", unique = true, nullable = false)
    private Estimate estimate;

    @Column(nullable = false)
    private boolean isCompleteConstruct;
    @Column(nullable = false)
    private boolean isCompletePay;

    //아파트, 기타건물 나뉨
    //아파트
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    private String apartmentDong;
    private String apartmentHosu;
    private String apartmentType;
    //기타건물
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "other_home_id")
    private OtherHome otherHome;
    private String otherHomeDong;
    private String otherHomeHosu;
    private String otherHomeType;

    //회원/비회원 주문 나뉨
    //회원
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserCustomer userCustomer;
    //비회원
    private String nonMemberName;
    private String nonMemberPhoneNumber;
}
