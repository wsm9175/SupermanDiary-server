package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.constructor.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, String> {
}
