package com.lodong.spring.supermandiary.repo;


import com.lodong.spring.supermandiary.domain.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, String> {
}
