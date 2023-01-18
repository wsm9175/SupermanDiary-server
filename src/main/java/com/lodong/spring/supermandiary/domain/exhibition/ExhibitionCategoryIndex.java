package com.lodong.spring.supermandiary.domain.exhibition;

import jdk.jfr.Category;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ExhibitionCategoryIndex {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private ExhibitionBoard exhibitionBoard;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ExhibitionCategory category;
}

