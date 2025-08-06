package com.wassimlagnaoui.RestaurantOrder.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KitchenOrderQueueResponse {
    private long orderItemId;
    private Long orderId;
    private String tableNumber;
    private String itemName;
    private int quantity;
    private boolean served;
}
