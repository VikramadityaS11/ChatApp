package com.vikram.chat.config;

import com.vikram.chat.entity.ChatMessage;
import com.vikram.chat.entity.MessageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);
    private final SimpMessageSendingOperations messageTemplate;

    public WebSocketEventListener(SimpMessageSendingOperations messageTemplate) {
        this.messageTemplate = messageTemplate;
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userID"); // Fetch userId instead of username

        if (userId != null) {
            log.info("User disconnected: {}", userId);

            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setType(MessageType.LEAVE); // Changed LEAVER → LEAVE
            chatMessage.setSenderId(userId); // Set senderId correctly
            chatMessage.setContent("User left the chat"); // More meaningful message

            messageTemplate.convertAndSend("/topic/public", chatMessage);
        }
    }
}
