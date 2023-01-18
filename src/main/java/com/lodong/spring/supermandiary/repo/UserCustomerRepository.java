package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.usercustomer.UserCustomer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.Optional;

public interface UserCustomerRepository extends JpaRepository<UserCustomer, String> {
    @Transactional
    @Modifying
    @Query(value = "UPDATE UserCustomer u set u.refreshToken =:refreshToken where u.phoneNumber = :phoneNumber")
    void insertRefreshToken(String refreshToken, String phoneNumber);

    @EntityGraph(value = "get-all-data-user_customer", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<UserCustomer> findByPhoneNumber(String phoneNumber);
}
