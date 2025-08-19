package com.wassimlagnaoui.RestaurantOrder.Service;

import com.wassimlagnaoui.RestaurantOrder.DTO.MostOrderedItemDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.TableSessionAverageRevenueByDate;
import com.wassimlagnaoui.RestaurantOrder.Repository.MenuItemRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.OrderItemRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.TableSessionRepository;
import com.wassimlagnaoui.RestaurantOrder.model.MenuItem;
import com.wassimlagnaoui.RestaurantOrder.model.Order;
import com.wassimlagnaoui.RestaurantOrder.model.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private TableSessionRepository tableSessionRepository;

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private StatsService statsService;

    private MenuItem burger;
    private MenuItem pizza;
    private Order order1;
    private Order order2;
    private OrderItem orderItem1;
    private OrderItem orderItem2;
    private OrderItem orderItem3;

    @BeforeEach
    void setUp() {
        // Setup menu items
        burger = new MenuItem();
        burger.setId(1L);
        burger.setName("Burger");
        burger.setPrice(12.99);

        pizza = new MenuItem();
        pizza.setId(2L);
        pizza.setName("Pizza");
        pizza.setPrice(15.99);

        // Setup orders
        order1 = new Order();
        order1.setId(1L);
        order1.setOrderDate(LocalDateTime.of(2025, 8, 19, 12, 0));

        order2 = new Order();
        order2.setId(2L);
        order2.setOrderDate(LocalDateTime.of(2025, 8, 20, 14, 0));

        // Setup order items
        orderItem1 = new OrderItem();
        orderItem1.setId(1L);
        orderItem1.setMenuItem(burger);
        orderItem1.setQuantity(2);
        orderItem1.setOrder(order1);

        orderItem2 = new OrderItem();
        orderItem2.setId(2L);
        orderItem2.setMenuItem(pizza);
        orderItem2.setQuantity(1);
        orderItem2.setOrder(order1);

        orderItem3 = new OrderItem();
        orderItem3.setId(3L);
        orderItem3.setMenuItem(burger);
        orderItem3.setQuantity(3);
        orderItem3.setOrder(order2);
    }

    @Test
    void getMostOrderedItems_ShouldReturnListOfMostOrderedItems() {
        // Arrange
        List<MostOrderedItemDTO> expectedItems = Arrays.asList(
            new MostOrderedItemDTO("Burger", 5L, 2L),
            new MostOrderedItemDTO("Pizza", 1L, 1L)
        );

        when(orderItemRepository.findMostOrderedItems()).thenReturn(expectedItems);

        // Act
        List<MostOrderedItemDTO> result = statsService.getMostOrderedItems();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Burger", result.get(0).getName());
        assertEquals(5, result.get(0).getTotalQuantity());
        assertEquals(2, result.get(0).getOrderCount());

        verify(orderItemRepository).findMostOrderedItems();
    }

    @Test
    void getMostOrderedItems_ShouldReturnEmptyList_WhenNoItemsFound() {
        // Arrange
        when(orderItemRepository.findMostOrderedItems()).thenReturn(Arrays.asList());

        // Act
        List<MostOrderedItemDTO> result = statsService.getMostOrderedItems();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderItemRepository).findMostOrderedItems();
    }

    @Test
    void getTotalRevenueByDate_ShouldReturnRevenueMapByDate() {
        // Arrange
        List<String> dates = Arrays.asList("2025-08-19", "2025-08-20");
        List<OrderItem> allOrderItems = Arrays.asList(orderItem1, orderItem2, orderItem3);

        when(tableSessionRepository.findAllDates()).thenReturn(dates);
        when(orderItemRepository.findAll()).thenReturn(allOrderItems);

        // Act
        HashMap<String, Double> result = statsService.getTotalRevenueByDate();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // 2025-08-19: (2 * 12.99) + (1 * 15.99) = 25.98 + 15.99 = 41.97
        assertEquals(41.97, result.get("2025-08-19"), 0.01);

        // 2025-08-20: (3 * 12.99) = 38.97
        assertEquals(38.97, result.get("2025-08-20"), 0.01);

        verify(tableSessionRepository).findAllDates();
        verify(orderItemRepository, times(2)).findAll();
    }

    @Test
    void getTotalRevenueByDate_ShouldReturnEmptyMap_WhenNoDatesFound() {
        // Arrange
        when(tableSessionRepository.findAllDates()).thenReturn(Arrays.asList());

        // Act
        HashMap<String, Double> result = statsService.getTotalRevenueByDate();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(tableSessionRepository).findAllDates();
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void getTotalRevenueByMenuItem_ShouldReturnRevenueMapByMenuItem() {
        // Arrange
        List<MenuItem> menuItems = Arrays.asList(burger, pizza);
        List<OrderItem> allOrderItems = Arrays.asList(orderItem1, orderItem2, orderItem3);

        when(menuItemRepository.findAll()).thenReturn(menuItems);
        when(orderItemRepository.findAll()).thenReturn(allOrderItems);

        // Act
        HashMap<String, Double> result = statsService.getTotalRevenueByMenuItem();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Burger: (2 * 12.99) + (3 * 12.99) = 25.98 + 38.97 = 64.95
        assertEquals(64.95, result.get("Burger"), 0.01);

        // Pizza: (1 * 15.99) = 15.99
        assertEquals(15.99, result.get("Pizza"), 0.01);

        verify(menuItemRepository).findAll();
        verify(orderItemRepository, times(2)).findAll();
    }

    @Test
    void getTotalRevenueByMenuItem_ShouldReturnEmptyMap_WhenNoMenuItemsFound() {
        // Arrange
        when(menuItemRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        HashMap<String, Double> result = statsService.getTotalRevenueByMenuItem();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(menuItemRepository).findAll();
        verifyNoInteractions(orderItemRepository);
    }

    @Test
    void getTotalRevenueByMenuItem_ShouldHandleZeroRevenue_WhenItemNotOrdered() {
        // Arrange
        MenuItem saladItem = new MenuItem();
        saladItem.setName("Salad");
        saladItem.setPrice(8.99);

        List<MenuItem> menuItems = Arrays.asList(burger, saladItem);
        List<OrderItem> allOrderItems = Arrays.asList(orderItem1); // Only burger ordered

        when(menuItemRepository.findAll()).thenReturn(menuItems);
        when(orderItemRepository.findAll()).thenReturn(allOrderItems);

        // Act
        HashMap<String, Double> result = statsService.getTotalRevenueByMenuItem();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(25.98, result.get("Burger"), 0.01); // 2 * 12.99
        assertEquals(0.0, result.get("Salad"), 0.01); // Not ordered

        verify(menuItemRepository).findAll();
        verify(orderItemRepository, times(2)).findAll();
    }

    @Test
    void getAverageTotalByTableSessionGroupedByDate_ShouldReturnAverageRevenueByDate() {
        // Arrange
        Object[] record1 = {"2025-08-19", 41.97};
        Object[] record2 = {"2025-08-20", 38.97};
        List<Object[]> records = Arrays.asList(record1, record2);

        when(tableSessionRepository.findAverageTotalByTableSessionGroupedByDate()).thenReturn(records);

        // Act
        List<TableSessionAverageRevenueByDate> result = statsService.getAverageTotalByTableSessionGroupedByDate();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        TableSessionAverageRevenueByDate firstRecord = result.get(0);
        assertEquals("2025-08-19", firstRecord.getDate());
        assertEquals(41.97, firstRecord.getAverageRevenue(), 0.01);

        TableSessionAverageRevenueByDate secondRecord = result.get(1);
        assertEquals("2025-08-20", secondRecord.getDate());
        assertEquals(38.97, secondRecord.getAverageRevenue(), 0.01);

        verify(tableSessionRepository).findAverageTotalByTableSessionGroupedByDate();
    }

    @Test
    void getAverageTotalByTableSessionGroupedByDate_ShouldReturnEmptyList_WhenNoRecordsFound() {
        // Arrange
        when(tableSessionRepository.findAverageTotalByTableSessionGroupedByDate()).thenReturn(Arrays.asList());

        // Act
        List<TableSessionAverageRevenueByDate> result = statsService.getAverageTotalByTableSessionGroupedByDate();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(tableSessionRepository).findAverageTotalByTableSessionGroupedByDate();
    }

    @Test
    void getTotalRevenueByDate_ShouldCalculateCorrectRevenue_WithMultipleOrderItems() {
        // Arrange
        List<String> dates = Arrays.asList("2025-08-19");

        // Create additional order items for the same date
        OrderItem additionalOrderItem = new OrderItem();
        additionalOrderItem.setMenuItem(pizza);
        additionalOrderItem.setQuantity(2);
        additionalOrderItem.setOrder(order1); // Same date as order1

        List<OrderItem> allOrderItems = Arrays.asList(orderItem1, orderItem2, additionalOrderItem);

        when(tableSessionRepository.findAllDates()).thenReturn(dates);
        when(orderItemRepository.findAll()).thenReturn(allOrderItems);

        // Act
        HashMap<String, Double> result = statsService.getTotalRevenueByDate();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        // Expected: (2 * 12.99) + (1 * 15.99) + (2 * 15.99) = 25.98 + 15.99 + 31.98 = 73.95
        assertEquals(73.95, result.get("2025-08-19"), 0.01);
    }
}
