package com.wassimlagnaoui.RestaurantOrder.Service;


import com.wassimlagnaoui.RestaurantOrder.DTO.*;
import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.PlaceOrderRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.*;
import com.wassimlagnaoui.RestaurantOrder.Mapper.OrderItemMapper;
import com.wassimlagnaoui.RestaurantOrder.Mapper.OrderMapper;
import com.wassimlagnaoui.RestaurantOrder.Repository.MenuItemRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.OrderItemRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.OrderRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.TableSessionRepository;
import com.wassimlagnaoui.RestaurantOrder.model.*;
import com.wassimlagnaoui.RestaurantOrder.Exception.MenuItemNotFoundException;
import com.wassimlagnaoui.RestaurantOrder.Exception.NoActiveTableSessionFoundException;
import com.wassimlagnaoui.RestaurantOrder.Exception.OrderNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing orders in the restaurant order system.
 * Handles placing orders, retrieving orders, updating order status, and kitchen queue operations.
 */
@Slf4j
@Service
public class OrderService {


    private  final TableSessionRepository tableSessionRepository;

    private final OrderItemRepository orderItemRepository;

    private final OrderRepository orderRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderService(TableSessionRepository tableSessionRepository, OrderItemRepository orderItemRepository, OrderRepository orderRepository, MenuItemRepository menuItemRepository, OrderItemMapper orderItemMapper) {
        this.tableSessionRepository = tableSessionRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemMapper = orderItemMapper;
    }
    // place order new method
    /**
     * Places a new order for a table session.
     * @param orderRequest the request DTO containing order details
     * @return PlaceOrderResponse with order details
     */
    @Transactional
    public PlaceOrderResponse placeOrder(PlaceOrderRequest orderRequest){
        TableSession tableSession = tableSessionRepository.findActiveTableSessionById(orderRequest.getTableSessionId())
            .orElseThrow(NoActiveTableSessionFoundException::new);

        Order order = new Order();
        order.setStatus(OrderStatus.PLACED.name());
        order.setTableSession(tableSession);
        order.setOrderDate(LocalDateTime.now());

        List<OrderItem> orderItems = new ArrayList<>();
        Double total = 0.0;

        for (OrderItemRequest orderItemRequest: orderRequest.getItems()){
            MenuItem menuItem = menuItemRepository.findById(orderItemRequest.getMenuItemId())
                .orElseThrow(MenuItemNotFoundException::new);

            OrderItem orderItem = new OrderItem();

            orderItem.setServed(false);
            orderItem.setMenuItem(menuItem);
            orderItem.setQuantity(orderItemRequest.getQuantity());
            orderItem.setOrder(order);

            total += orderItem.getQuantity()*orderItem.getMenuItem().getPrice();

            orderItems.add(orderItem);

        }

        order.setItems(orderItems);
        order.setTotal(total);

        orderRepository.save(order);

        // forming the response

        PlaceOrderResponse response = new PlaceOrderResponse();

        response.setOrderId(order.getId());
        response.setSessionId(tableSession.getId());
        response.setCreatedAt(order.getOrderDate());
        response.setStatus(order.getStatus());

        // setting item
        List<OrderItemResponse> itemResponses = new ArrayList<>();

        for (OrderItem orderItem : orderItems){

            OrderItemResponse orderItemResponse = new OrderItemResponse();
            orderItemResponse.setItemId(orderItem.getId());
            orderItemResponse.setMenuItemId(orderItem.getMenuItem().getId());
            orderItemResponse.setName(orderItem.getMenuItem().getName());
            orderItemResponse.setQuantity(orderItem.getQuantity());
            orderItemResponse.setServed(orderItem.getServed());
            orderItemResponse.setTotalPrice(orderItem.getQuantity() * orderItem.getMenuItem().getPrice());

            itemResponses.add(orderItemResponse);
        }

        response.setItems(itemResponses);


        return response ;



    }



    // Being Replaced
    /**
     * Creates a new order (legacy method).
     * @param orderRequest the request DTO for order creation
     * @return OrderResponse with created order details
     */
    public OrderResponse createOrder(OrderRequest orderRequest) {
     TableSession tableSession = tableSessionRepository.findActiveTableSessionByTableNumber(orderRequest.getTableSession().getTableNumber()).orElseThrow(()-> new RuntimeException("No TableSession found for this table"));

     Order order = new Order();

     order.setTableSession(tableSession);
     order.setOrderDate(LocalDateTime.now());

     order.setStatus(orderRequest.getStatus());


     // Setting the items to be inserted
     List<OrderItem> orderItems = new ArrayList<>();

     List<OrderItemRequest> orderItemRequests = orderRequest.getOrderItems();

     for (OrderItemRequest orderItemRequest: orderItemRequests){
         MenuItem menuItem = menuItemRepository.findById(orderItemRequest.getMenuItemId()).orElseThrow(()-> new RuntimeException("MenuItem Not Found"));

         orderItems.add(OrderItemMapper.toOrderItem(orderItemRequest,menuItem,order));

     }

     order.setItems(orderItems);
     // setting the total
        double total=0;

        for (OrderItem orderItem: orderItems){
            total += orderItem.getQuantity()*orderItem.getMenuItem().getPrice();
        }

        order.setTotal(total);

        orderRepository.save(order);

        return OrderMapper.fromOrder(order);



    }

    /**
     * Retrieves an order by its ID.
     * @param id the order ID
     * @return OrderResponse with order details
     */
    public OrderResponse getOrderById(Long id){
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found in the System"));

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(id);
        orderResponse.setStatus(order.getStatus());
        orderResponse.setSessionId(order.getTableSession().getId());

        List<OrderItemResponse> items = new ArrayList<>();

       items = order.getItems().stream().map(orderItem -> {
         OrderItemResponse orderItemResponse = new OrderItemResponse();

         orderItemResponse.setItemId(orderItem.getId());
         orderItemResponse.setMenuItemId(orderItem.getMenuItem().getId());
         orderItemResponse.setName(orderItem.getMenuItem().getName());
         orderItemResponse.setServed(orderItem.getServed());
         orderItemResponse.setUnitPrice(orderItem.getMenuItem().getPrice());
         orderItemResponse.setTotalPrice(orderItem.getMenuItem().getPrice()*orderItem.getQuantity());

         return orderItemResponse;
       }).collect(Collectors.toUnmodifiableList());


       orderResponse.setOrderItems(items);

        return orderResponse;

    }

    /**
     * Retrieves all orders for a given session ID.
     * @param sessionId the session ID
     * @return List of OrderResponse for the session
     */
    public List<OrderResponse> getOrderBySessionId(Long sessionId){
            TableSession tableSession = tableSessionRepository.findById(sessionId).orElseThrow(()-> new RuntimeException("Tablesession does not exist"));


            List<Order> orders = orderRepository.findByTableSession(tableSession);

            List<OrderResponse> orderResponses = new ArrayList<>();

            for (Order order : orders){
                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setOrderId(order.getId());
                orderResponse.setStatus(order.getStatus());
                orderResponse.setSessionId(order.getTableSession().getId());
                // map items
                List<OrderItemResponse> itemResponses = new ArrayList<>();

                for (OrderItem orderItem : order.getItems()){
                    OrderItemResponse orderItemResponse = new OrderItemResponse();
                    orderItemResponse.setItemId(orderItem.getId());
                    orderItemResponse.setQuantity(orderItem.getQuantity());
                    orderItemResponse.setMenuItemId(orderItem.getMenuItem().getId());
                    orderItemResponse.setName(orderItem.getMenuItem().getName());
                    orderItemResponse.setServed(orderItem.getServed());
                    orderItemResponse.setUnitPrice(orderItem.getMenuItem().getPrice());
                    orderItemResponse.setTotalPrice(orderItem.getMenuItem().getPrice()*orderItem.getQuantity());

                    itemResponses.add(orderItemResponse);
                }
               orderResponse.setOrderItems(itemResponses);

                orderResponses.add(orderResponse);

            }






            return orderResponses ;
    }

    /**
     * Retrieves all unserved order items for a session.
     * @param sessionId the session ID
     * @return List of OrderItemResponse for unserved items
     */
    public List<OrderItemResponse> getUnServedItemsBySession(Long sessionId){
        TableSession tableSession = tableSessionRepository.findById(sessionId).orElseThrow(()-> new RuntimeException("Session Not Found"));

        List<Order> orders = orderRepository.findByTableSession(sessionId);

        List<OrderItemResponse> orderItemResponses = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .filter(orderItem -> orderItem.getServed().equals(false))
                .map(orderItem -> {
                    OrderItemResponse orderItemResponse = new OrderItemResponse();
                    orderItemResponse.setItemId(orderItem.getId());
                    orderItemResponse.setQuantity(orderItem.getQuantity());
                    orderItemResponse.setServed(orderItem.getServed());
                    orderItemResponse.setMenuItemId(orderItem.getMenuItem().getId());
                    orderItemResponse.setName(orderItem.getMenuItem().getName());
                    orderItemResponse.setUnitPrice(orderItem.getMenuItem().getPrice());
                    orderItemResponse.setTotalPrice(orderItem.getMenuItem().getPrice()*orderItem.getQuantity());
                    return orderItemResponse;
                } )
                .collect(Collectors.toUnmodifiableList());





        return orderItemResponses;
    }
    /**
     * Retrieves all served order items for a session.
     * @param sessionId the session ID
     * @return List of OrderItemResponse for served items
     */
    public List<OrderItemResponse> getServedItemsBySession(Long sessionId){
        TableSession tableSession = tableSessionRepository.findById(sessionId).orElseThrow(()-> new RuntimeException("Session Not Found"));

        List<Order> orders = orderRepository.findByTableSession(sessionId);

        List<OrderItemResponse> orderItemResponses = orders.stream()
                .flatMap(order -> order.getItems().stream())
                .filter(OrderItem::getServed)
                .map(orderItem -> {
                    OrderItemResponse orderItemResponse = new OrderItemResponse();
                    orderItemResponse.setItemId(orderItem.getId());
                    orderItemResponse.setQuantity(orderItem.getQuantity());
                    orderItemResponse.setServed(true);
                    orderItemResponse.setMenuItemId(orderItem.getMenuItem().getId());
                    orderItemResponse.setName(orderItem.getMenuItem().getName());
                    orderItemResponse.setUnitPrice(orderItem.getMenuItem().getPrice());
                    orderItemResponse.setTotalPrice(orderItem.getMenuItem().getPrice()*orderItem.getQuantity());
                    return orderItemResponse;
                } )
                .collect(Collectors.toUnmodifiableList());





        return orderItemResponses;
    }

    /**
     * Marks an order as served by its ID.
     * @param id the order ID
     * @return OrderResponse with updated status
     */
    public OrderResponse markOrderAsServed(Long id){
       Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not found"));

       order.setStatus(OrderStatus.SERVED.name());

       orderRepository.save(order);

       return OrderMapper.fromOrder(order);

    }

    /**
     * Retrieves the kitchen queue of all unserved orders.
     * @return List of OrderResponse for kitchen queue
     */
    public List<OrderResponse> getKitchenQueue(){
        List<Order> kitchenQueue = orderRepository.findAllUnservedOrder();

        if (kitchenQueue.isEmpty()){
            throw new RuntimeException("No pending orders in the Kitchen");
        }

        List<OrderResponse> orderResponses = new ArrayList<>();
        for (Order order: kitchenQueue){
            orderResponses.add(OrderMapper.fromOrder(order));
        }

        return orderResponses;
    }

    /**
     * Retrieves all not served order items for the kitchen.
     * @return List of KitchenOrderQueueResponse for unserved items
     */
    public List<KitchenOrderQueueResponse> getNotServedItems(){
        List<OrderItem> unservedOrderItems = orderItemRepository.findUnservedOrderItem();

        if (unservedOrderItems.isEmpty()){
            throw new RuntimeException("No pending orders in the Kitchen");
        }

        List<KitchenOrderQueueResponse> kitchenOrderQueueResponses = new ArrayList<>();

        for (OrderItem orderItem : unservedOrderItems){
            KitchenOrderQueueResponse kitchenOrderQueueResponse = new KitchenOrderQueueResponse();
            kitchenOrderQueueResponse.setOrderItemId(orderItem.getId());
            kitchenOrderQueueResponse.setOrderId(orderItem.getOrder().getId());
            kitchenOrderQueueResponse.setItemName(orderItem.getMenuItem().getName());
            kitchenOrderQueueResponse.setTableNumber(orderItem.getOrder().getTableSession().getTableNumber());
            kitchenOrderQueueResponse.setQuantity(orderItem.getQuantity());
            kitchenOrderQueueResponse.setServed(orderItem.getServed());
            kitchenOrderQueueResponses.add(kitchenOrderQueueResponse);
        }

        return kitchenOrderQueueResponses;

    }


    /**
     * Marks an order item as served by its ID.
     * @param orderItemId the order item ID
     * @return MarkOrderItemServedDTO with result message
     */
    public MarkOrderItemServedDTO serveOrderItem(Long orderItemId) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new RuntimeException("OrderItem not found"));

        orderItem.setServed(true);

        orderItemRepository.save(orderItem);

        MarkOrderItemServedDTO response = new MarkOrderItemServedDTO();

        response.setOrderItemID(orderItem.getId());
        response.setMessage("Order item marked as served successfully");


        return response;
    }

    /**
     * Checks the served status of all items in an order.
     * @param id the order ID
     * @return OrderServedStatusDTO with status flags
     */
    public OrderServedStatusDTO checkStatusOfItemsByOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(()-> new RuntimeException("Order not Found"));
        OrderServedStatusDTO response = new OrderServedStatusDTO();

        Boolean allItemServed = order.getItems().stream()
                .allMatch(orderItem -> orderItem.getServed());

        Boolean someServed = order.getItems().stream().anyMatch(OrderItem::getServed);
        Boolean noneServed = order.getItems().stream().noneMatch(OrderItem::getServed);

        response.setOrderId(order.getId());
        response.setAllItemsServed(allItemServed);
        response.setNoneServed(noneServed);
        response.setSomeServed(someServed);



        return response;


    }
}
