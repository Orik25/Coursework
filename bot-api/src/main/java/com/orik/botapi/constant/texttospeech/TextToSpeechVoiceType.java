package com.orik.botapi.constant.texttospeech;

import com.orik.botapi.constant.text.GptModel;

public enum TextToSpeechVoiceType {
    ALLOY("alloy"),
    ECHO("echo"),
    FABLE("fable"),
    ONYX("onyx"),
    NOVA("nova"),
    SHIMMER("shimmer");
    private final String value;

    TextToSpeechVoiceType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static TextToSpeechVoiceType fromValue(String value) {
        for (TextToSpeechVoiceType model : TextToSpeechVoiceType.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid TextToSpeechVoiceType value: " + value);
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
