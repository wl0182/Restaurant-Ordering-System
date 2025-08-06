package com.wassimlagnaoui.RestaurantOrder.DTO.Response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EndSessionResponse {
    private String message;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String tableNumber;
}
