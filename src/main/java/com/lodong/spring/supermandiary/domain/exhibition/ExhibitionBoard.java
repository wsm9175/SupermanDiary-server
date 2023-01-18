package com.lodong.spring.supermandiary.domain.exhibition;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
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

@NamedEntityGraph(
        name = "get-with-all", attributeNodes = {
            @NamedAttributeNode(value = "exhibition"),
            @NamedAttributeNode(value = "constructor")
        }
)
public class ExhibitionBoard {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exhibition_id")
    private Exhibition exhibition;
    @Column
    private String videoLink;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;
    @Column
    private String constructorContent;
    @Column
    private String tag;
    @Column
    private String constructorName;
    private String constructorPhoneNumber;
    private boolean isAnswer;
    private boolean isPay;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "exhibitionBoard")
    private List<ExhibitionComment> exhibitionCommentList = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "exhibitionBoard")
    private List<ExhibitionCategoryIndex> exhibitionCategoryIndexList;
}
