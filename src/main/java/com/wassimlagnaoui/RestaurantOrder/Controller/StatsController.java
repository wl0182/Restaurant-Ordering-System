package com.wassimlagnaoui.RestaurantOrder.Controller;


import com.wassimlagnaoui.RestaurantOrder.DTO.MostOrderedItemDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.PopularItemsResponseDTO;
import com.wassimlagnaoui.RestaurantOrder.Service.MenuItemService;
import com.wassimlagnaoui.RestaurantOrder.Service.OrderService;
import com.wassimlagnaoui.RestaurantOrder.Service.StatsService;
import com.wassimlagnaoui.RestaurantOrder.Service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;

@RequestMapping("/api/stats")
public class StatsController {
    private final OrderService orderService;
    private final TableService tableService;
    private final MenuItemService menuItemService;
    private final StatsService statsService;

    public StatsController(OrderService orderService, TableService tableService, MenuItemService menuItemService, StatsService statsService) {
        this.orderService = orderService;
        this.tableService = tableService;
        this.menuItemService = menuItemService;
        this.statsService = statsService;
    }

    // Endpoint to get the most ordered items
    @RequestMapping("/most-ordered-items")
    public ResponseEntity<List<MostOrderedItemDTO>> getMostOrderedItems() {
        List<MostOrderedItemDTO> mostOrderedItems = statsService.getMostOrderedItems();
        return ResponseEntity.ok(mostOrderedItems);
    }

    // Endpoint to get the total revenue
    @RequestMapping("/total-revenue")
    public ResponseEntity<HashMap<String, Double>> getTotalRevenue() {
        HashMap<String, Double> totalRevenue = statsService.gdetTotalRevenueByDate();
        return ResponseEntity.ok(totalRevenue);
    }







}
