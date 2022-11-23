package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.working.NowWorkInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface NowWorkRepository extends JpaRepository<NowWorkInfo, String> {
    public Optional<NowWorkInfo> findByWorkDetailId(String detailId);
}
