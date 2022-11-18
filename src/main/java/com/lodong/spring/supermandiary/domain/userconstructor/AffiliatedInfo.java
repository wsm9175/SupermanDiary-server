package com.lodong.spring.supermandiary.domain.userconstructor;

import com.lodong.spring.supermandiary.domain.constructor.Constructor;
import lombok.*;

import javax.persistence.*;

@NamedEntityGraph(
        name = "get-constructor-id", attributeNodes = {
        @NamedAttributeNode(value = "userConstructor"),
        @NamedAttributeNode("constructor"),
})

@Entity
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AffiliatedInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * insertable = false : 컬럼을 수정한 이후 들어오는 데이터를 막음
     * updatable = false : 칼럼을 수정한 이후 기존에 저장된 데이터를 수정할 수 없게끔 막음
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserConstructor userConstructor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constructor_id")
    private Constructor constructor;

    @PrePersist
    public void prePersist() {

    }
}
