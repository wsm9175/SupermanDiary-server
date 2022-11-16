package com.lodong.spring.supermandiary.domain.working;

import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Slf4j @Entity @ToString
@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class WorkDetail {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    private Working working;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_level_id")
    private ConstructorProductWorkList constructorProductWorkList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_constructor_id")
    private UserConstructor userConstructor;
    @Column
    private String note;
    @Column
    private LocalDate estimateWorkDate;
    @Column
    private LocalTime estimateWorkTime;
    @Column
    private LocalDate actualWorkDate;
    @Column
    private LocalTime actualWorkTime;
    @Column
    private boolean isComplete;
}
