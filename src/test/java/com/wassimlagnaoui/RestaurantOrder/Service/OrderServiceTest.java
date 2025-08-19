package com.wassimlagnaoui.RestaurantOrder.Service;

import com.wassimlagnaoui.RestaurantOrder.DTO.OrderItemRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.PlaceOrderRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.KitchenOrderQueueResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.OrderResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.PlaceOrderResponse;
import com.wassimlagnaoui.RestaurantOrder.Exception.MenuItemNotFoundException;
import com.wassimlagnaoui.RestaurantOrder.Exception.NoActiveTableSessionFoundException;
import com.wassimlagnaoui.RestaurantOrder.Exception.OrderNotFoundException;
import com.wassimlagnaoui.RestaurantOrder.Mapper.OrderItemMapper;
import com.wassimlagnaoui.RestaurantOrder.Repository.MenuItemRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.OrderItemRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.OrderRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.TableSessionRepository;
import com.wassimlagnaoui.RestaurantOrder.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private TableSessionRepository tableSessionRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @Mock
    private OrderItemMapper orderItemMapper;

    @InjectMocks
    private OrderService orderService;

    private TableSession testTableSession;
    private MenuItem testMenuItem;
    private Order testOrder;
    private OrderItem testOrderItem;
    private PlaceOrderRequest placeOrderRequest;

    @BeforeEach
    void setUp() {
        testTableSession = new TableSession();
        testTableSession.setId(1L);
        testTableSession.setTableNumber("T1");

        testTableSession.setSessionStart(LocalDateTime.now());

        testMenuItem = new MenuItem();
        testMenuItem.setId(1L);
        testMenuItem.setName("Burger");
        testMenuItem.setPrice(12.99);
        testMenuItem.setCategory("Main Course");
        testMenuItem.setAvailable(true);

        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setTableSession(testTableSession);
        testOrder.setStatus(OrderStatus.PLACED.name());
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setTotal(25.98);

        testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setOrder(testOrder);
        testOrderItem.setMenuItem(testMenuItem);
        testOrderItem.setQuantity(2);
        testOrderItem.setServed(false);

        testOrder.setItems(Arrays.asList(testOrderItem));

        // Create place order request
        placeOrderRequest = new PlaceOrderRequest();
        placeOrderRequest.setTableSessionId(1L);

        OrderItemRequest orderItemRequest = new OrderItemRequest();
        orderItemRequest.setMenuItemId(1L);
        orderItemRequest.setQuantity(2);

        placeOrderRequest.setItems(Arrays.asList(orderItemRequest));
    }

    @Test
    void placeOrder_ShouldReturnPlaceOrderResponse_WhenValidRequest() {
        // Arrange
        when(tableSessionRepository.findActiveTableSessionById(1L)).thenReturn(Optional.of(testTableSession));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L); // Set the ID to simulate database save
            return order;
        });

        // Act
        PlaceOrderResponse response = orderService.placeOrder(placeOrderRequest);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals(testTableSession.getId(), response.getSessionId());
        assertEquals(OrderStatus.PLACED.name(), response.getStatus());
        assertNotNull(response.getItems());
        assertEquals(1, response.getItems().size());
        assertEquals(25.98, response.getItems().get(0).getTotalPrice());

        verify(tableSessionRepository).findActiveTableSessionById(1L);
        verify(menuItemRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void placeOrder_ShouldThrowException_WhenTableSessionNotFound() {
        // Arrange
        when(tableSessionRepository.findActiveTableSessionById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoActiveTableSessionFoundException.class, () -> orderService.placeOrder(placeOrderRequest));

        verify(tableSessionRepository).findActiveTableSessionById(1L);
        verifyNoInteractions(menuItemRepository);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void placeOrder_ShouldThrowException_WhenMenuItemNotFound() {
        // Arrange
        when(tableSessionRepository.findActiveTableSessionById(1L)).thenReturn(Optional.of(testTableSession));
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MenuItemNotFoundException.class, () -> orderService.placeOrder(placeOrderRequest));

        verify(tableSessionRepository).findActiveTableSessionById(1L);
        verify(menuItemRepository).findById(1L);
        verifyNoInteractions(orderRepository);
    }

    @Test
    void getOrderById_ShouldReturnOrderResponse_WhenOrderExists() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(testOrder));

        // Act
        OrderResponse response = orderService.getOrderById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getOrderId());
        assertEquals(OrderStatus.PLACED.name(), response.getStatus());
        assertEquals(1L, response.getSessionId());
        assertNotNull(response.getOrderItems());

        verify(orderRepository).findById(1L);
    }

    @Test
    void getOrderById_ShouldThrowException_WhenOrderNotFound() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OrderNotFoundException.class, () -> orderService.getOrderById(1L));

        verify(orderRepository).findById(1L);
    }

    @Test
    void getNotServedItems_ShouldReturnKitchenQueue_WhenUnservedItemsExist() {
        // Arrange
        List<OrderItem> unservedItems = Arrays.asList(testOrderItem);
        when(orderItemRepository.findUnservedOrderItem()).thenReturn(unservedItems);

        // Act
        List<KitchenOrderQueueResponse> response = orderService.getNotServedItems();

        // Assert
        assertNotNull(response);
        assertEquals(1, response.size());

        KitchenOrderQueueResponse queueItem = response.get(0);
        assertEquals(testOrderItem.getId(), queueItem.getOrderItemId());
        assertEquals(testOrder.getId(), queueItem.getOrderId());
        assertEquals(testMenuItem.getName(), queueItem.getItemName());
        assertEquals(testTableSession.getTableNumber(), queueItem.getTableNumber());
        assertEquals(testOrderItem.getQuantity(), queueItem.getQuantity());

        verify(orderItemRepository).findUnservedOrderItem();
    }

    @Test
    void getNotServedItems_ShouldThrowException_WhenNoUnservedItems() {
        // Arrange
        when(orderItemRepository.findUnservedOrderItem()).thenReturn(Arrays.asList());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.getNotServedItems());
        assertEquals("No pending orders in the Kitchen", exception.getMessage());

        verify(orderItemRepository).findUnservedOrderItem();
    }

    @Test
    void serveOrderItem_ShouldMarkItemAsServed_WhenItemExists() {
        // Arrange
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(testOrderItem));
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(testOrderItem);

        // Act
        var response = orderService.serveOrderItem(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getOrderItemID());
        assertEquals("Order item marked as served successfully", response.getMessage());
        assertTrue(testOrderItem.getServed());

        verify(orderItemRepository).findById(1L);
        verify(orderItemRepository).save(testOrderItem);
    }

    @Test
    void serveOrderItem_ShouldThrowException_WhenItemNotFound() {
        // Arrange
        when(orderItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> orderService.serveOrderItem(1L));
        assertEquals("OrderItem not found", exception.getMessage());

        verify(orderItemRepository).findById(1L);
        verify(orderItemRepository, never()).save(any());
    }

    @Test
    void placeOrder_ShouldCalculateCorrectTotal() {
        // Arrange
        MenuItem expensiveItem = new MenuItem();
        expensiveItem.setId(2L);
        expensiveItem.setName("Steak");
        expensiveItem.setPrice(25.00);

        OrderItemRequest expensiveItemRequest = new OrderItemRequest();
        expensiveItemRequest.setMenuItemId(2L);
        expensiveItemRequest.setQuantity(3);

        PlaceOrderRequest requestWithExpensiveItem = new PlaceOrderRequest();
        requestWithExpensiveItem.setTableSessionId(1L);
        requestWithExpensiveItem.setItems(Arrays.asList(expensiveItemRequest));

        when(tableSessionRepository.findActiveTableSessionById(1L)).thenReturn(Optional.of(testTableSession));
        when(menuItemRepository.findById(2L)).thenReturn(Optional.of(expensiveItem));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = invocation.getArgument(0);
            order.setId(1L);
            return order;
        });

        // Act
        PlaceOrderResponse response = orderService.placeOrder(requestWithExpensiveItem);

        // Assert
        assertEquals(75.00, response.getItems().get(0).getTotalPrice()); // 25.00 * 3 = 75.00

        verify(orderRepository).save(argThat(order -> order.getTotal().equals(75.00)));
    }
}
