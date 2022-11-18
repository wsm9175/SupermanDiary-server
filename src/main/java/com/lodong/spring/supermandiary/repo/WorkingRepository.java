package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.working.Working;
import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface WorkingRepository extends JpaRepository<Working, String> {
    @EntityGraph(value = "get-estimate-info", type = EntityGraph.EntityGraphType.LOAD)
    Optional<Working> findByIdAndConstructorId(String id, String constructorId);
    Optional<List<Working>> findByConstructor(Constructor constructor);
    Optional<List<Working>> findByConstructorIdAndNonMemberPhoneNumber(String constructorId, String phoneNumber);
    Optional<List<Working>> findByConstructorIdAndUserCustomer_PhoneNumbers_phoneNumber(String constructorId, String phoneNumber);

    Optional<List<Working>> findByConstructorIdAndApartmentDongAndApartmentHosu(String constructorId, String dong, String hosu);
    Optional<List<Working>> findByConstructorIdAndOtherHomeDongAndOtherHomeHosu(String constructorId,String dong, String hosu);

    Optional<List<Working>> findByConstructorIdAndApartmentDong(String constructorId,String dong);
    Optional<List<Working>> findByConstructorIdAndApartmentHosu(String constructorId,String hosu);
    Optional<List<Working>> findByConstructorIdAndOtherHomeDong(String constructorId,String dong);
    Optional<List<Working>> findByConstructorIdAndOtherHomeHosu(String constructorId,String hosu);
    @Transactional
    @Modifying
    @Query(value = "UPDATE UserConstructor u set u.refreshToken =:refreshToken where u.phoneNumber = :phoneNumber")
    void insertRefreshToken(String refreshToken, String phoneNumber);
}
