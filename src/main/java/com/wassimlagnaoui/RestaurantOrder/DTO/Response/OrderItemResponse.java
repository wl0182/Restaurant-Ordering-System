package com.wassimlagnaoui.RestaurantOrder.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponse {
    private Long itemId;
    private Integer quantity;
    private Long menuItemId;
    private String name;
    private double totalPrice;
    private double unitPrice;
    private Boolean served;
}

/*
{
      "itemId": 501,
      "menuItemId": 101,
      "name": "Chicken Sandwich",
      "quantity": 2,
      "served": false
    },
 */