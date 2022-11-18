package com.lodong.spring.supermandiary.domain.file;

import com.lodong.spring.supermandiary.domain.working.WorkDetail;
import lombok.*;

import javax.persistence.*;

@Entity
@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkFile {
    @Id
    private String id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "file_id")
    private FileList fileList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name =  "work_detail_id")
    private WorkDetail workDetail;

}
