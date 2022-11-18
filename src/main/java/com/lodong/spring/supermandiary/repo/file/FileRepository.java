package com.lodong.spring.supermandiary.repo.file;

import com.lodong.spring.supermandiary.domain.file.FileList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileList, String> {
    Optional<FileList> findByName(String name);
}
