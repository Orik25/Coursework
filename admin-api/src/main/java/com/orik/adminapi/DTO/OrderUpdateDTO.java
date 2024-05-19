package com.orik.adminapi.DTO;

import com.orik.adminapi.constant.order.StatusType;
import com.orik.adminapi.constant.order.ServerType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderUpdateDTO {
    private Long id;
    private ServerType serverType;
    private StatusType statusType;
}
