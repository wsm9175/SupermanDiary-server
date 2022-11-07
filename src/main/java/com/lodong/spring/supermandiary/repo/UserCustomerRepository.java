package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.UserCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCustomerRepository extends JpaRepository<UserCustomer, String> {
}
