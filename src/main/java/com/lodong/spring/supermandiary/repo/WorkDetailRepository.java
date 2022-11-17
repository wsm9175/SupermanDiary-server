package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface WorkDetailRepository extends JpaRepository<WorkDetail, String> {
    public Optional<List<WorkDetail>> findByWorkingConstructorIdAndEstimateWorkDate(String constructorId, LocalDate date);
    public Optional<List<WorkDetail>> findByWorkingConstructorIdAndEstimateWorkDateIsNull(String constructorId);
    public Optional<WorkDetail> findByWorkingIdAndConstructorProductWorkList_Sequence(String id, int sequence);

    @Transactional
    @Modifying
    @Query(value = "UPDATE WorkDetail u set u.note =:note, u.estimateWorkDate = :date, u.estimateWorkTime = :time, u.userConstructor.id =:workerId where u.id = :workDetailId")
    void updateWorkDetail(String workDetailId, LocalDate date, LocalTime time, String workerId, String note);
}
