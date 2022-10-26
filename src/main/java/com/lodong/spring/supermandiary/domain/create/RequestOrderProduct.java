package com.lodong.spring.supermandiary.domain.create;

import com.lodong.spring.supermandiary.domain.constructor.ConstructorProduct;
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


public class RequestOrderProduct {
    @Id
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "request_order_id")
    private RequestOrder requestOrder;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private ConstructorProduct constructorProduct;
}
