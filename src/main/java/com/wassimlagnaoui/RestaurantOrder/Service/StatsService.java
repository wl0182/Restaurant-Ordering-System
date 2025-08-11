package com.wassimlagnaoui.RestaurantOrder.Service;

import com.wassimlagnaoui.RestaurantOrder.DTO.MostOrderedItemDTO;
import com.wassimlagnaoui.RestaurantOrder.Repository.MenuItemRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.OrderItemRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.TableSessionRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class StatsService {
    private final OrderItemRepository orderItemRepository;
    private final TableSessionRepository tableSessionRepository;
    private final MenuItemRepository   menuItemRepository;

    public StatsService(OrderItemRepository orderItemRepository, TableSessionRepository tableSessionRepository, MenuItemRepository menuItemRepository) {
        this.orderItemRepository = orderItemRepository;
        this.tableSessionRepository = tableSessionRepository;
        this.menuItemRepository = menuItemRepository;
    }

    // find most ordered items
    /**
     * Retrieves the most ordered items from the order item repository.
     * This method uses a custom query to fetch items that have been served, along with their total quantity and order count.
     *
     * @return a list of MostOrderedItemDTO containing the name, total quantity, and order count of each item.
     */
    public List<MostOrderedItemDTO> getMostOrderedItems() {
        return orderItemRepository.findMostOrderedItems();
    }


    /**
     * Calculates the total revenue per date.
     * This method retrieves all unique dates from table sessions and calculates the total revenue for each date.
     *
     * @return a HashMap where the key is the date (formatted as "yyyy-MM-dd") and the value is the total revenue for that date.
     */
    public HashMap<String, Double> getTotalRevenuePerDate() {
        // Initialize a HashMap to store the total revenue per date
        HashMap<String, Double> totalRevenuePerDate = new HashMap<>();

        // Fetch all table sessions by date formatted as "yyyy-MM-dd"
        List<String> dates = tableSessionRepository.findAllDates();
        for (String date : dates) {
            // Calculate the total revenue for each date
            Double totalRevenue = calculateTotalRevenueByDate(date);
            // Store the result in the HashMap
            totalRevenuePerDate.put(date, totalRevenue);
        }
        return totalRevenuePerDate;

    }

    private Double calculateTotalRevenueByDate(String date) {
    // Calculate the total revenue for a specific date
        return orderItemRepository.findAll().stream()
                .filter(orderItem -> orderItem.getOrder().getOrderDate().toLocalDate().toString().equals(date))
                .mapToDouble(orderItem -> orderItem.getMenuItem().getPrice() * orderItem.getQuantity())
                .sum();
    }
}
