package com.lodong.spring.supermandiary.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.create.RequestOrder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j @Entity @ToString
@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor

public class Estimate {
    @Id
    private String id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ConstructorProduct constructorProduct;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_order_id")
    private RequestOrder requestOrder;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "apartment_code")
    private Apartment apartment;
    @Column(nullable = true)
    private String apartment_dong;
    @Column(nullable = true)
    private String apartment_hosu;
    @Column(nullable = true)
    private String apartment_type;
    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "other_home_id")
    private OtherHome otherHome;
    @Column(nullable = true)
    private String otherHomeDong;
    @Column(nullable = true)
    private String otherHomeHosu;
    @Column(nullable = true)
    private String otherHomeType;
    @Column(nullable = true)
    private String note;
    @Column(nullable = true)
    private int discount;
    @Column(nullable = true)
    private String discountCri;
    @Column(nullable = true)
    private String name;
    @Column(nullable = true)
    private String phoneNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "estimate", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<EstimateDetail> estimateDetails = new ArrayList<>();

}
