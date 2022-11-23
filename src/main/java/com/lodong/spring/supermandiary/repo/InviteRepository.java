package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.admin.Invite;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InviteRepository extends JpaRepository<Invite, String> {
    @EntityGraph(value = "get-all-with", type = EntityGraph.EntityGraphType.LOAD)
    public List<Invite> findByConstructorIdAndSignComplete(String constructorId, boolean isSignComplete);
}
