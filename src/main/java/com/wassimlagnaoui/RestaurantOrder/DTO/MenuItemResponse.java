package com.wassimlagnaoui.RestaurantOrder.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemResponse {

    private Long id;
    private String name;
    private Double price;
    private String imageUrl;
    private String category;
    private boolean available;



}
