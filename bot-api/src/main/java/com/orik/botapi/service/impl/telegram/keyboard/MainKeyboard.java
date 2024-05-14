package com.orik.botapi.service.impl.telegram.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.orik.botapi.constant.Routing.*;

@Component
public class MainKeyboard {
    public InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> textModelList = new ArrayList<>();
        InlineKeyboardButton textModel = new InlineKeyboardButton("Text");
        textModel.setCallbackData(T0_TEXT_MODEL.getValue());
        textModelList.add(textModel);
        keyboard.add(textModelList);

        List<InlineKeyboardButton> imageModelList = new ArrayList<>();
        InlineKeyboardButton imageModel = new InlineKeyboardButton("Image");
        imageModel.setCallbackData(T0_IMAGE_MODEL.getValue());
        imageModelList.add(imageModel);
        keyboard.add(imageModelList);

        List<InlineKeyboardButton> textToSpeechModelList = new ArrayList<>();
        InlineKeyboardButton textToSpeech = new InlineKeyboardButton("Text to speech");
        textToSpeech.setCallbackData(T0_TEXT_TO_SPEECH_MODEL.getValue());
        textToSpeechModelList.add(textToSpeech);
        keyboard.add(textToSpeechModelList);

        List<InlineKeyboardButton> speechToTextModelList = new ArrayList<>();
        InlineKeyboardButton speechToText = new InlineKeyboardButton("Speech to text");
        speechToText.setCallbackData(T0_SPEECH_TO_TEXT_MODEL.getValue());
        speechToTextModelList.add(speechToText);
        keyboard.add(speechToTextModelList);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

}
