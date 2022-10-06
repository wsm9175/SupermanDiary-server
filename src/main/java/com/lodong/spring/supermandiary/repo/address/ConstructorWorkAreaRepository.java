package com.lodong.spring.supermandiary.repo.address;

import com.lodong.spring.supermandiary.domain.Constructor;
import com.lodong.spring.supermandiary.domain.ConstructorWorkArea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConstructorWorkAreaRepository extends JpaRepository<ConstructorWorkArea, String> {
    Optional<List<ConstructorWorkArea>> findByConstructorId(String id);
}
