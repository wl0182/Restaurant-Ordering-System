package com.wassimlagnaoui.RestaurantOrder.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime sessionStart;

    private LocalDateTime sessionEnd;

    private String tableNumber;

    @OneToMany(mappedBy = "tableSession")
    private List<Order> orders;



}
