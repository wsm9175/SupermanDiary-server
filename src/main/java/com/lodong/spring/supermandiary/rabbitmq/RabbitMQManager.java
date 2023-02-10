package com.lodong.spring.supermandiary.rabbitmq;

import com.lodong.spring.supermandiary.enumvalue.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQManager {
    /*
     * rabbitMQ의 3대 컴포넌트
     * Exchange - rabbitmq로 전송한 메시지를 수신하고 메시지를 보낼 위치를 결정한다.
     * 발행한 메시지가 처음 도착하는 지점, 메시지가 목적지에 도달할 수 있도록 라우팅 규칙적용
     * 다양한 라우팅 규칙이 존재
     * Queue
     * AMQP 스펙에 따라 큐 설정이 immutable함
     * 수신한 메시지를 저장하는 역할을 한다. 메시지에 수행하는 작업을 정의하는 설정정보가 있다.
     * 자동 삭제 큐, 큐 독점 설정, 자동 메시지 만료, 대기 메시지 수 제한, 오래된 메시지 큐에서 제거
     * Binding
     * exchange와 queue간의 가상 연결로 메시지가 exchange에서 큐로 이동할 수 있도록 하는 역할
     * Binding과 Binding key는 exchange가 어떤 큐에 메시지를 전달해야 하는지를 의미한다.
     * 익스체인지에 메시지를 발행할 때 애플리케이션은 라우팅 키 속성을 사용한다.
     * */
    private final AmqpAdmin amqpAdmin;

    public void declareQueue(String username, boolean durable) {
        Queue queue = new Queue(username, durable);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding(queue, topicExchange(), username));
    }

    private TopicExchange topicExchange() {
        return new TopicExchange(Exchange.EXCHANGE.getExchange());
    }

    private Binding binding(Queue queue, TopicExchange topicExchange, String routingKey) {
        return BindingBuilder
                .bind(queue)
                .to(topicExchange)
                .with(routingKey);
    }
}