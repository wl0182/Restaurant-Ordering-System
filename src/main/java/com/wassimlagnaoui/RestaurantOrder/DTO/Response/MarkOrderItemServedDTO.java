package com.wassimlagnaoui.RestaurantOrder.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarkOrderItemServedDTO {
    private String message;
    private long orderItemID;
}
