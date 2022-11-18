package com.lodong.spring.supermandiary.domain.constructor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lodong.spring.supermandiary.domain.address.SiggAreas;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "constructor_work_area",
        uniqueConstraints = {
            @UniqueConstraint(
                    name = "constructor_id",
                    columnNames = {"constructor_id", "sigg_code"}
            )
        }
)
public class ConstructorWorkArea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JsonIgnore
    @ManyToOne(targetEntity = Constructor.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id", referencedColumnName = "id")
    private Constructor constructor;
    @JsonIgnore
    @ManyToOne(targetEntity = SiggAreas.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "sigg_code")
    //@JsonBackReference
    private SiggAreas siggAreas;

    @PrePersist
    public void prePersist() {

    }
}
