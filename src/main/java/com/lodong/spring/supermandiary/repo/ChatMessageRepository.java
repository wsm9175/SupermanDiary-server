package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.chat.ChatMessage;
import com.lodong.spring.supermandiary.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
}
