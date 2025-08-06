package com.wassimlagnaoui.RestaurantOrder.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemSummaryDTO {
    private Long OrderId;
    private Long itemId;
    private String itemName;
    private int totalQuantity;
    private Boolean served;
    private Double totalPrice;
}
