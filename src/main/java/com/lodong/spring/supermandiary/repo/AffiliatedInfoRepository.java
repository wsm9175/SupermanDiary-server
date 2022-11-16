package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AffiliatedInfoRepository extends JpaRepository<AffiliatedInfo, Integer> {
    Optional<AffiliatedInfo> findByUserConstructor(UserConstructor userConstructor);
    Optional<List<AffiliatedInfo>> findByConstructorId(String constructorId);
}
