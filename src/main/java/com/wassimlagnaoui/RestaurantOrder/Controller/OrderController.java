package com.wassimlagnaoui.RestaurantOrder.Controller;


import com.wassimlagnaoui.RestaurantOrder.DTO.OrderServedStatusDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.*;
import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.PlaceOrderRequest;
import com.wassimlagnaoui.RestaurantOrder.Service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {



    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<PlaceOrderResponse> placeOrder(@RequestBody PlaceOrderRequest placeOrderRequest){
        PlaceOrderResponse orderResponse = orderService.placeOrder(placeOrderRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable("id") Long id){
        OrderResponse orderResponse = orderService.getOrderById(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @GetMapping("/sessions/{id}")
    public ResponseEntity<List<OrderResponse>> getOrderBySession(@PathVariable("id") Long sessionId){
        List<OrderResponse> orderResponses = orderService.getOrderBySessionId(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
    }

    @GetMapping("/sessions/{id}/served")
    public ResponseEntity<List<OrderItemResponse>> showServedOrders(@PathVariable("id") long sessionId){
        List<OrderItemResponse> itemResponses = orderService.getServedItemsBySession(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(itemResponses);
    }

    @GetMapping("/sessions/{id}/unserved")
    public ResponseEntity<List<OrderItemResponse>> showUnServedOrders(@PathVariable("id") long sessionId){
        List<OrderItemResponse> orderResponses= orderService.getUnServedItemsBySession(sessionId);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
    }

    @PostMapping("/{id}/serve")
    public ResponseEntity<OrderResponse> markAsServed(@PathVariable("id") long id){
        OrderResponse orderResponse = orderService.markOrderAsServed(id);
        return ResponseEntity.status(HttpStatus.OK).body(orderResponse);
    }

    @GetMapping("/kitchen/queue")
    public ResponseEntity<List<KitchenOrderQueueResponse>> getKitchenQueue(){
        List<KitchenOrderQueueResponse> orderResponses = orderService.getNotServedItems();
        return ResponseEntity.status(HttpStatus.OK).body(orderResponses);
    }



    @PostMapping("/orderItem/{id}/serve")
    public ResponseEntity<MarkOrderItemServedDTO> serveOrderItem(@PathVariable("id") Long orderItemId){
        MarkOrderItemServedDTO response = orderService.serveOrderItem(orderItemId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/{id}/Items-status")
    public ResponseEntity<OrderServedStatusDTO> checkAllItemsServed(@PathVariable Long id) {
        OrderServedStatusDTO dto = orderService.checkStatusOfItemsByOrder(id);
        return ResponseEntity.ok(dto);
    }



}
