package com.orik.botapi.service.impl.telegram.keyboard;

import com.orik.botapi.config.settings.TextToSpeechSettings;
import com.orik.botapi.constant.Routing;
import com.orik.botapi.constant.texttospeech.TextToSpeechModel;
import com.orik.botapi.constant.texttospeech.TextToSpeechVoiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class TextToSpeechModelKeyboard {
    private List<InlineKeyboardButton> modelButtons;
    private List<InlineKeyboardButton> modelDescriptionButtons;
    private List<InlineKeyboardButton> voiceTypeButtons1;
    private List<InlineKeyboardButton> voiceTypeButtons2;
    private List<InlineKeyboardButton> voiceTypeDescriptionButtons;
    private List<InlineKeyboardButton> speedButtons;
    private List<InlineKeyboardButton>  speedDescriptionButtons;
    private List<InlineKeyboardButton> backButtonList;
    private InlineKeyboardButton tts1;
    private InlineKeyboardButton tts1Hd;
    private InlineKeyboardButton alloy;
    private InlineKeyboardButton echo;
    private InlineKeyboardButton fable;
    private InlineKeyboardButton onyx;
    private InlineKeyboardButton nova;
    private InlineKeyboardButton shimmer;
    private InlineKeyboardButton back;
    private InlineKeyboardButton description;
    private InlineKeyboardButton voiceTypeDescription;
    private InlineKeyboardButton currentSpeedDescription;
    private InlineKeyboardButton currentSpeed;
    private InlineKeyboardButton plus;
    private InlineKeyboardButton minus;

    @Autowired
    public TextToSpeechModelKeyboard(){
        this.modelButtons = new ArrayList<>();
        this.backButtonList = new ArrayList<>();
        this.modelDescriptionButtons = new ArrayList<>();
        this.voiceTypeButtons1 = new ArrayList<>();
        this.voiceTypeButtons2 = new ArrayList<>();
        this.voiceTypeDescriptionButtons = new ArrayList<>();
        this.speedDescriptionButtons = new ArrayList<>();
        this.speedButtons = new ArrayList<>();

        this.tts1 = new InlineKeyboardButton(TextToSpeechModel.TTS_1.name());
        this.tts1.setCallbackData(TextToSpeechModel.TTS_1.getValue());
        this.tts1Hd = new InlineKeyboardButton(TextToSpeechModel.TTS_1_HD.name());
        this.tts1Hd.setCallbackData(TextToSpeechModel.TTS_1_HD.getValue());

        this.alloy = new InlineKeyboardButton(TextToSpeechVoiceType.ALLOY.name());
        this.alloy.setCallbackData(TextToSpeechVoiceType.ALLOY.getValue());
        this.echo = new InlineKeyboardButton(TextToSpeechVoiceType.ECHO.name());
        this.echo.setCallbackData(TextToSpeechVoiceType.ECHO.getValue());
        this.fable = new InlineKeyboardButton(TextToSpeechVoiceType.FABLE.name());
        this.fable.setCallbackData(TextToSpeechVoiceType.FABLE.getValue());
        this.onyx = new InlineKeyboardButton(TextToSpeechVoiceType.ONYX.name());
        this.onyx.setCallbackData(TextToSpeechVoiceType.ONYX.getValue());
        this.nova = new InlineKeyboardButton(TextToSpeechVoiceType.NOVA.name());
        this.nova.setCallbackData(TextToSpeechVoiceType.NOVA.getValue());
        this.shimmer = new InlineKeyboardButton(TextToSpeechVoiceType.SHIMMER.name());
        this.shimmer.setCallbackData(TextToSpeechVoiceType.SHIMMER.getValue());

        this.plus = new InlineKeyboardButton("➕");
        this.plus.setCallbackData("+");
        this.currentSpeed = new InlineKeyboardButton(TextToSpeechSettings.speechToTextSpeed.toString());
        this.currentSpeed.setCallbackData("invalid");
        this.minus = new InlineKeyboardButton("➖");
        this.minus.setCallbackData("-");

        this.speedButtons.add(plus);
        this.speedButtons.add(currentSpeed);
        this.speedButtons.add(minus);

        this.currentSpeedDescription = new InlineKeyboardButton("Speed of speech");
        this.currentSpeedDescription.setCallbackData("invalid");
        this.speedDescriptionButtons.add(currentSpeedDescription);

        this.back = new InlineKeyboardButton("◀️Back");
        this.back.setCallbackData(Routing.TO_MAIN.getValue());

        this.description = new InlineKeyboardButton("Audio Model");
        this.description.setCallbackData("invalid");

        this.voiceTypeDescription = new InlineKeyboardButton("Select Voice");
        this.voiceTypeDescription.setCallbackData("invalid");

        this.voiceTypeButtons1.add(alloy);
        this.voiceTypeButtons1.add(echo);
        this.voiceTypeButtons1.add(fable);

        this.voiceTypeButtons2.add(onyx);
        this.voiceTypeButtons2.add(nova);
        this.voiceTypeButtons2.add(shimmer);

        this.modelDescriptionButtons.add(description);
        this.modelButtons.add(tts1);
        this.modelButtons.add(tts1Hd);
        this.voiceTypeDescriptionButtons.add(voiceTypeDescription);
        this.backButtonList.add(back);


        this.updateInlineKeyboard();
    }

    public InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(modelDescriptionButtons);
        keyboard.add(modelButtons);
        keyboard.add(voiceTypeDescriptionButtons);
        keyboard.add(voiceTypeButtons1);
        keyboard.add(voiceTypeButtons2);
        keyboard.add(speedDescriptionButtons);
        keyboard.add(speedButtons);
        keyboard.add(backButtonList);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public void updateInlineKeyboard(){
        for (InlineKeyboardButton button: modelButtons) {
            if(button.getCallbackData().equals(TextToSpeechSettings.textToSpeechModel.getValue())){
                if(!button.getText().contains("❌")){
                    button.setText(button.getText() + "❌");
                }
            }else{
                button.setText(TextToSpeechModel.fromValue(button.getCallbackData()).name());
            }
        }
        for (InlineKeyboardButton button: voiceTypeButtons1) {
            if(button.getCallbackData().equals(TextToSpeechSettings.textToSpeechVoiceType.getValue())){
                if(!button.getText().contains("❌")){
                    button.setText(button.getText() + "❌");
                }
            }else{
                button.setText(TextToSpeechVoiceType.fromValue(button.getCallbackData()).name());
            }
        }
        for (InlineKeyboardButton button: voiceTypeButtons2) {
            if(button.getCallbackData().equals(TextToSpeechSettings.textToSpeechVoiceType.getValue())){
                if(!button.getText().contains("❌")){
                    button.setText(button.getText() + "❌");
                }
            }else{
                button.setText(TextToSpeechVoiceType.fromValue(button.getCallbackData()).name());
            }
        }
        currentSpeed.setText(TextToSpeechSettings.speechToTextSpeed.toString());
    }
}
