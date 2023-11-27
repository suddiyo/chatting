package com.example.rabbitmq.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@RequestMapping("/chat")
@Controller
public class ChatRoomController {

    @GetMapping("/rooms")
    public String getRooms() {
        return "/chat/rooms";
    }


    @GetMapping(value = "/room")
    public String getRoom(Long chatRoomId, String nickname, Model model){
        model.addAttribute("roomId", chatRoomId);
        model.addAttribute("nickname", nickname);
        return "chat/room";
    }

}