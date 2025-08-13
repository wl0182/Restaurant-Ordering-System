package com.wassimlagnaoui.RestaurantOrder.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class
TableSessionResponse {
    private Long id;
    private String tableNumber;
    private LocalDateTime sessionStartTime;
    private LocalDateTime sessionEndTime;

}
