package com.lodong.spring.supermandiary.domain.file;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;

@Entity
@Getter @Setter @ToString @Builder
@AllArgsConstructor @NoArgsConstructor
public class BusinessLicense {
    @Id
    private String id;
    @Column(nullable = false)
    private String constructorId;
    @Column(nullable = false)
    private String fileId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConstructorId() {
        return constructorId;
    }

    public void setConstructorId(String constructorId) {
        this.constructorId = constructorId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    @PrePersist
    public void prePersist() {

    }
}
