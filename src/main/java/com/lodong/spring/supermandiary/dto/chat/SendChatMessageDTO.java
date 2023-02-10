package com.lodong.spring.supermandiary.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendChatMessageDTO {
    private String roomId;
    private String senderId;
    private String receiverId;
    private String type;
    private String message;
    private List<String> imageFileIdList;

}
