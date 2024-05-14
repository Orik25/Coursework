package com.orik.botapi.constant.speechtotext;

public enum SpeechToTextLanguage {
    UA("uk"),
    EN("en");
    private final String value;

    SpeechToTextLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SpeechToTextLanguage fromValue(String value) {
        for (SpeechToTextLanguage model : SpeechToTextLanguage.values()) {
            if (model.value.equals(value)) {
                return model;
            }
        }
        throw new IllegalArgumentException("Invalid SpeechToTextLanguage value: " + value);
    }

    public static boolean isOneOf(String value) {
        try {
            fromValue(value);
            return true;
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
