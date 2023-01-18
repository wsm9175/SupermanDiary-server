package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructorHoliday;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface UserConstructorHolidayRepository extends JpaRepository<UserConstructorHoliday, String> {
    @EntityGraph(attributePaths = {"userConstructor"}, type = EntityGraph.EntityGraphType.LOAD)
    public void deleteByUserConstructorIdAndDate(String constructorId, LocalDate date);
}
