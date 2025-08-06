package com.wassimlagnaoui.RestaurantOrder.DTO;

import com.wassimlagnaoui.RestaurantOrder.model.OrderItem;
import com.wassimlagnaoui.RestaurantOrder.model.TableSession;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    private Double total;

    private LocalDateTime orderDate;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private String status;

    List<OrderItemRequest> orderItems;

    TableSession tableSession;
}
