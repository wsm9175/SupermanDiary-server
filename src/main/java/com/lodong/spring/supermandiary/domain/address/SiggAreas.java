package com.lodong.spring.supermandiary.domain.address;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lodong.spring.supermandiary.domain.UserConstructor;
import lombok.*;

import javax.persistence.*;

@Entity @Getter @Setter @ToString
@Builder
@AllArgsConstructor @NoArgsConstructor

public class SiggAreas {
    @Id
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int code;
    @Column(nullable = false)
    private int sidoCode;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "code",insertable = false, updatable = false)
    @JsonBackReference
    private SidoAreas sidoAreas;

    @PrePersist
    public void prePersist() {

    }

}
