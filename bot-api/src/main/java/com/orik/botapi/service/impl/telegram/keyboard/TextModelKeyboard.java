package com.orik.botapi.service.impl.telegram.keyboard;

import com.orik.botapi.config.settings.TextModelSettings;
import com.orik.botapi.constant.text.GptModel;
import com.orik.botapi.constant.Routing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

import static com.orik.botapi.constant.text.GptModel.GPT_3;
import static com.orik.botapi.constant.text.GptModel.GPT_4;

@Component
public class TextModelKeyboard {
    //text model version
    private List<InlineKeyboardButton> gptModelButtons;
    private List<InlineKeyboardButton> modelDescriptionButtons;
    private List<InlineKeyboardButton> backButtonList;
    private InlineKeyboardButton gpt3;
    private InlineKeyboardButton gpt4;
    private InlineKeyboardButton back;
    private InlineKeyboardButton description;

    @Autowired
    public TextModelKeyboard(){
        this.gptModelButtons = new ArrayList<>();
        this.backButtonList = new ArrayList<>();
        this.modelDescriptionButtons = new ArrayList<>();

        this.gpt3 = new InlineKeyboardButton("GPT_3");
        this.gpt3.setCallbackData(GPT_3.getValue());
        this.gpt4 = new InlineKeyboardButton("GPT_4");
        this.gpt4.setCallbackData(GPT_4.getValue());

        this.back = new InlineKeyboardButton("◀️Back");
        this.back.setCallbackData(Routing.TO_MAIN.getValue());

        this.description = new InlineKeyboardButton("Chat Model");
        this.description.setCallbackData("invalid");

        this.modelDescriptionButtons.add(description);
        this.gptModelButtons.add(gpt3);
        this.gptModelButtons.add(gpt4);
        this.backButtonList.add(back);


        this.updateInlineKeyboard();
    }

    public InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(modelDescriptionButtons);
        keyboard.add(gptModelButtons);
        keyboard.add(backButtonList);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public void updateInlineKeyboard(){
        for (InlineKeyboardButton button: gptModelButtons) {
            if(button.getCallbackData().equals(TextModelSettings.gptModel.getValue())){
                button.setText(button.getText()+"❌");
            }else{
                button.setText(GptModel.fromValue(button.getCallbackData()).name());
            }
        }
    }
}
