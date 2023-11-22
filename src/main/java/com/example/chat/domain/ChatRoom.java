package com.example.chat.domain;

import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Builder
@Entity
public class ChatRoom {

    @Id
    private String id;

    private String name;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessages;

    public void addMessage(ChatMessage chatMessage) {
        this.chatMessages.add(chatMessage);
        if (chatMessage.getChatRoom() != this) {
            chatMessage.setChatRoom(this);
        }
    }
}