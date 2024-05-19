package com.orik.botapi.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chats")
public class Chat {

    @Id
    private Long chatId;

    @Column(name = "bot_token")
    private String botToken;

    @Override
    public String toString() {
        return "Chat{" +
                "chatId=" + chatId +
                '}';
    }
}
