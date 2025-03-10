package com.vikram.chat.repository;

import com.vikram.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    @Query("SELECT cm FROM ChatMessage cm WHERE (cm.senderId = :senderId AND cm.receiverId = :receiverId) OR (cm.senderId = :receiverId AND cm.receiverId = :senderId) ORDER BY cm.timestamp ASC")
    List<ChatMessage> findPrivateChatHistory(@Param("senderId") Long senderId, @Param("receiverId") Long receiverId);
}
