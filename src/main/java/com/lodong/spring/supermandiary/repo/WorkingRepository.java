package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.Working;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface WorkingRepository extends JpaRepository<Working, String> {
    Optional<Working> findByIdAndConstructorId(String id, String constructorId);
    Optional<List<Working>> findByConstructor(Constructor constructor);
    Optional<List<Working>> findByConstructorIdAndUserCustomerPhoneNumber(String constructorId, String phoneNumber);
    Optional<List<Working>> findByConstructorIdAndNonMemberPhoneNumber(String constructorId, String phoneNumber);

    @Transactional
    @Modifying
    @Query(value = "UPDATE UserConstructor u set u.refreshToken =:refreshToken where u.phoneNumber = :phoneNumber")
    void insertRefreshToken(String refreshToken, String phoneNumber);
}
