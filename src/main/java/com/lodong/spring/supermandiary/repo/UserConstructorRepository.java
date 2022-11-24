package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserConstructorRepository extends JpaRepository<UserConstructor, String> {
    Optional<UserConstructor> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    @Transactional
    @Modifying
    @Query(value = "UPDATE UserConstructor u set u.refreshToken =:refreshToken where u.phoneNumber = :phoneNumber")
    void insertRefreshToken(String refreshToken, String phoneNumber);

  /*  @Transactional
    @Modifying
    @Query(value = "UPDATE UserConstructor u set u.active =:isActivate, u.roles =:roles  where u.id = :userConstructorId")
    void updateUserConstructorActivate(String userConstructorId, boolean isActivate, List<T> roles);*/

    @NotNull
    @EntityGraph(value = "userConstructor-with-workDetail", type = EntityGraph.EntityGraphType.LOAD)
    UserConstructor getById(@NotNull String id);

/*    @Query(value = "select DISTINCT c from UserConstructor c left join fetch c.userConstructorTech")
    List<UserConstructor> findAllWithUserConstructorTech();*/
}
