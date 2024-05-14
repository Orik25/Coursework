package com.orik.botapi.constant.texttospeech;

import com.orik.botapi.constant.text.GptModel;

public enum TextToSpeechModel {
    TTS_1("tts-1"),
    TTS_1_HD("tts-1-hd");
    private final String value;

    TextToSpeechModel(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TextToSpeechModel fromValue(String value) {
        for (TextToSpeechModel model : TextToSpeechModel.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid TextToSpeechModel value: " + value);
    }

    public static boolean isOneOf(String value){
        try{
            fromValue(value);
            return true;
        }catch (IllegalArgumentException ex){
            return false;
        }
    }
}