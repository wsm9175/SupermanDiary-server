package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.constructor.ConstructorAlarm;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ConstructorAlarmRepository extends JpaRepository<ConstructorAlarm, String> {
    @EntityGraph(value = "get-with-all-constructor-alarm", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<ConstructorAlarm>> findByConstructor_IdAndIsReadAlarm(String constructorId, boolean isRead);
    @Transactional
    @Modifying
    @Query(value = "UPDATE ConstructorAlarm u set u.isReadAlarm =:status where u.id =:id")
    public void updateReadAlarm(boolean status, String id);
}
