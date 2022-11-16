package com.lodong.spring.supermandiary.domain.chat;

import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class ChatRoom {
    @Id
    private String id;
    @ManyToOne
    private Constructor constructor;
    @ManyToOne
    private UserCustomer userCustomer;
    @Transient
    private String roomUserOneNickName;
    @Transient
    private String roomUserTwoNickName;
    @Transient
    private String roomUserOneId;
    @Transient
    private String roomUserTwoId;


}
