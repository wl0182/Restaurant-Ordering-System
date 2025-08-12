package com.wassimlagnaoui.RestaurantOrder.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class staffAddedResponse {
    private String message;
    private Long staffId;
}
