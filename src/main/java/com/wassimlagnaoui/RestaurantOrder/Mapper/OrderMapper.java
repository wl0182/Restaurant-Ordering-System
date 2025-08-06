package com.wassimlagnaoui.RestaurantOrder.Mapper;

import com.wassimlagnaoui.RestaurantOrder.DTO.Response.OrderItemResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.OrderRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.OrderResponse;
import com.wassimlagnaoui.RestaurantOrder.model.Order;
import com.wassimlagnaoui.RestaurantOrder.model.OrderItem;
import com.wassimlagnaoui.RestaurantOrder.model.TableSession;

import java.util.ArrayList;
import java.util.List;

public class OrderMapper {
    public static Order toOrder(OrderRequest orderRequest, List<OrderItem> orderItems, TableSession tableSession) {
        Order order = new Order();

        order.setOrderDate(orderRequest.getOrderDate());
        order.setItems(orderItems);
        order.setTableSession(tableSession);
        order.setStatus(orderRequest.getStatus());





       return order;

    }

    public static OrderResponse fromOrder(Order order) {
        OrderResponse orderResponse = new OrderResponse();


        // setting up the order items
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse = OrderItemMapper.fromOrderItem(item);
            orderItemResponses.add(orderItemResponse);
        }
        //
        orderResponse.setOrderItems(orderItemResponses);
        return orderResponse;
    }


}
