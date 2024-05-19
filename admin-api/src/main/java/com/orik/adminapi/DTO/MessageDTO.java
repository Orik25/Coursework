package com.orik.adminapi.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class MessageDTO {
    private String message;
    private MultipartFile file;
}
