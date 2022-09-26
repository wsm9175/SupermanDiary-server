package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.Constructor;
import com.lodong.spring.supermandiary.domain.FileList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileList, String> {
}
