package com.orik.botapi.constant.speechtotext;

import com.orik.botapi.constant.text.GptModel;

public enum SpeechToTextMode {
    ENABLED("enabled"),
    DISABLED("disabled");
    private final String value;

    SpeechToTextMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SpeechToTextMode fromValue(String value) {
        for (SpeechToTextMode model : SpeechToTextMode.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid SpeechToTextMode value: " + value);
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
