package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.Working;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkingRepository extends JpaRepository<Working, String> {
    Optional<List<Working>> findByConstructor(Constructor constructor);
}
