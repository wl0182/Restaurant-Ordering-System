package com.wassimlagnaoui.RestaurantOrder.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Staff {
    @Id
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private Long employeeId;


}
