package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.domain.UserConstructorTech;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AffiliatedInfoRepository extends JpaRepository<AffiliatedInfo, Integer> {
    Optional<AffiliatedInfo> findByUserConstructor(UserConstructor userConstructor);
}
