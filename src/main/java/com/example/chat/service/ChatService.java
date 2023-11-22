package com.example.chat.service;

import com.example.chat.domain.ChatMessage;
import com.example.chat.domain.ChatRoom;
import com.example.chat.dto.ChatMessageDto;
import com.example.chat.dto.ChatRoomDto;
import com.example.chat.repository.ChatMessageRepository;
import com.example.chat.repository.ChatRoomRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional
    public Long createChatRoom(String chatRoomName) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return savedChatRoom.getId();
    }

    public List<ChatMessageDto> getMessagesByChatRoomId(Long chatRoomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));

        return chatRoom.getChatMessages().stream()
                .map(ChatMessageDto::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void sendMessageToChatRoom(Long chatRoomId, ChatMessageDto chatMessageDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid chat room ID"));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .senderId(chatMessageDto.getSenderId())
                .message(chatMessageDto.getMessage()).build();

        chatRoom.addMessage(chatMessage);

        chatMessageRepository.save(chatMessage);
    }

    public List<ChatRoomDto> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
                .map(ChatRoomDto::toDto)
                .collect(Collectors.toList());
    }
}
