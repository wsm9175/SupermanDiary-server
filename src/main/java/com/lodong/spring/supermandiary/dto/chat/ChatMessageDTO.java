package com.lodong.spring.supermandiary.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ChatMessageDTO implements Serializable {
    private String id;
    private boolean confirmed;
    private String message;
    private String receiverId;
    private String senderId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createAt;
    private String chatRoomId;
    private boolean isConstructorSend;
    private String type;

    private String senderName;
    private String receiverName;

    private List<String> imageFileIdList;
}
