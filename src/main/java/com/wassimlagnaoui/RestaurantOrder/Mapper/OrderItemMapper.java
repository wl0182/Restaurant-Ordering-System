package com.wassimlagnaoui.RestaurantOrder.Mapper;

import com.wassimlagnaoui.RestaurantOrder.DTO.OrderItemRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.OrderItemResponse;
import com.wassimlagnaoui.RestaurantOrder.model.MenuItem;
import com.wassimlagnaoui.RestaurantOrder.model.Order;
import com.wassimlagnaoui.RestaurantOrder.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public static OrderItemResponse fromOrderItem(OrderItem orderItem){
        OrderItemResponse orderItemResponse = new OrderItemResponse();

        double totalPrice = orderItem.getQuantity() * orderItem.getMenuItem().getPrice();

        orderItemResponse.setMenuItemId(orderItem.getMenuItem().getId());

        orderItemResponse.setUnitPrice(orderItem.getMenuItem().getPrice());
        orderItemResponse.setTotalPrice(totalPrice);


        return orderItemResponse;

    }

    public static OrderItem toOrderItem(OrderItemRequest orderItemRequest, MenuItem menuItem, Order order){
        OrderItem orderItem = new OrderItem();


        orderItem.setMenuItem(menuItem);
        orderItem.setOrder(order);
        orderItem.setQuantity(orderItemRequest.getQuantity());


        return orderItem;
    }

}
