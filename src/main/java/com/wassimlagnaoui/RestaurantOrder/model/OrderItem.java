package com.wassimlagnaoui.RestaurantOrder.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @Column(name = "served")
    private Boolean served=false;


    @ManyToOne
    private MenuItem menuItem;

    @ManyToOne
    private Order order;

}
