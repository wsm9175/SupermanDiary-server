package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ApartmentRepository extends JpaRepository<Apartment, String> {
    Optional<List<Apartment>> findBySiggCode(int siggCode);
}
