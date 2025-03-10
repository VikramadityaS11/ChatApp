package com.vikram.chat.controller;

import com.vikram.chat.entity.ChatMessage;
import com.vikram.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Controller
public class ChatController {

    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // ✅ Handles public messages
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
        logger.info("Received message: SenderId={}, ReceiverId={}, Content={}",
                chatMessage.getSenderId(), chatMessage.getReceiverId(), chatMessage.getContent());

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        logger.info("Message saved with ID: {}", savedMessage.getId());

        return savedMessage;
    }

    // ✅ Handles new user joining
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        logger.info("User joined: {}", chatMessage.getSenderId());
        headerAccessor.getSessionAttributes().put("userID", chatMessage.getSenderId());
        return chatMessage;
    }

    // ✅ Handles private messages
    @MessageMapping("/chat.privateMessage")
    public void sendPrivateMessage(@Payload ChatMessage chatMessage) {
        logger.info("Private message: SenderId={}, ReceiverId={}, Content={}",
                chatMessage.getSenderId(), chatMessage.getReceiverId(), chatMessage.getContent());

        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);
        logger.info("Private message saved with ID: {}", savedMessage.getId());

        String destination = "/user/" + chatMessage.getReceiverId() + "/queue/messages";
        logger.info("🔄 Sending WebSocket message to user: {} at {}", chatMessage.getReceiverId(), destination);

        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getReceiverId()), "/queue/messages", savedMessage
        );

        logger.info("✅ WebSocket message sent: {}", savedMessage);
    }



    // ✅ Fetch private message history
    @MessageMapping("/chat.history.private")
    public void getPrivateHistory(@Payload ChatMessage chatMessage) {
        List<ChatMessage> history = chatMessageRepository.findPrivateChatHistory(
                chatMessage.getSenderId(), chatMessage.getReceiverId());

        logger.info("Fetched {} messages for chat between {} and {}", history.size(),
                chatMessage.getSenderId(), chatMessage.getReceiverId());

        messagingTemplate.convertAndSendToUser(
                String.valueOf(chatMessage.getSenderId()), "/queue/history", history
        );
    }
}
