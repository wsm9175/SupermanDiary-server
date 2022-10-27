package com.lodong.spring.supermandiary.repo;


import com.lodong.spring.supermandiary.domain.Estimate;
import com.lodong.spring.supermandiary.domain.EstimateDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateDetailRepository extends JpaRepository<EstimateDetail, String> {

}
