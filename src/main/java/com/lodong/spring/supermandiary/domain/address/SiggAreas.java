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
    private int code;
    @Column(nullable = false)
    private String name;
    /*@Column(nullable = false)
    private int sidoCode;*/

    @ManyToOne(targetEntity = SidoAreas.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "sidoCode")
    //@JsonBackReference
    private SidoAreas sidoAreas;

    @PrePersist
    public void prePersist() {

    }

}
