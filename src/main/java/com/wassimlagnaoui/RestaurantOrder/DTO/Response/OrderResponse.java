package com.wassimlagnaoui.RestaurantOrder.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private Long orderId;
    private Long sessionId;
    private String status;
    private List<OrderItemResponse> orderItems;




}
