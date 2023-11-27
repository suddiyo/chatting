package com.example.stomp.controller;

import com.example.stomp.dto.ChatMessage;
import com.example.stomp.dto.ChatMessage.MessageType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChatMessageController {

    private final SimpMessageSendingOperations sendingOperations;

    @MessageMapping("/chat/message")
    public void enter(ChatMessage message) {
        if (message.getType().equals(MessageType.ENTER)) {
            message.setMessage("🐶 " + message.getSender() + "님이 입장하셨습니다.");
        }
        sendingOperations.convertAndSend("/topic/chat/room/" + message.getRoomId(), message);
    }
}
