package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.BusinessLicense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessLicenseRepository extends JpaRepository<BusinessLicense, String> {
}
