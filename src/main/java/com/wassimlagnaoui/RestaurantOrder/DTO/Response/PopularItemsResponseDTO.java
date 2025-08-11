package com.wassimlagnaoui.RestaurantOrder.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PopularItemsResponseDTO {
    // Fields for popular items response
    private String itemName;
    private Long orderCount;
    private Double totalRevenue;

}
