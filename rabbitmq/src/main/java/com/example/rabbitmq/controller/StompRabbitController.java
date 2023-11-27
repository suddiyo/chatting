package com.example.rabbitmq.controller;

import com.example.rabbitmq.dto.ChatMessage;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@RequiredArgsConstructor
@Controller
public class StompRabbitController {
    private final RabbitTemplate template;

    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";
    private final static String CHAT_QUEUE_NAME = "chat.queue";

    @MessageMapping("chat.enter.{chatRoomId}")
    public void enter(ChatMessage message, @DestinationVariable String chatRoomId) {

        message.setMessage("🐶 " + message.getNickname() + "님이 입장하셨습니다.");

//        chat.setRegDate(LocalDateTime.now());

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, message); // exchange
        //template.convertAndSend("room." + chatRoomId, chat); //queue
//        template.convertAndSend("amq.topic", "room." + chatRoomId, message); //topic
    }


    @MessageMapping("chat.message.{chatRoomId}")
    public void send(ChatMessage message, @DestinationVariable String chatRoomId) {

//        chat.setRegDate(LocalDateTime.now());

        template.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, message);
        //template.convertAndSend( "room." + chatRoomId, chat);
//        template.convertAndSend("amq.topic", "room." + chatRoomId, message);
    }

    //receive()는 단순히 큐에 들어온 메세지를 소비만 한다. (현재는 디버그용도)
    @RabbitListener(queues = CHAT_QUEUE_NAME)
    public void receive(ChatMessage message) {
        System.out.println("received : " + message.getMessage());
    }

}
