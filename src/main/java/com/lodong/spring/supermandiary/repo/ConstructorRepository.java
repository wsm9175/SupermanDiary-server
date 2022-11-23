package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface ConstructorRepository extends JpaRepository<Constructor, String> {
    @EntityGraph(attributePaths = {"constructorProducts"}, type = EntityGraph.EntityGraphType.LOAD)
    Optional<Constructor> findWithAllById(String constructorId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Constructor u set u.payActivation =:isActivate where u.id = :constructorId")
    void updatePayManage(String constructorId, boolean isActivate);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Constructor u set u.orderManage =:isActivate where u.id = :constructorId")
    void updateOrderManage(String constructorId, boolean isActivate);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Constructor u set u.bank =:bank, u.bankAccount =:bankAccount where u.id = :constructorId")
    void updatePayInfo(String constructorId, String bank, String bankAccount);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Constructor u set u.callingNumber =:callingNumber, u.isCertificatePhoneNumber = true where u.id = :constructorId")
    void updateCallingNumber(String constructorId, String callingNumber);

    @Transactional
    @Modifying
    @Query(value = "UPDATE Constructor u set u.payTemplate =:template where u.id = :constructorId")
    void updatePayTemplate(String constructorId, String template);

}
