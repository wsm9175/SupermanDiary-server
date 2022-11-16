package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConstructorProductWorkListRepository extends JpaRepository<ConstructorProductWorkList, String> {
    Optional<List<ConstructorProductWorkList>> findByConstructorProduct_Constructor_Id(String constructorId);
}
