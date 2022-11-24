package com.lodong.spring.supermandiary.domain.userconstructor;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserConstructorHoliday {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserConstructor userConstructor;
    @Column(nullable = false)
    private LocalDate date;
}
