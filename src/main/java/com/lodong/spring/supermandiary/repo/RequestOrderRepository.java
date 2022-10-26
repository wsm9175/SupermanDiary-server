package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.create.RequestOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestOrderRepository extends JpaRepository<RequestOrder, String> {
    public Optional<List<RequestOrder>> findByConstructor(Constructor constructor);
}
