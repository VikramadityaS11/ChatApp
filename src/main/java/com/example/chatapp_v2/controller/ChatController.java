package com.example.chatapp_v2.controller;

import com.example.chatapp_v2.entity.ChatMessage;
import com.example.chatapp_v2.entity.MessageType;
import com.example.chatapp_v2.repository.Repo;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class ChatController {

    private final Repo chatMessageRepository;
    private final SimpMessageSendingOperations messagingTemplate;

    public ChatController(Repo chatMessageRepository, SimpMessageSendingOperations messagingTemplate) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(@Payload ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
        return chatMessage;
    }

    @MessageMapping("/addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());

        chatMessage.setType(MessageType.JOIN);
        chatMessage.setContent(chatMessage.getSender() + " joined the chat!");
        chatMessageRepository.save(chatMessage);

        List<ChatMessage> previousMessages = chatMessageRepository.findAll();

        messagingTemplate.convertAndSend("/topic/"+chatMessage.getSender(),previousMessages);

        return chatMessage;
    }

}
