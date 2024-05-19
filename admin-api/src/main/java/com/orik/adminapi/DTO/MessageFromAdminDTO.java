package com.orik.adminapi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageFromAdminDTO {
    private Long chat_id;
    private String text;

}
