package com.lodong.spring.supermandiary.domain.working;

import com.lodong.spring.supermandiary.domain.file.WorkFile;
import com.lodong.spring.supermandiary.domain.userconstructor.UserConstructor;
import com.lodong.spring.supermandiary.domain.admin.ConstructorProductWorkList;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@NamedEntityGraphs(
        @NamedEntityGraph(name = "workDetail-with-all", attributeNodes = {
                @NamedAttributeNode(value = "working", subgraph = "apartment"),
                @NamedAttributeNode("userConstructor"),
                @NamedAttributeNode(value = "workFileList", subgraph = "file")
        },
                subgraphs = {
                        @NamedSubgraph(name = "apartment", attributeNodes = {
                                @NamedAttributeNode("apartment"),
                                @NamedAttributeNode("otherHome"),
                                @NamedAttributeNode("constructorProduct"),
                                @NamedAttributeNode("nowWorkInfo"),
                        }),
                        @NamedSubgraph(name="file", attributeNodes = {
                                @NamedAttributeNode("fileList")
                        })
                }
        )
)
public class WorkDetail {
    @Id
    private String id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_id")
    private Working working;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_constructor_id")
    private UserConstructor userConstructor;
    @Column
    private String note;
    @Column
    private LocalDate estimateWorkDate;
    @Column
    private LocalTime estimateWorkTime;
    @Column
    private LocalDate actualWorkDate;
    @Column
    private LocalTime actualWorkTime;
    @Column
    private boolean isComplete;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private int sequence;
    @Column(nullable = false)
    private boolean isFileIn;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "workDetail")
    private List<WorkFile> workFileList = new ArrayList<>();


}
