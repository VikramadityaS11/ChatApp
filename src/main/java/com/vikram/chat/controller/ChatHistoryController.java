package com.vikram.chat.controller;

import com.vikram.chat.entity.ChatMessage;
import com.vikram.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@RestController
@RequestMapping("/chat/history")
public class ChatHistoryController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    // ✅ Fetch private chat history between two users
    @GetMapping("/private/{senderId}/{receiverId}")
    public ResponseEntity<List<ChatMessage>> getPrivateChatHistory(@PathVariable Long senderId, @PathVariable Long receiverId) {
        List<ChatMessage> messages = chatMessageRepository.findPrivateChatHistory(senderId, receiverId);
        return ResponseEntity.ok(messages);
    }

}
