package com.lodong.spring.supermandiary.domain.usercustomer;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraph(name = "get-with-all-user-customer-alarm", attributeNodes = {
        @NamedAttributeNode(value = "userCustomer")
})

public class UserCustomerAlarm {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_customer_id")
    private UserCustomer userCustomer;
    @Column(nullable = false)
    private String kind;
    @Column(nullable = false)
    private String content;
    @Column(nullable = true)
    private String detail;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private boolean isReadAlarm;
}
