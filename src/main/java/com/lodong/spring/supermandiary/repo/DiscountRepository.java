package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.Discount;
import com.lodong.spring.supermandiary.domain.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountRepository extends JpaRepository<Discount, String> {
    public void deleteAllByEstimate(Estimate estimate);
}
