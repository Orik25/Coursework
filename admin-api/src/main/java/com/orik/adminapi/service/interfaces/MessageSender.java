package com.orik.adminapi.service.interfaces;

public interface MessageSender {
    void sendMessage(String text);
    void sendMessage(String text, byte[] file);
}
