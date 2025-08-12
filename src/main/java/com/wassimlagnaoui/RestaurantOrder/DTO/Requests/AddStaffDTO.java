package com.wassimlagnaoui.RestaurantOrder.DTO.Requests;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddStaffDTO {
    private String firstName;
    private String lastName;
    private String email;
    private Long employeeId; // Unique identifier for the staff member
    private String role; // Role of the staff member (e.g., waiter, chef,
}
