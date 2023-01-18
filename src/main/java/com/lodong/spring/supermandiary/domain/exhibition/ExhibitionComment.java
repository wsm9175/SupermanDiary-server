package com.lodong.spring.supermandiary.domain.exhibition;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionComment {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private ExhibitionBoard exhibitionBoard;
    @Column
    private String commentGroupId;
    @Column
    private int sequence;
    @Column
    private String comment;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_customer_id")
    private UserCustomer userCustomer;
    @Column(nullable = false)
    private LocalDateTime createAt;
}
