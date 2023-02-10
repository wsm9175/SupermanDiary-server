package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("select case when count(c)> 0 then true else false end from ChatRoom c where (c.constructor.id=:userOne and c.customer.id=:userTwo) or (c.constructor.id=:userTwo and c.customer.id=:userOne)")
    boolean existsByUserOneAndUserTwo(String userOne, String userTwo);

    @Query("select c from ChatRoom c where (c.constructor.id=:userOne and c.customer.id=:userTwo) or (c.constructor.id=:userTwo and c.customer.id=:userOne)")
    Optional<ChatRoom> findByRoomUserOneAndRoomUserTwo(String userOne, String userTwo);
}
