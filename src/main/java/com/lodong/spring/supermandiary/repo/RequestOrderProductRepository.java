package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.create.RequestOrder;
import com.lodong.spring.supermandiary.domain.create.RequestOrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RequestOrderProductRepository extends JpaRepository<RequestOrderProduct, String> {
    public Optional<List<RequestOrderProduct>> findRequestOrderProductByRequestOrder(RequestOrder requestOrder);
}
