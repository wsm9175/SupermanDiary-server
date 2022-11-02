package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConstructorProductRepository extends JpaRepository<ConstructorProduct, String> {
    public Optional<List<ConstructorProduct>> findConstructorProductByConstructor(Constructor constructor);
    public Optional<List<ConstructorProduct>> findConstructorProductByConstructorId(String constructorId);
}
