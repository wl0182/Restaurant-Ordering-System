package com.wassimlagnaoui.RestaurantOrder.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceOrderResponse {
    private Long orderId;
    private Long sessionId;
    private String status;
    private LocalDateTime createdAt;
    private List<OrderItemResponse> items;
}

