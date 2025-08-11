package com.wassimlagnaoui.RestaurantOrder.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TableSessionAverageRevenueByDate {
    private String date;
    private Double averageRevenue;

}
