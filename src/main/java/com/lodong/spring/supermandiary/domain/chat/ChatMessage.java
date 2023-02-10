package com.lodong.spring.supermandiary.domain.chat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NamedEntityGraph(
        name = "get-with-all-chatmessage", attributeNodes = {
        @NamedAttributeNode(value = "chatRoom"),
        }
)

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage{
    @Id
    private String id;
    @Column(nullable = false)
    private boolean confirmed;
    @Column(nullable = false)
    private String message;
    @Column(nullable = true)
    private String receiver;
    @Column(nullable = false)
    private String sender;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false, referencedColumnName = "id")
    private ChatRoom chatRoom;
    @Column(nullable = false)
    private boolean isConstructorSend;
    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String senderName;

    @Column(nullable = false)
    private String receiverName;
}
