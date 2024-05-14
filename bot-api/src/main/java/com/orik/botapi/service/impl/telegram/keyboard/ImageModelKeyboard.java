package com.orik.botapi.service.impl.telegram.keyboard;

import com.orik.botapi.config.settings.ImageModelSettings;
import com.orik.botapi.constant.Routing;
import com.orik.botapi.constant.image.ImageModel;
import com.orik.botapi.constant.image.ImageSize;
import com.orik.botapi.constant.image.ImageStyle;
import com.orik.botapi.constant.text.GptModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class ImageModelKeyboard {
    //text model version
    private List<InlineKeyboardButton> imageModelButtons;
    private List<InlineKeyboardButton> modelDescriptionButtons;
    private List<InlineKeyboardButton> imageSizeButtonsForDalle2;
    private List<InlineKeyboardButton> imageSizeButtonsForDalle3;
    private List<InlineKeyboardButton> imageSizeDescriptionButtons;

    private List<InlineKeyboardButton> imageStyleButtons;
    private List<InlineKeyboardButton> imageStyleDescriptionButtons;
    private List<InlineKeyboardButton> backButtonList;
    private InlineKeyboardButton dalle2;
    private InlineKeyboardButton dalle3;
    private InlineKeyboardButton back;
    private InlineKeyboardButton modelDescription;
    private InlineKeyboardButton imageSizeDescription;
    private InlineKeyboardButton _256x256;
    private InlineKeyboardButton _512x512;
    private InlineKeyboardButton _1024x1024;
    private InlineKeyboardButton _1792x1024;
    private InlineKeyboardButton _1024x1792;
    private InlineKeyboardButton imageStyleDescription;
    private InlineKeyboardButton vivid;
    private InlineKeyboardButton natural;

    @Autowired
    public ImageModelKeyboard() {
        this.imageModelButtons = new ArrayList<>();
        this.backButtonList = new ArrayList<>();
        this.modelDescriptionButtons = new ArrayList<>();
        this.imageSizeDescriptionButtons = new ArrayList<>();
        this.imageSizeButtonsForDalle2 = new ArrayList<>();
        this.imageSizeButtonsForDalle3 = new ArrayList<>();
        this.imageStyleDescriptionButtons = new ArrayList<>();
        this.imageStyleButtons = new ArrayList<>();

        this.dalle2 = new InlineKeyboardButton(ImageModel.DALL_E_2.name());
        this.dalle2.setCallbackData(ImageModel.DALL_E_2.getValue());
        this.dalle3 = new InlineKeyboardButton(ImageModel.DALL_E_3.name());
        this.dalle3.setCallbackData(ImageModel.DALL_E_3.getValue());

        this._256x256 = new InlineKeyboardButton(ImageSize._256x256.getValue());
        this._256x256.setCallbackData(ImageSize._256x256.getValue());
        this._512x512 = new InlineKeyboardButton(ImageSize._512x512.getValue());
        this._512x512.setCallbackData(ImageSize._512x512.getValue());
        this._1024x1024 = new InlineKeyboardButton(ImageSize._1024x1024.getValue());
        this._1024x1024.setCallbackData(ImageSize._1024x1024.getValue());
        this._1792x1024 = new InlineKeyboardButton(ImageSize._1792x1024.getValue());
        this._1792x1024.setCallbackData(ImageSize._1792x1024.getValue());
        this._1024x1792 = new InlineKeyboardButton(ImageSize._1024x1792.getValue());
        this._1024x1792.setCallbackData(ImageSize._1024x1792.getValue());

        this.vivid = new InlineKeyboardButton(ImageStyle.VIVID.name());
        this.vivid.setCallbackData(ImageStyle.VIVID.getValue());
        this.natural = new InlineKeyboardButton(ImageStyle.NATURAL.name());
        this.natural.setCallbackData(ImageStyle.NATURAL.getValue());

        this.imageSizeButtonsForDalle2.add(_256x256);
        this.imageSizeButtonsForDalle2.add(_512x512);
        this.imageSizeButtonsForDalle2.add(_1024x1024);

        this.imageSizeButtonsForDalle3.add(_1024x1024);
        this.imageSizeButtonsForDalle3.add(_1792x1024);
        this.imageSizeButtonsForDalle3.add(_1024x1792);

        this.back = new InlineKeyboardButton("◀️Back");
        this.back.setCallbackData(Routing.TO_MAIN.getValue());

        this.modelDescription = new InlineKeyboardButton("Image Model");
        this.modelDescription.setCallbackData("invalid");

        this.imageSizeDescription = new InlineKeyboardButton("Image Size");
        this.imageSizeDescription.setCallbackData("invalid");

        this.imageStyleDescription = new InlineKeyboardButton("Image Style");
        this.imageStyleDescription.setCallbackData("invalid");


        this.modelDescriptionButtons.add(modelDescription);
        this.imageModelButtons.add(dalle2);
        this.imageModelButtons.add(dalle3);
        this.imageSizeDescriptionButtons.add(imageSizeDescription);
        this.backButtonList.add(back);
        this.imageStyleDescriptionButtons.add(imageStyleDescription);

        this.imageStyleButtons.add(vivid);
        this.imageStyleButtons.add(natural);


        this.updateInlineKeyboard();
    }

    public InlineKeyboardMarkup getInlineKeyboard() {
        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        keyboard.add(modelDescriptionButtons);
        keyboard.add(imageModelButtons);
        keyboard.add(imageSizeDescriptionButtons);
        if (ImageModelSettings.imageModel.equals(ImageModel.DALL_E_2)) {
            keyboard.add(imageSizeButtonsForDalle2);
        } else if (ImageModelSettings.imageModel.equals(ImageModel.DALL_E_3)) {
            keyboard.add(imageSizeButtonsForDalle3);
            keyboard.add(imageStyleDescriptionButtons);
            keyboard.add(imageStyleButtons);
        }
        keyboard.add(backButtonList);

        keyboardMarkup.setKeyboard(keyboard);
        return keyboardMarkup;
    }

    public void updateInlineKeyboard() {
        for (InlineKeyboardButton button : imageModelButtons) {
            if (button.getCallbackData().equals(ImageModelSettings.imageModel.getValue())) {
                if(!button.getText().contains("❌")){
                    button.setText(button.getText() + "❌");
                }
            } else {
                button.setText(ImageModel.fromValue(button.getCallbackData()).name());
            }
        }
        if (ImageModelSettings.imageModel.equals(ImageModel.DALL_E_2)) {
            for (InlineKeyboardButton button : imageSizeButtonsForDalle2) {
                if (button.getCallbackData().equals(ImageModelSettings.imageSize.getValue())) {
                    if(!button.getText().contains("❌")){
                        button.setText(button.getText() + "❌");
                    }
                } else {
                    button.setText(ImageSize.fromValue(button.getCallbackData()).getValue());
                }
            }
        } else if (ImageModelSettings.imageModel.equals(ImageModel.DALL_E_3)) {
            for (InlineKeyboardButton button : imageSizeButtonsForDalle3) {
                if (button.getCallbackData().equals(ImageModelSettings.imageSize.getValue())) {
                    if(!button.getText().contains("❌")){
                        button.setText(button.getText() + "❌");
                    }
                } else {
                    button.setText(ImageSize.fromValue(button.getCallbackData()).getValue());
                }
            }
            for (InlineKeyboardButton button : imageStyleButtons) {
                if (button.getCallbackData().equals(ImageModelSettings.imageStyle.getValue())) {
                    if(!button.getText().contains("❌")){
                        button.setText(button.getText() + "❌");
                    }
                } else {
                    button.setText(ImageStyle.fromValue(button.getCallbackData()).name());
                }
            }
        }
    }
}
