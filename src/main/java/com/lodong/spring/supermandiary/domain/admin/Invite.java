package com.lodong.spring.supermandiary.domain.admin;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Getter @Setter @ToString
@Builder @AllArgsConstructor @NoArgsConstructor
public class Invite {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String phoneNumber;
    @Column(nullable = false)
    private LocalDateTime createAt;

}
