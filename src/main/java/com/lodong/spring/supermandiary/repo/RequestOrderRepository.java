package com.lodong.spring.supermandiary.repo;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import com.lodong.spring.supermandiary.domain.create.RequestOrder;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface RequestOrderRepository extends JpaRepository<RequestOrder, String> {
    public Optional<List<RequestOrder>> findByConstructor(Constructor constructor);

    @Transactional
    @Modifying
    @Query(value = "UPDATE RequestOrder u set u.status =:status where u.id = :id")
    public void updateStatus(String status, String id);
}
