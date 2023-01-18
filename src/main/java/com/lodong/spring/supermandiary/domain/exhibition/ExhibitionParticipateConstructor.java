package com.lodong.spring.supermandiary.domain.exhibition;

import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionParticipateConstructor {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private UserConstructor userConstructor;
    @Column(nullable = false)
    private String constructorName;
    @Column(nullable = false)
    private boolean isAnswer;
    @Column(nullable = false)
    private boolean isPay;
    @Column
    private String constructorIntroduce;
}
