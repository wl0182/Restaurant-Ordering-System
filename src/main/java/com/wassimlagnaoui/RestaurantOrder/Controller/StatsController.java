package com.wassimlagnaoui.RestaurantOrder.Controller;


import com.wassimlagnaoui.RestaurantOrder.DTO.MostOrderedItemDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.PopularItemsResponseDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.TableSessionAverageRevenueByDate;
import com.wassimlagnaoui.RestaurantOrder.Service.MenuItemService;
import com.wassimlagnaoui.RestaurantOrder.Service.OrderService;
import com.wassimlagnaoui.RestaurantOrder.Service.StatsService;
import com.wassimlagnaoui.RestaurantOrder.Service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
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
    public ResponseEntity<HashMap<String, Double>> getTotalRevenueByDate() {
        HashMap<String, Double> totalRevenue = statsService.getTotalRevenueByDate();
        return ResponseEntity.ok(totalRevenue);
    }

    // Endpoint to get total revenue by menu item in a map in descending order
    @RequestMapping("/total-revenue-by-menu-item")
    public ResponseEntity<HashMap<String, Double>> getTotalRevenueByMenuItem() {
        HashMap<String, Double> totalRevenueByMenuItem = statsService.getTotalRevenueByMenuItem();
        return ResponseEntity.ok(totalRevenueByMenuItem);
    }

    //get Average total revenue of a Session grouped by date
    @RequestMapping("/average-session-revenue-by-date")
    public ResponseEntity<List<TableSessionAverageRevenueByDate>> getAverageRevenueByDate() {
        List<TableSessionAverageRevenueByDate> averageRevenueByDate = statsService.getAverageTotalByTableSessionGroupedByDate();
        return ResponseEntity.ok(averageRevenueByDate);
    }









}
