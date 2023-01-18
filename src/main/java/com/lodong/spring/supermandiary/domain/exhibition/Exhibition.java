package com.lodong.spring.supermandiary.domain.exhibition;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Exhibition {
    @Id
    private String id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private LocalDate createDate;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;
    @Column(nullable = false)
    private boolean isOfflineOn;
    @Column
    private LocalDateTime offlineStartDateTime;
    private LocalDateTime offlineEndDateTime;
}
