package com.lodong.spring.supermandiary.domain.chat;

import com.google.common.base.Strings;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class ChatMessage implements Serializable {
    @Id
    private String id;
    private boolean confirmed;
    private String message;
    private String receiver;
    private String sender;
    private String timestamp;
    @ManyToOne
    private ChatRoom room;
    @Transient
    private String senderNickName;
    @Transient
    private String receiverNickName;
    @Transient
    private String roomId;

    @PrePersist
    public void prePersist() {
        if(Strings.isNullOrEmpty(timestamp)) this.timestamp = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now());
        this.confirmed = false;
    }

}
