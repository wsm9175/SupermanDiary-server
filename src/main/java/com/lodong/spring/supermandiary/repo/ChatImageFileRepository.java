package com.lodong.spring.supermandiary.repo;


import com.lodong.spring.supermandiary.domain.chat.ChatImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatImageFileRepository extends JpaRepository<ChatImageFile, String> {
}
