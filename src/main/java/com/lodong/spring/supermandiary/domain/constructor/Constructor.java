package com.lodong.spring.supermandiary.domain.constructor;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraph(name = "get-all-data", attributeNodes = {
        @NamedAttributeNode(value = "constructorProducts"),
        @NamedAttributeNode(value = "affiliatedInfoList")
})
public class Constructor {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column
    private String introduction;
    @Column(nullable = false)
    private boolean payActivation;
    @Column(nullable = false)
    private boolean orderManage;
    @Column(nullable = false)
    private boolean payManage;
    @Column(nullable = false)
    private boolean webAdminActive;

    @Column
    private String callingNumber;
    @Column(nullable = false)
    private boolean isCertificatePhoneNumber;
    @Column
    private String bank;
    private String bankAccount;
    private String payTemplate;
    private String orderMethod;
    private String placeOrder;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<ConstructorProduct> constructorProducts = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "constructor")
    private List<AffiliatedInfo> affiliatedInfoList = new ArrayList<>();

    @PrePersist
    public void prePersist() {

    }

    public Constructor(String id, String name, boolean payActivation, boolean orderManage, boolean payManage, boolean webAdminActive) {
        this.id = id;
        this.name = name;
        this.payActivation = payActivation;
        this.orderManage = orderManage;
        this.payManage = payManage;
        this.webAdminActive = webAdminActive;
    }

    public static Constructor getPublicProfile(Constructor constructor) {
        return new Constructor(constructor.getId(), constructor.getName(), constructor.isPayActivation(), constructor.isOrderManage(), constructor.isPayManage(), constructor.isWebAdminActive());
    }
}
