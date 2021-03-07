package com.gyy.rabbitmq.consumer;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQListener {


    @RabbitListener(queues = "boot_queue")
    public void ListenerQueue(Message message){
        System.out.println(message);
    }
}
