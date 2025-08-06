package com.wassimlagnaoui.RestaurantOrder.DTO;

import com.wassimlagnaoui.RestaurantOrder.model.MenuItem;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemRequest {

    private int quantity;
    private Long menuItemId;


}
