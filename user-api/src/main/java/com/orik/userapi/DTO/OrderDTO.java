package com.orik.userapi.DTO;

import com.orik.userapi.constant.ServerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDTO {
    @NotEmpty(message = "First Name is required")
    private String firstName;
    @NotEmpty(message = "Last Name is required")
    private String lastName;
    @NotEmpty(message = "Phone Number is required")
    private String phoneNumber;
    @Email(message = "Not valid email")
    private String email;
    private ServerType serverType;
    private String message;
}
