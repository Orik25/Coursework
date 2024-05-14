package com.orik.botapi.service.impl.telegram.keyboard;

import com.orik.botapi.config.settings.SpeechToTextModelSettings;
import com.orik.botapi.constant.Routing;
import com.orik.botapi.constant.speechtotext.SpeechToTextLanguage;
import com.orik.botapi.constant.speechtotext.SpeechToTextMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpeechToTextModelKeyboard {
    private List<InlineKeyboardButton> modeButtons;
    private List<InlineKeyboardButton> modeDescriptionButtons;
    private List<InlineKeyboardButton> languageButtons;
    private List<InlineKeyboardButton>  languageDescriptionButtons;
    private List<InlineKeyboardButton> backButtonList;
    private InlineKeyboardButton disabled;
    private InlineKeyboardButton enabled;
    private InlineKeyboardButton ua;
    private InlineKeyboardButton en;
    private InlineKeyboardButton back;
    private InlineKeyboardButton description;
    private InlineKeyboardButton languageDescription;

    @Autowired
    public SpeechToTextModelKeyboard() {
        this.modeButtons = new ArrayList<>();
        this.backButtonList = new ArrayList<>();
        this.modeDescriptionButtons = new ArrayList<>();
        this.languageDescriptionButtons = new ArrayList<>();
        this.languageButtons = new ArrayList<>();

        this.disabled = new InlineKeyboardButton(SpeechToTextMode.DISABLED.name());
        this.disabled.setCallbackData(SpeechToTextMode.DISABLED.getValue());
        this.enabled = new InlineKeyboardButton(SpeechToTextMode.DISABLED.name());
        this.enabled.setCallbackData(SpeechToTextMode.ENABLED.getValue());

        this.ua = new InlineKeyboardButton(SpeechToTextLanguage.UA.name());
        this.ua.setCallbackData(SpeechToTextLanguage.UA.getValue());
        this.en = new InlineKeyboardButton(SpeechToTextLanguage.EN.name());
        this.en.setCallbackData(SpeechToTextLanguage.EN.getValue());

        this.back = new InlineKeyboardButton("◀️Back");
        this.back.setCallbackData(Routing.TO_MAIN.getValue());

        this.description = new InlineKeyboardButton("Voice to text mode");
        this.description.setCallbackData("invalid");

        this.languageDescription = new InlineKeyboardButton("Language");
        this.languageDescription.setCallbackData("invalid");

        this.languageDescriptionButtons.add(languageDescription);
        this.modeDescriptionButtons.add(description);
        this.modeButtons.add(disabled);
        this.modeButtons.add(enabled);
        this.backButtonList.add(back);
        this.languageButtons.add(ua);
        this.languageButtons.add(en);

        this.updateInlineKeyboard();
    }

    public InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(modeDescriptionButtons);
        keyboard.add(modeButtons);
        if(SpeechToTextModelSettings.speechToTextMode.equals(SpeechToTextMode.ENABLED)){
            keyboard.add(languageDescriptionButtons);
            keyboard.add(languageButtons);
        }

        keyboard.add(backButtonList);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public void updateInlineKeyboard() {
        for (InlineKeyboardButton button : modeButtons) {
            if (button.getCallbackData().equals(SpeechToTextModelSettings.speechToTextMode.getValue())) {
                if(!button.getText().contains("❌")){
                    button.setText(button.getText() + "❌");
                }
            } else {
                button.setText(SpeechToTextMode.fromValue(button.getCallbackData()).name());
            }
        }
        for (InlineKeyboardButton button : languageButtons) {
            if (button.getCallbackData().equals(SpeechToTextModelSettings.speechToTextLanguage.getValue())) {
                if(!button.getText().contains("❌")){
                    button.setText(button.getText() + "❌");
                }
            } else {
                button.setText(SpeechToTextLanguage.fromValue(button.getCallbackData()).name());
            }
        }

    }
}