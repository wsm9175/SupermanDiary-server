package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.exhibition.ExhibitionBoard;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ExhibitionBoardRepository extends JpaRepository<ExhibitionBoard, String> {
    @EntityGraph(value = "get-with-all", type = EntityGraph.EntityGraphType.LOAD)
    public Optional<List<ExhibitionBoard>> findByConstructorId(String constructorId);
}
