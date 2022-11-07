package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.admin.Invite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InviteRepository extends JpaRepository<Invite, String> {

}
