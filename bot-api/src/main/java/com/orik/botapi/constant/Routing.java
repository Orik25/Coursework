package com.orik.botapi.constant;

public enum Routing {
    T0_TEXT_MODEL("toTextModel"),
    T0_IMAGE_MODEL("toImageModel"),
    T0_TEXT_TO_SPEECH_MODEL("toTextToSpeechModel"),
    T0_SPEECH_TO_TEXT_MODEL("toSpeechToTextModel"),
    TO_MAIN("toMain");

    private final String value;

    Routing(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
