package com.orik.botapi.service.impl.telegram;


import com.orik.botapi.config.BotConfig;
import com.orik.botapi.config.settings.ImageModelSettings;
import com.orik.botapi.config.settings.SpeechToTextModelSettings;
import com.orik.botapi.config.settings.TextModelSettings;
import com.orik.botapi.config.settings.TextToSpeechSettings;
import com.orik.botapi.constant.image.ImageModel;
import com.orik.botapi.constant.image.ImageSize;
import com.orik.botapi.constant.image.ImageStyle;
import com.orik.botapi.constant.speechtotext.SpeechToTextLanguage;
import com.orik.botapi.constant.speechtotext.SpeechToTextMode;
import com.orik.botapi.constant.text.GptModel;
import com.orik.botapi.constant.texttospeech.TextToSpeechModel;
import com.orik.botapi.constant.texttospeech.TextToSpeechVoiceType;
import com.orik.botapi.data.DAO.ChatRepository;
import com.orik.botapi.data.entity.Chat;
import com.orik.botapi.service.impl.gpt.ChatGPTService;
import com.orik.botapi.service.impl.telegram.keyboard.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.orik.botapi.constant.Routing.*;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final ChatGPTService chatServive;
    private final ChatRepository chatRepository;
    private final MainKeyboard mainKeyboard;
    private final TextModelKeyboard textModelKeyboard;
    private final ImageModelKeyboard imageModelKeyboard;
    private final TextToSpeechModelKeyboard textToSpeechModelKeyboard;
    private final SpeechToTextModelKeyboard speechToTextModelKeyboard;
    private static final String HELP_TEXT = "This bot is created to communicate with Chat GPT from Telegram\n\n" +
            "You can execute commands from the main menu on the left or by typing a command:\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /start to see a welcome message\n\n" +
            "Type /text {prompt} to get text response from ChatGPT\n\n" +
            "Type /speech {prompt} to get speech from your prompt\n\n" +
            "Type /settings to set detailed configuration of models\n\n" +
            "Type /help to see this message again";

    @Autowired
    public TelegramBot(BotConfig botConfig,
                       ChatGPTService chatServive,
                       MainKeyboard mainKeyboard,
                       ChatRepository chatRepository,
                       TextModelKeyboard textModelKeyboard,
                       ImageModelKeyboard imageModelKeyboard,
                       SpeechToTextModelKeyboard speechToTextModelKeyboard,
                       TextToSpeechModelKeyboard textToSpeechModelKeyboard) {
        this.botConfig = botConfig;
        this.chatServive = chatServive;
        this.mainKeyboard = mainKeyboard;
        this.chatRepository = chatRepository;
        this.textModelKeyboard = textModelKeyboard;
        this.imageModelKeyboard = imageModelKeyboard;
        this.textToSpeechModelKeyboard = textToSpeechModelKeyboard;
        this.speechToTextModelKeyboard = speechToTextModelKeyboard;
        List<BotCommand> commands = new ArrayList<>();
        commands.add(new BotCommand("/start", "get greeting message"));
        commands.add(new BotCommand("/help", "get info how to use"));
        commands.add(new BotCommand("/text", "get text response from GPT"));
        commands.add(new BotCommand("/speech", "get speech by your text"));
        commands.add(new BotCommand("/image", "to generate image by your text"));
        commands.add(new BotCommand("/settings", "detailed configuration of models"));
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
                                case "/settings" -> sendSettingsMenu(chatId);
                                case "/text", "/speech", "/image" ->
                                        sendMessageInReply(chatId, userId, "Please, enter your prompt after /\"command\"", messageId);
                                default -> sendMessageInReply(chatId, userId, "Sorry, I can`t answer(", messageId);

                            }
                        } else {
                            String messageText = splitText[1].trim();
                            switch (command) {
                                case "/start" -> greeting(chatId, userId, firstName, messageId);
                                case "/help" -> sendMessageInReply(chatId, userId, HELP_TEXT, messageId);
                                case "/settings" -> sendSettingsMenu(chatId);
                                case "/text" ->
                                        sendMessageInReply(chatId, userId, chatServive.getTextResponse(List.of(messageText)), messageId);
                                case "/speech" ->
                                        sendAudioReply(chatId, userId, chatServive.getSpeechByText(messageText), messageId);
                                case "/image" ->
                                        sendPhotoReply(chatId, userId, chatServive.getImageByPrompt(messageText), messageId);
                                default -> sendMessageInReply(chatId, userId, "Sorry, I can`t answer(", messageId);
                            }
                        }
                    }

                }
            } else if (SpeechToTextModelSettings.speechToTextMode.equals(SpeechToTextMode.ENABLED) && (update.getMessage().hasVoice() || update.getMessage().hasAudio())) {
                Message message = update.getMessage();
                Long chatId = message.getChatId();
                Integer messageId = message.getMessageId();
                Long userId = message.getFrom().getId();

                if (message.hasVoice()) {
                    Voice voice = message.getVoice();
                    handleVoice(chatId, messageId, userId, voice);
                } else if (message.hasAudio()) {
                    Audio audio = message.getAudio();
                    handleAudio(chatId, messageId, userId, audio);
                }
            }

        } else if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            String data = callbackQuery.getData();
            Long chatId = callbackQuery.getMessage().getChatId();
            Integer messageId = callbackQuery.getMessage().getMessageId();

            if (data.equals(T0_TEXT_MODEL.getValue())) {
                editMessageKeyboard(chatId, messageId, textModelKeyboard.getInlineKeyboard());
            } else if (data.equals(T0_IMAGE_MODEL.getValue())) {
                editMessageKeyboard(chatId, messageId, imageModelKeyboard.getInlineKeyboard());
            } else if (data.equals(T0_TEXT_TO_SPEECH_MODEL.getValue())) {
                editMessageKeyboard(chatId, messageId, textToSpeechModelKeyboard.getInlineKeyboard());
            } else if (data.equals(T0_SPEECH_TO_TEXT_MODEL.getValue())) {
                editMessageKeyboard(chatId, messageId, speechToTextModelKeyboard.getInlineKeyboard());
            } else if (data.equals(TO_MAIN.getValue())) {
                editMessageKeyboard(chatId, messageId, mainKeyboard.getInlineKeyboard());
            } else if (GptModel.isOneOf(data)) {
                TextModelSettings.gptModel = GptModel.fromValue(data);
                textModelKeyboard.updateInlineKeyboard();
                editMessageKeyboard(chatId, messageId, textModelKeyboard.getInlineKeyboard());
            } else if (ImageModel.isOneOf(data)) {
                ImageModelSettings.imageModel = ImageModel.fromValue(data);
                imageModelKeyboard.updateInlineKeyboard();
                editMessageKeyboard(chatId, messageId, imageModelKeyboard.getInlineKeyboard());
            } else if (ImageSize.isOneOf(data)) {
                ImageModelSettings.imageSize = ImageSize.fromValue(data);
                imageModelKeyboard.updateInlineKeyboard();
                editMessageKeyboard(chatId, messageId, imageModelKeyboard.getInlineKeyboard());
            } else if (ImageStyle.isOneOf(data)) {
                ImageModelSettings.imageStyle = ImageStyle.fromValue(data);
                imageModelKeyboard.updateInlineKeyboard();
                editMessageKeyboard(chatId, messageId, imageModelKeyboard.getInlineKeyboard());
            } else if (TextToSpeechModel.isOneOf(data)) {
                TextToSpeechSettings.textToSpeechModel = TextToSpeechModel.fromValue(data);
                textToSpeechModelKeyboard.updateInlineKeyboard();
                editMessageKeyboard(chatId, messageId, textToSpeechModelKeyboard.getInlineKeyboard());
            } else if (TextToSpeechVoiceType.isOneOf(data)) {
                TextToSpeechSettings.textToSpeechVoiceType = TextToSpeechVoiceType.fromValue(data);
                textToSpeechModelKeyboard.updateInlineKeyboard();
                editMessageKeyboard(chatId, messageId, textToSpeechModelKeyboard.getInlineKeyboard());
            } else if (SpeechToTextMode.isOneOf(data)) {
                SpeechToTextModelSettings.speechToTextMode = SpeechToTextMode.fromValue(data);
                speechToTextModelKeyboard.updateInlineKeyboard();
                editMessageKeyboard(chatId, messageId, speechToTextModelKeyboard.getInlineKeyboard());
            } else if (SpeechToTextLanguage.isOneOf(data)) {
                SpeechToTextModelSettings.speechToTextLanguage = SpeechToTextLanguage.fromValue(data);
                speechToTextModelKeyboard.updateInlineKeyboard();
                editMessageKeyboard(chatId, messageId, speechToTextModelKeyboard.getInlineKeyboard());
            } else if (data.equals("+")) {
                if (TextToSpeechSettings.speechToTextSpeed <= 3.75) {
                    TextToSpeechSettings.speechToTextSpeed += 0.25;
                    textToSpeechModelKeyboard.updateInlineKeyboard();
                    editMessageKeyboard(chatId, messageId, textToSpeechModelKeyboard.getInlineKeyboard());
                }

            } else if (data.equals("-")) {
                if (TextToSpeechSettings.speechToTextSpeed >= 0.50) {
                    TextToSpeechSettings.speechToTextSpeed -= 0.25;
                    textToSpeechModelKeyboard.updateInlineKeyboard();
                    editMessageKeyboard(chatId, messageId, textToSpeechModelKeyboard.getInlineKeyboard());
                }

            } else {
                System.out.println(data);
            }

        }
    }

    private void handleVoice(Long chatId, Integer messageId, Long userId, Voice voice) {
        String fileId = voice.getFileId();
        processAudioFile(chatId, messageId, userId, fileId);
    }

    private void handleAudio(Long chatId, Integer messageId, Long userId, Audio audio) {
        String fileId = audio.getFileId();
        processAudioFile(chatId, messageId, userId, fileId);
    }

    private void processAudioFile(Long chatId, Integer messageId, Long userId, String fileId) {
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

    private void greeting(Long chatId, Long userId, String name, Integer replyToMessageId) {
        chatRepository.save(new Chat(chatId));
        sendMessageInReply(chatId, userId, "Hi, " + name + ", nice to meet you", replyToMessageId);
    }

    private void sendSettingsMenu(Long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Here is");
        sendMessage.setReplyMarkup(mainKeyboard.getInlineKeyboard());
        try {
            log.info("Sending keyboard to in chat: " + chatId);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            log.info("Replying in chat: " + chatId);
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

    private void sendPhotoReply(Long chatId, Long userId, String imageBase64, Integer replyToMessageId) {
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId.toString());

        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

        InputStream inputStream = new ByteArrayInputStream(imageBytes);
        InputFile inputFile = new InputFile();
        inputFile.setMedia(inputStream, "photo.png");

        sendPhoto.setPhoto(inputFile);
        sendPhoto.setReplyToMessageId(replyToMessageId);

        try {
            log.info("Replying to " + userId + " in chat: " + chatId);
            execute(sendPhoto);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void editMessageKeyboard(Long chatId, Integer messageId, InlineKeyboardMarkup keyboard) {
        EditMessageReplyMarkup editMessage = new EditMessageReplyMarkup();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setMessageId(messageId);
        editMessage.setReplyMarkup(keyboard);

        try {
            log.info("Changing keyboard to in chat: " + chatId);
            execute(editMessage);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
}