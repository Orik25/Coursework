package com.orik.botapi.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("application.properties")
public class ChatGPTConfig {

    @Value("${ChatGPT.token}")
    private String token;

}
