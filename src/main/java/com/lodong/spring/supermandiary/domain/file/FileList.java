package com.lodong.spring.supermandiary.domain.file;

import lombok.*;
import org.hibernate.jdbc.Work;

import javax.persistence.*;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
@Embeddable
public class FileList {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String extension;

    @Column(nullable = false)
    private String storage;

    @Column(nullable = false)
    private String createAt;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "fileList", cascade = CascadeType.ALL)
    private WorkFile workFile;

    @PrePersist
    public void prePersist() {

    }
}
