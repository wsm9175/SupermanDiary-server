package com.lodong.spring.supermandiary.repo.file;

import com.lodong.spring.supermandiary.domain.file.FileList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileList, String> {
}
