package com.orik.adminapi.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
