package com.vikram.chat.chat;

import lombok.*;

import java.awt.*;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ChatMessage {
    private String content;
    private String sender;
    private MessageType type;

//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }
//
//    public String getSender() {
//        return sender;
//    }
//
//    public void setSender(String sender) {
//        this.sender = sender;
//    }
//
//    public MessageType getType() {
//        return type;
//    }
//
//    public void setType(MessageType type) {
//        this.type = type;
//    }
//
//    public ChatMessage(String content, String sender, MessageType type) {
//        this.content = content;
//        this.sender = sender;
//        this.type = type;
//    }
//    public ChatMessage() {}

}
