package com.wassimlagnaoui.RestaurantOrder.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaffInfoDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Long employeeId;
}

