package com.lodong.spring.supermandiary.domain.constructor;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraph(name = "get-with-all-constructor-alarm", attributeNodes = {
        @NamedAttributeNode(value = "constructor")
})

public class ConstructorAlarm {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @Column(nullable = false)
    private String kind;
    @Column(nullable = true)
    private String detail;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private LocalDateTime createAt;
    @Column(nullable = false)
    private boolean isReadAlarm;
}
