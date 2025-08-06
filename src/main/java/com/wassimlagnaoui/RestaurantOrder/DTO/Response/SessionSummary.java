package com.wassimlagnaoui.RestaurantOrder.DTO.Response;

import com.wassimlagnaoui.RestaurantOrder.DTO.ItemSummaryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionSummary {
    private Long sessionId;
    private String tableNumber;
    private Long totalOrders;
    private Long totalItemOrdered;
    private Double totalAmont;
    private List<ItemSummaryDTO> items;

}
/*
{
  "sessionId": 5,
  "tableNumber": "T3",
  "totalOrders": 3,
  "totalItemsOrdered": 11,
  "itemsServed": 9,
  "itemsUnserved": 2,
  "totalAmount": 123.75
}
 */