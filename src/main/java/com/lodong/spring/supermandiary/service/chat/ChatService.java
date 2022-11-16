package com.lodong.spring.supermandiary.service.chat;

import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiary.domain.chat.ChatMessage;
import com.lodong.spring.supermandiary.domain.chat.ChatRoom;
import com.lodong.spring.supermandiary.repo.ChatMessageRepository;
import com.lodong.spring.supermandiary.repo.ChatRoomRepository;
import com.lodong.spring.supermandiary.repo.UserCustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {
    private final RabbitTemplate rabbitTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository messageRepository;
    private final UserCustomerRepository userCustomerRepository;
    /**
     * 채팅의 경우 시공사와 고객 1:1 통신이다.
     * 해당서버는 시공사용 서버이다.
     * sender - 시공사
     * receiver - 유저
     */
    @Transactional
    public void send(ChatMessage chatMessage) throws NullPointerException{
        UserCustomer receiver = userCustomerRepository
                .findById(chatMessage.getReceiver())
                .orElseThrow(()->new NullPointerException("받는 회원정보가 데이터베이스에 없습니다."));

        ChatRoom chatRoom = chatRoomRepository
                .findById(chatMessage.getRoomId())
                .orElseThrow(()->new NullPointerException("해당 채팅방은 존재하지 않습니다."));

        chatMessage.setRoom(chatRoom);
        ChatMessage saved = messageRepository.save(chatMessage);
        log.info("message saved : {}", saved);

        rabbitTemplate.convertAndSend(chatMessage.getReceiver(), chatMessage);
    }

 /*   public ChatRoom registerNewChatRoom(String requester, String subject) throws NullPointerException{
        if(isChatRoomAlreadyExists(requester, subject)){
            ChatRoom publicRoomDetails = chatRoomRepository
                    .findByConstructorIdAndUserCustomerId(requester, subject)
                    .orElseThrow(()-> new NullPointerException("채팅방 생성중 오류가 발생했습니다."));
            log.debug("already exists : {}", publicRoomDetails);
            return publicRoomDetails;
        }
*//*
        ChatRoom chatRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .constructor()
                .userCustomer()
                .build();*//*
    }*/

    private boolean isChatRoomAlreadyExists(String requester, String subject) {
        return chatRoomRepository.existsByConstructorIdAndUserCustomerId(requester, subject);
    }
}
