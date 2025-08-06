package com.wassimlagnaoui.RestaurantOrder.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemRequest {
    @NotNull
    private String name;

    private String description;
    @NotNull
    private Double price;
    @NotNull
    private String imageUrl;
    @NotNull
    private String category;
    @NotNull
    private boolean available;

}
