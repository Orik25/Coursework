package com.orik.botapi.config.settings;

import com.orik.botapi.constant.texttospeech.TextToSpeechModel;
import com.orik.botapi.constant.texttospeech.TextToSpeechVoiceType;

public class TextToSpeechSettings {
    public static TextToSpeechModel textToSpeechModel = TextToSpeechModel.TTS_1;
    public static TextToSpeechVoiceType textToSpeechVoiceType = TextToSpeechVoiceType.ALLOY;
    public static Double speechToTextSpeed = 1.0;
}
