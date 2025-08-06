package com.wassimlagnaoui.RestaurantOrder.DTO.Requests;

import com.wassimlagnaoui.RestaurantOrder.DTO.OrderItemRequest;
import lombok.Data;

import java.util.List;

@Data
public class PlaceOrderRequest {
    private Long tableSessionId;
    private List<OrderItemRequest> items;
}
