package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WorkDetailRepository extends JpaRepository<WorkDetail, String> {
    public Optional<List<WorkDetail>> findByWorkingConstructorIdAndEstimateWorkDate(String constructorId, LocalDate date);
    public Optional<List<WorkDetail>> findByWorkingConstructorIdAndEstimateWorkDateIsNull(String constructorId);
}
