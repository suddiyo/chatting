package com.example.chat.controller;

import com.example.chat.dto.ChatMessageDto;
import com.example.chat.dto.ChatRoomDto;
import com.example.chat.service.ChatService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@RestController
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/rooms")
    public ResponseEntity<Long> createChatRoom(String chatRoomName) {
        Long chatRoomId = chatService.createChatRoom(chatRoomName);
        return ResponseEntity.ok(chatRoomId);
    }

    @GetMapping("/{chatRoomId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(@PathVariable Long chatRoomId) {
        List<ChatMessageDto> messages = chatService.getMessagesByChatRoomId(chatRoomId);
        return ResponseEntity.ok(messages);
    }

    @PostMapping("/{chatRoomId}/messages")
    public ResponseEntity<Void> sendChatMessage(@PathVariable Long chatRoomId, @RequestBody ChatMessageDto chatMessage) {
        chatService.sendMessageToChatRoom(chatRoomId, chatMessage);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rooms")
    public ResponseEntity<List<ChatRoomDto>> getChatRooms() {
        List<ChatRoomDto> chatRooms = chatService.getAllChatRooms();
        return ResponseEntity.ok(chatRooms);
    }

}
