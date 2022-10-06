package com.lodong.spring.supermandiary.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lodong.spring.supermandiary.domain.Constructor;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity @ToString
@Builder @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Table(name = "constructor_work_area")
public class ConstructorWorkArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne(targetEntity = Constructor.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "constructor_id")
    @JsonBackReference
    private Constructor constructor;

    @ManyToOne(targetEntity = SiggAreas.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "sigg_code")
    //@JsonBackReference
    private SiggAreas siggAreas;

    @PrePersist
    public void prePersist() {

    }

}
