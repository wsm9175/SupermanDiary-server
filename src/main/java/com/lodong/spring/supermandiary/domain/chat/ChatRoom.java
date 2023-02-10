package com.lodong.spring.supermandiary.domain.chat;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NamedEntityGraph(
        name = "get-with-all-chatroom", attributeNodes = {
        @NamedAttributeNode(value = "customer"),
        @NamedAttributeNode("constructor"),
        @NamedAttributeNode("chatMessageList")
        }
)

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id", nullable = false, referencedColumnName = "id")
    private Constructor constructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private UserCustomer customer;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "chatRoom")
    private List<ChatMessage> chatMessageList;
}
