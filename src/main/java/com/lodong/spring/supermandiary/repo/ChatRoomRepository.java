package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {
    Optional<ChatRoom> findByConstructorIdAndUserCustomerId(String constructorId, String customerId);
    boolean existsByConstructorIdAndUserCustomerId(String constructorId, String customerId);

}
