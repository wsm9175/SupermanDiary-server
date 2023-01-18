package com.lodong.spring.supermandiary.domain.create;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodong.spring.supermandiary.domain.Apartment;
import com.lodong.spring.supermandiary.domain.Estimate;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.OtherHome;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraph( name = "get-all-with-request_order", attributeNodes = {
        @NamedAttributeNode(value = "constructor"),
        @NamedAttributeNode(value = "customer"),
        @NamedAttributeNode(value = "apartment"),
        @NamedAttributeNode(value = "apartment"),
        @NamedAttributeNode(value = "otherHome")
}
)

public class RequestOrder {
    @Id
    private String id;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserCustomer customer;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_code")
    private Apartment apartment;
    @Column(nullable = true)
    private String apartment_type;
    @Column(nullable = true)
    private String dong;
    @Column(nullable = true)
    private String hosu;
    @Column
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
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
    @Column(nullable = true)
    private boolean cashReceiptPurpose;
    @Column(nullable = true)
    private String cashReceiptPhoneNumber;
    @Column
    private String rejectMessage;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ConstructorProduct constructorProduct;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "requestOrder")
    private Estimate estimate;

}
