package com.lodong.spring.supermandiary.repo.file;

import com.lodong.spring.supermandiary.domain.file.BusinessLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessLicenseRepository extends JpaRepository<BusinessLicense, String> {
}
