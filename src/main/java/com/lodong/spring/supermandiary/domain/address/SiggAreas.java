package com.lodong.spring.supermandiary.domain.address;

import lombok.*;

import javax.persistence.*;

@Entity @Getter @Setter
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
