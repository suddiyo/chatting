package com.example.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessageDto {
    public enum MessageType{
        ENTER, TALK
    }

    private MessageType messageType;
    private Long chatRoomId;
    private Long senderId;
    private String message;
}