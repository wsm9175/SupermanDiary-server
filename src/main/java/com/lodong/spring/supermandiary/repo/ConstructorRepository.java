package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.Constructor;
import com.lodong.spring.supermandiary.domain.UserConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConstructorRepository extends JpaRepository<Constructor, String> {
}
