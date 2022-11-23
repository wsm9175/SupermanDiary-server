package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface WorkDetailRepository extends JpaRepository<WorkDetail, String> {
    @Override
    @EntityGraph(value = "workDetail-with-all", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<WorkDetail> findById(String id);
    @EntityGraph(value = "workDetail-with-all", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<WorkDetail>> findByWorkingConstructorId(String constructorId);
    public Optional<List<WorkDetail>> findByWorkingConstructorIdAndEstimateWorkDateIsNull(String constructorId);
    public Optional<WorkDetail> findByWorkingIdAndSequence(String id, int sequence);

    @EntityGraph(value = "workDetail-with-all", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<WorkDetail>> findByUserConstructorIdAndWorkingConstructorId(String userId, String constructorId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE WorkDetail u set u.note =:note, u.estimateWorkDate = :date, u.estimateWorkTime = :time, u.userConstructor.id =:workerId where u.id = :workDetailId")
    void updateWorkDetail(String workDetailId, LocalDate date, LocalTime time, String workerId, String note);
}
