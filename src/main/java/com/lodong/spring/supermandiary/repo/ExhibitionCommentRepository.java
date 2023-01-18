package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.exhibition.ExhibitionComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExhibitionCommentRepository extends JpaRepository<ExhibitionComment, String> {
}
