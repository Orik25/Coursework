package com.orik.userapi.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RequestConsultationDTO {
    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;
    private String message;
}
