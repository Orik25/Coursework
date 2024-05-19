package com.orik.adminapi.service.impl;

import com.orik.adminapi.DTO.MessageFromAdminDTO;
import com.orik.adminapi.DTO.TelegramResponse;
import com.orik.adminapi.data.DAO.ChatRepository;
import com.orik.adminapi.data.entity.Chat;
import com.orik.adminapi.service.interfaces.MessageSender;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@AllArgsConstructor
public class MessageSenderImpl implements MessageSender {

    private final ChatRepository chatRepository;
    private final RestTemplate restTemplate;

    @Override
    public void sendMessage(String text) {
        List<Chat> chatsInfo = chatRepository.findAll();
        chatsInfo.forEach(
                chat -> sendText(text, chat.getBotToken(), chat.getChatId())
        );

    }

    @Override
    public void sendMessage(String text, byte[] file) {
        List<Chat> chatsInfo = chatRepository.findAll();
        chatsInfo.forEach(
                chat -> sendPhoto(text, file, chat.getBotToken(), chat.getChatId())
        );
    }

    private void sendText(String text, String botToken, Long chatId) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendMessage";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        MessageFromAdminDTO message = new MessageFromAdminDTO();
        message.setChat_id(chatId);
        message.setText(text);
        HttpEntity<MessageFromAdminDTO> requestEntity = new HttpEntity<>(message, headers);
        ResponseEntity<TelegramResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, TelegramResponse.class);
    }

    private void sendPhoto(String text, byte[] file, String botToken, Long chatId) {
        String url = "https://api.telegram.org/bot" + botToken + "/sendPhoto";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("chat_id", chatId.toString());
        body.add("caption", text);
        body.add("photo", new ByteArrayResource(file) {
            @Override
            public String getFilename() {
                return "photo.jpg";
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<TelegramResponse> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, TelegramResponse.class);
    }

}
