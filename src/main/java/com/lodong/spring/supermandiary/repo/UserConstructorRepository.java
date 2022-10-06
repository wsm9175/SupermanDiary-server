package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.domain.UserConstructorTech;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserConstructorRepository extends JpaRepository<UserConstructor, String> {
    Optional<UserConstructor> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);

/*    @Query(value = "select DISTINCT c from UserConstructor c left join fetch c.userConstructorTech")
    List<UserConstructor> findAllWithUserConstructorTech();*/
}
