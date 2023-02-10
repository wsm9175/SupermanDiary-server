package com.lodong.spring.supermandiary.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.gson.Gson;
import com.lodong.spring.supermandiary.domain.chat.ChatImageFile;
import com.lodong.spring.supermandiary.domain.chat.ChatMessage;
import com.lodong.spring.supermandiary.domain.chat.ChatRoom;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.file.FileList;
import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import com.lodong.spring.supermandiary.dto.chat.ChatFilesDTO;
import com.lodong.spring.supermandiary.dto.chat.ChatMessageDTO;
import com.lodong.spring.supermandiary.dto.chat.CreateChatRoomDTO;
import com.lodong.spring.supermandiary.dto.chat.SendChatMessageDTO;
import com.lodong.spring.supermandiary.enumvalue.ChatMessageTypeEnum;
import com.lodong.spring.supermandiary.repo.*;
import com.lodong.spring.supermandiary.repo.file.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final RabbitTemplate rabbitTemplate;
    private final ChatRoomRepository chatRoomRepository;
    private final UserCustomerRepository userCustomerRepository;
    private final ConstructorRepository constructorRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatImageFileRepository chatImageFileRepository;
    private final FileRepository fileRepository;
    private final static String CHAT_EXCHANGE_NAME = "amq.topic";

    private final String STORAGE_ROOT_PATH = "/app/";
    private final String File_PATH = "chat-info/";


    // 시공사 앱임으로 sender - 시공사, receiver 소비자 임으로 가정
    @Transactional
    public ChatMessageDTO send(SendChatMessageDTO message) throws NullPointerException {
        Constructor sender = constructorRepository.findById(message.getSenderId()).orElseThrow(() -> new NullPointerException("잘못된 시공사정보입니다."));
        UserCustomer receiver = userCustomerRepository.findById(message.getReceiverId()).orElseThrow(() -> new NullPointerException("잘못된 회원정보입니다."));
        ChatRoom chatRoom = chatRoomRepository.findById(message.getRoomId()).orElseThrow();

        ChatMessage chatMessage = new ChatMessage(UUID.randomUUID().toString(), false, message.getMessage(), message.getSenderId(), message.getReceiverId(), LocalDateTime.now(), chatRoom, false, message.getType(), sender.getName(), receiver.getName());
        chatMessageRepository.save(chatMessage);

        //message type check - for image saving
        List<String> fileNameList = new ArrayList<>();
        if (Optional.ofNullable(message.getType()).orElse("").equals(ChatMessageTypeEnum.IMAGE.label())) {
            Optional.ofNullable(message.getImageFileIdList()).ifPresent(fileIdList -> {
                List<FileList> fileLists = fileRepository.findAllById(fileIdList);
                List<ChatImageFile> chatImageFiles = new ArrayList<>();
                Optional.of(fileLists).orElseGet(Collections::emptyList).forEach(fileList -> {
                    chatImageFiles.add(new ChatImageFile(UUID.randomUUID().toString(), chatMessage, fileList));
                    fileNameList.add(fileList.getName());
                });
                if (!chatImageFiles.isEmpty()) {
                    chatImageFileRepository.saveAll(chatImageFiles);
                }
            });
        }
        ChatMessageDTO chatMessageDTO
                = new ChatMessageDTO(chatRoom.getId(), chatMessage.isConfirmed(), chatMessage.getMessage(), chatMessage.getReceiver(), chatMessage.getSender(), chatMessage.getCreateAt(), chatMessage.getChatRoom().getId(), chatMessage.isConstructorSend(), chatMessage.getType(), chatMessage.getSenderName(), chatMessage.getReceiverName(), fileNameList.size() == 0 ? null : fileNameList);
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoom.getId(), chatMessageDTO);
        return chatMessageDTO;
    }

    //시공사 서버임으로 senderId가 시공사, receiverId가 소비자
    public CreateChatRoomDTO registerNewChatRoom(String senderId, String receiverId) throws NullPointerException {
        if (isChatRoomAlreadyExists(senderId, receiverId)) {
            ChatRoom chatRoom = chatRoomRepository.findByRoomUserOneAndRoomUserTwo(senderId, receiverId)
                    .orElseThrow(() -> new NullPointerException("채팅방 조회 에러"));
            log.debug("already exists : {}", chatRoom);
            return new CreateChatRoomDTO(chatRoom.getId());
        }
        Constructor constructor = constructorRepository.findById(senderId).orElseThrow(() -> new NullPointerException("잘못된 시공사입니다."));
        UserCustomer userCustomer = userCustomerRepository.findById(receiverId).orElseThrow(() -> new NullPointerException("잘못된 회원입니다."));

        ChatRoom chatRoom = ChatRoom.builder()
                .id(UUID.randomUUID().toString())
                .constructor(constructor)
                .customer(userCustomer)
                .build();

        ChatRoom save = chatRoomRepository.save(chatRoom);
        log.info("room Created : {}", save);
        return new CreateChatRoomDTO(save.getId());
    }

    private boolean isChatRoomAlreadyExists(String senderId, String receiverId) {
        return chatRoomRepository.existsByUserOneAndUserTwo(senderId, receiverId);
    }

    public ChatFilesDTO fileUpload(List<MultipartFile> images) throws IOException {
        List<FileList> fileLists = new ArrayList<>();
        List<String> fileUidList = new ArrayList<>();

        if (images != null) {
            for (MultipartFile image : images) {
                String uuid = UUID.randomUUID().toString();
                String fileName = uuid + Objects.requireNonNull(image.getOriginalFilename()).substring(image.getOriginalFilename().lastIndexOf("."));
                String storage = STORAGE_ROOT_PATH + File_PATH + fileName;
                FileList fileList = new FileList();
                fileList.setId(uuid);
                fileList.setName(fileName);
                fileList.setExtension(image.getContentType());
                fileList.setStorage(storage);
                fileList.setCreateAt(LocalDateTime.now().toString());
                fileLists.add(fileList);
                saveFile(image, storage);
                fileUidList.add(uuid);
            }
            fileRepository.saveAll(fileLists);
        }
        return new ChatFilesDTO(fileUidList);
    }


    @Transactional
    public void sendNotification(ChatMessageDTO chatMessageDTO) throws FirebaseMessagingException, IllegalArgumentException {
        // notification - sender : customer, receiver : constructor
        Constructor constructor = constructorRepository.findById(chatMessageDTO.getReceiverId()).orElseThrow(() -> new NullPointerException("잘못된 시공사 정보"));
        List<AffiliatedInfo> affiliatedInfoList = constructor.getAffiliatedInfoList();
        List<String> fcmList = new ArrayList<>();
        Optional.ofNullable(affiliatedInfoList).orElseGet(Collections::emptyList).forEach(affiliatedInfo -> {
            fcmList.add(affiliatedInfo.getUserConstructor().getFcm());
        });

        List<Message> messages = fcmList.stream().filter(Objects::nonNull).map(token -> Message.builder()
                .putData("title", chatMessageDTO.getSenderName())
                .putData("body", chatMessageDTO.getMessage())
                .putData("data", new Gson().toJson(chatMessageDTO))
                .setToken(token)
                .build()).toList();
        if (messages != null) {
            FirebaseMessaging.getInstance().sendAll(messages);

        }
    }

    /*public ChatRoom getChatRoomId(String id, String subject) {
        return ChatRoom.getPublicRoomDetails(chatRoomRepository.findByRoomUserOneAndRoomUserTwo(id, subject).orElseThrow());
    }*/

    private void saveFile(MultipartFile file, String storage) throws IOException {
        File saveFile = new File(storage);
        file.transferTo(saveFile);
    }
}
