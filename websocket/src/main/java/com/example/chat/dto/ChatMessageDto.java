package com.example.chat.dto;

import com.example.chat.domain.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    public enum MessageType{
        ENTER, TALK
    }

    private MessageType messageType;
    private String chatRoomId;
    private Long senderId;
    private String message;

    public static ChatMessageDto toDto(ChatMessage chatMessage) {
        return ChatMessageDto.builder()
                .chatRoomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSenderId())
                .message(chatMessage.getMessage())
                .build();
    }
}