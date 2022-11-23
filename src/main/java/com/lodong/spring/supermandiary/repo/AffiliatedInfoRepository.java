package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.userconstructor.AffiliatedInfo;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AffiliatedInfoRepository extends JpaRepository<AffiliatedInfo, Integer> {
    @EntityGraph(value = "get-constructor-id", type = EntityGraph.EntityGraphType.FETCH)
    Optional<AffiliatedInfo> findByUserConstructor(UserConstructor userConstructor);
    @EntityGraph(value = "get-constructor-id", type = EntityGraph.EntityGraphType.FETCH)
    Optional<List<AffiliatedInfo>> findByConstructorId(String constructorId);

}
