package com.lodong.spring.supermandiary.domain.exhibition;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ExhibitionCategory {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;
    @Column
    private String name;
}
