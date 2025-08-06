package com.wassimlagnaoui.RestaurantOrder.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderServedStatusDTO {
    private Long orderId;
    private boolean allItemsServed;
    private boolean someServed;
    private boolean noneServed;
}
