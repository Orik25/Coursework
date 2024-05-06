package com.orik.botapi.service.impl;


import com.orik.botapi.config.BotConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final ChatGPTService chatServive;
    private static final String HELP_TEXT = "This bot is created to communicate with Chat GPT from Telegram\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /text {prompt} to get text response from ChatGPT\n\n" +
            "Type /speech {prompt} to get speech from your prompt\n\n" +
            "Type /help to see this message again";

    @Autowired
    public TelegramBot(BotConfig botConfig, ChatGPTService chatServive) {
        this.botConfig = botConfig;
        this.chatServive = chatServive;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "get greeting message"));
        commands.add(new BotCommand("/help", "get info how to use"));
        commands.add(new BotCommand("/text", "get text response from GPT"));
        commands.add(new BotCommand("/speech", "get speech by your text"));
        try {
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error setting bot`d command list: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                Message message = update.getMessage();
                String text = message.getText();

                if (text.contains(botConfig.getBotName())) {
                    Integer messageId = message.getMessageId();
                    Long chatId = message.getChatId();
                    String firstName = message.getFrom().getFirstName();
                    Long userId = message.getFrom().getId();

                    String[] splitText = text.split("@" + botConfig.getBotName());
                    if (splitText.length > 0) {
                        String command = splitText[0];

                        if (splitText.length == 1) {
                            switch (command) {
                                case "/start" -> greeting(chatId, userId, firstName, messageId);
                                case "/help" -> sendMessageInReply(chatId, userId, HELP_TEXT, messageId);
                                case "/text", "/speech" ->
                                        sendMessageInReply(chatId, userId, "Please, enter your prompt after /\"command\"", messageId);
                                default -> sendMessageInReply(chatId, userId, "Sorry, I can`t answer(", messageId);

                            }
                        } else {
                            String messageText = splitText[1].trim();
                            switch (command) {
                                case "/start" -> greeting(chatId, userId, firstName, messageId);
                                case "/help" -> sendMessageInReply(chatId, userId, HELP_TEXT, messageId);
                                case "/text" ->
                                        sendMessageInReply(chatId, userId, chatServive.getTextResponse(List.of(messageText)), messageId);
                                case "/speech" ->
                                        sendAudioReply(chatId, userId, chatServive.getSpeechByText(messageText), messageId);
                                default -> sendMessageInReply(chatId, userId, "Sorry, I can`t answer(", messageId);
                            }
                        }
                    }

                }
            } else if (update.getMessage().hasVoice()) {
                Message message = update.getMessage();
                Voice voice = message.getVoice();
                Long chatId = message.getChatId();
                Integer messageId = message.getMessageId();
                Long userId = message.getFrom().getId();
                String fileId = voice.getFileId();

                GetFile getFile = new GetFile();
                getFile.setFileId(fileId);
                File file;
                try {
                    file = execute(getFile);
                    InputStream inputStream = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath()).openStream();
                    String response = chatServive.getTextBySpeech(inputStream);
                    sendMessageInReply(chatId, userId, response, messageId);
                } catch (TelegramApiException | IOException e) {
                    log.error("Error occurred: " + e.getMessage());
                    sendMessageInReply(chatId, userId, "Sorry, this service is currently unavailable(\nContact the owners", messageId);
                }
            }

        }
    }

    private void greeting(Long chatId, Long userId, String name, Integer replyToMessageId) {
        sendMessageInReply(chatId, userId, "Hi, " + name + ", nice to meet you", replyToMessageId);
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            log.info("Replying to " + chatId);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMessageInReply(Long chatId, Long userId, String textToSend, Integer replyToMessageId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        sendMessage.setReplyToMessageId(replyToMessageId);
        try {
            log.info("Replying to " + userId + " in chat: " + chatId);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendAudioReply(Long chatId, Long userId, byte[] audio, Integer replyToMessageId) {
        if (Arrays.equals(audio, new byte[]{})) {
            sendMessageInReply(chatId, userId, "Sorry, this service is currently unavailable(\nContact the owners", replyToMessageId);
            return;
        }
        SendAudio sendAudio = new SendAudio();

        sendAudio.setChatId(chatId.toString());
        InputStream inputStream = new ByteArrayInputStream(audio);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "audio/YourSpeech");
        sendAudio.setAudio(inputFile);
        sendAudio.setReplyToMessageId(replyToMessageId);

        try {
            log.info("Replying to " + userId + " in chat: " + chatId);
            execute(sendAudio);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

}
