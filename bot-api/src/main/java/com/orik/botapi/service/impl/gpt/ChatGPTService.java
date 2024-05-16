package com.orik.botapi.service.impl.gpt;

import com.orik.botapi.config.ChatGPTConfig;
import com.orik.botapi.config.settings.ImageModelSettings;
import com.orik.botapi.config.settings.SpeechToTextModelSettings;
import com.orik.botapi.config.settings.TextModelSettings;
import com.orik.botapi.config.settings.TextToSpeechSettings;
import com.theokanning.openai.audio.CreateSpeechRequest;
import com.theokanning.openai.audio.CreateTranscriptionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.image.CreateImageRequest;
import com.theokanning.openai.image.Image;
import com.theokanning.openai.service.OpenAiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ChatGPTService {
    private final ChatGPTConfig config;

    @Autowired
    public ChatGPTService(ChatGPTConfig config) {
        this.config = config;
    }

    public String getTextResponse(List<String> messages) {
        List<ChatMessage> prompt = new ArrayList<>();
        for (String message : messages) {
            prompt.add(new ChatMessage("user", message));
        }
        OpenAiService service = new OpenAiService(config.getToken(), Duration.ofSeconds(30));
        ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                .messages(prompt)
                .model(TextModelSettings.gptModel.getValue())
                .build();

        try {
            return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
        } catch (Exception e) {
            log.error("Timeout exception occurred: " + e.getMessage());
            log.info("Sending the message again!");
            try {
                return service.createChatCompletion(completionRequest).getChoices().get(0).getMessage().getContent();
            }catch (Exception ex){
                log.error("Timeout exception occurred: " + ex.getMessage());
                return "Sorry, the response time has expired!";
            }
        }

    }

    public byte[] getSpeechByText(String prompt) {
        OpenAiService service = new OpenAiService(config.getToken(), Duration.ofSeconds(30));

        CreateSpeechRequest speechRequest = CreateSpeechRequest.builder()
                .model(TextToSpeechSettings.textToSpeechModel.getValue())
                .input(prompt)
                .voice(TextToSpeechSettings.textToSpeechVoiceType.getValue())
                .responseFormat("mp3")
                .speed(TextToSpeechSettings.speechToTextSpeed)
                .build();
        try {
            log.info("Generating speech by text...");
            return service.createSpeech(speechRequest).bytes();
        }
        catch (Exception ex){
            log.error("Error occurred " + ex.getMessage());
        }
        return new byte[]{};
    }

    public String getTextBySpeech(InputStream inputStream) throws IOException {
        OpenAiService service = new OpenAiService(config.getToken(), Duration.ofSeconds(30));

        CreateTranscriptionRequest speechRequest = CreateTranscriptionRequest.builder()
                .language(SpeechToTextModelSettings.speechToTextLanguage.getValue())
                .model("whisper-1")
                .responseFormat("json")
                .build();

        try {
            File tempFile = File.createTempFile("speech", ".mp3");
            tempFile.deleteOnExit();

            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) != -1) {
                    fos.write(buffer, 0, length);
                }
            }
            log.info("Generating text by speech...");
            return service.createTranscription(speechRequest, tempFile).getText();
        } catch (Exception ex) {
            log.error("Error occurred " + ex.getMessage());
        }
        return "Sorry, I can`t generate speech now(\nContact the owners";
    }

    public String getImageByPrompt(String prompt){
        OpenAiService service = new OpenAiService(config.getToken(), Duration.ofSeconds(30));

        CreateImageRequest imageRequest = CreateImageRequest.builder()
                .prompt(prompt)
                .model(ImageModelSettings.imageModel.getValue())
                .n(1)
                .size(ImageModelSettings.imageSize.getValue())
                .style(ImageModelSettings.imageStyle.getValue())
                .responseFormat("b64_json")
                .build();

        try {
            return service.createImage(imageRequest)
                    .getData()
                    .stream()
                    .map(Image::getB64Json)
                    .collect(Collectors.joining());
        } catch (Exception ex) {
            log.error("Error occurred " + ex.getMessage());
        }
        return "Sorry, I can`t generate images now(\nContact the owners";
    }

}
