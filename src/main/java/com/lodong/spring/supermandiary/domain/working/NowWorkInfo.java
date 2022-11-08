package com.lodong.spring.supermandiary.domain.working;

import com.lodong.spring.supermandiary.domain.UserConstructor;
import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j @Entity @ToString
@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor

public class NowWorkInfo {
    @Id
    private String id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id", nullable = false)
    private Working working;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_detail_id")
    private WorkDetail workDetail;

}
