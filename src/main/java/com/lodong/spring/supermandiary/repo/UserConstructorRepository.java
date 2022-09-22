package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.UserConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserConstructorRepository extends JpaRepository<UserConstructor, String> {
    Optional<UserConstructor> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
}
