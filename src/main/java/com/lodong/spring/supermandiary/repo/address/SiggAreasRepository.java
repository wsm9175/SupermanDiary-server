package com.lodong.spring.supermandiary.repo.address;

import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SiggAreasRepository extends JpaRepository<SiggAreas, Integer> {
    Optional<SiggAreas> findByName(String name);
}
