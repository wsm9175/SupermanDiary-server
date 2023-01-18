package com.lodong.spring.supermandiary.domain.exhibition;

import com.lodong.spring.supermandiary.domain.Apartment;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionApartment {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;
    @Column
    private String apartmentName;
}
