package com.wassimlagnaoui.RestaurantOrder.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MostOrderedItemDTO {
    private String name;
    private Long totalQuantity;
    private Long orderCount;
}

