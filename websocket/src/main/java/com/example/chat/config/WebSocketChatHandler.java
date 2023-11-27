package com.example.chat.config;

import com.example.chat.dto.ChatMessageDto;
import com.example.chat.dto.ChatMessageDto.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebSocketChatHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper;
    private final Set<WebSocketSession> sessions = new HashSet<>();
    private final Map<String, Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("WebSocket connection established with session ID: {}", session.getId());
        sessions.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("Received payload from session Id {}: {}", session.getId(), payload);

        ChatMessageDto chatMessageDto = objectMapper.readValue(payload, ChatMessageDto.class);
        log.info("Processed ChatMessageDto from session ID {}: {}", session.getId(), chatMessageDto);

        String chatRoomId = chatMessageDto.getChatRoomId();
        if (!chatRoomSessionMap.containsKey(chatRoomId)) {
            chatRoomSessionMap.put(chatRoomId, new HashSet<>());
        }
        Set<WebSocketSession> chatRoomSession = chatRoomSessionMap.get(chatRoomId);

        if (chatMessageDto.getMessageType().equals(MessageType.ENTER)) {
            chatRoomSession.add(session);
            log.info("Session ID {} joined chat room ID {}", session.getId(), chatRoomId);
        }
        if (chatRoomSession.size() >= 3) {
            removeClosedSession(chatRoomId, chatRoomSession);
        }
        sendMessageToChatRoom(chatMessageDto, chatRoomSession);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("WebSocket connection closed for session ID: {}", session.getId());
        sessions.remove(session);
    }

    private void removeClosedSession(String chatRoomId, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(session -> !sessions.contains(session));
        log.info("Cleaning up closed sessions in chat room ID {}", chatRoomId);
    }

    private void sendMessageToChatRoom(ChatMessageDto chatMessageDto, Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.parallelStream().forEach(session -> sendMessage(session, chatMessageDto));
    }

    private <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
            log.info("Sending message to session ID {}: {}", session.getId(), objectMapper.writeValueAsString(message));
        } catch (IOException e) {
            log.error("Error sending message to session ID {}: {}", session.getId(), e.getMessage(), e);
        }
    }
}
