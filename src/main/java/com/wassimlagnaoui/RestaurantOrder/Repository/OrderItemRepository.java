package com.wassimlagnaoui.RestaurantOrder.Repository;

import com.wassimlagnaoui.RestaurantOrder.DTO.MostOrderedItemDTO;
import com.wassimlagnaoui.RestaurantOrder.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("select o from OrderItem o where o.served = false order by o.order.orderDate")
    List<OrderItem> findUnservedOrderItem();

    @Query("select o from OrderItem o where o.served = true order by o.order.orderDate")
    List<OrderItem> findServedOrderItem();

    // Find most ordered items with total quantity and order count using MostOrderedItemDTO
    @Query("SELECT new com.wassimlagnaoui.RestaurantOrder.DTO.MostOrderedItemDTO(" +
           "oi.menuItem.name, SUM(oi.quantity), COUNT(DISTINCT oi.order.id)) " +
           "FROM OrderItem oi " +
           "WHERE oi.served = true " +
           "GROUP BY oi.menuItem.name " +
           "ORDER BY SUM(oi.quantity) DESC")
    List<MostOrderedItemDTO> findMostOrderedItems();


}
