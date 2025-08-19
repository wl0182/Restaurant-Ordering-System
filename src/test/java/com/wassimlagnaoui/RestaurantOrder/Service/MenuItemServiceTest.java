package com.wassimlagnaoui.RestaurantOrder.Service;

import com.wassimlagnaoui.RestaurantOrder.DTO.MenuItemRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.MenuItemResponse;
import com.wassimlagnaoui.RestaurantOrder.Exception.MenuItemIdNotFoundException;
import com.wassimlagnaoui.RestaurantOrder.Exception.MenuItemNotAvailableException;
import com.wassimlagnaoui.RestaurantOrder.Repository.MenuItemRepository;
import com.wassimlagnaoui.RestaurantOrder.model.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemRepository menuItemRepository;

    @InjectMocks
    private MenuItemService menuItemService;

    private MenuItem testMenuItem;
    private MenuItemRequest menuItemRequest;

    @BeforeEach
    void setUp() {
        testMenuItem = new MenuItem();
        testMenuItem.setId(1L);
        testMenuItem.setName("Cheeseburger");
        testMenuItem.setDescription("Delicious cheeseburger with fries");
        testMenuItem.setPrice(12.99);
        testMenuItem.setCategory("Main Course");
        testMenuItem.setAvailable(true);
        testMenuItem.setImageUrl("burger.jpg");

        menuItemRequest = new MenuItemRequest();
        menuItemRequest.setName("Pizza Margherita");
        menuItemRequest.setDescription("Classic pizza with tomato and mozzarella");
        menuItemRequest.setPrice(14.99);
        menuItemRequest.setCategory("Main Course");
        menuItemRequest.setImageUrl("pizza.jpg");
        menuItemRequest.setAvailable(true); // Add this line to set available to true
    }

    @Test
    void findById_ShouldReturnMenuItemResponse_WhenMenuItemExists() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));

        // Act
        MenuItemResponse response = menuItemService.findById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(testMenuItem.getId(), response.getId());
        assertEquals(testMenuItem.getName(), response.getName());
        assertEquals(testMenuItem.getPrice(), response.getPrice());
        assertEquals(testMenuItem.getCategory(), response.getCategory());
        assertEquals(testMenuItem.isAvailable(), response.isAvailable());

        verify(menuItemRepository).findById(1L);
    }

    @Test
    void findById_ShouldThrowException_WhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MenuItemIdNotFoundException.class, () -> menuItemService.findById(1L));

        verify(menuItemRepository).findById(1L);
    }

    @Test
    void findAll_ShouldReturnListOfMenuItems() {
        // Arrange
        MenuItem secondMenuItem = new MenuItem();
        secondMenuItem.setId(2L);
        secondMenuItem.setName("Caesar Salad");
        secondMenuItem.setPrice(8.99);
        secondMenuItem.setCategory("Appetizer");
        secondMenuItem.setAvailable(true);

        List<MenuItem> menuItems = Arrays.asList(testMenuItem, secondMenuItem);
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        // Act
        List<MenuItemResponse> responses = menuItemService.findAll();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("Cheeseburger", responses.get(0).getName());
        assertEquals("Caesar Salad", responses.get(1).getName());

        verify(menuItemRepository).findAll();
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoMenuItems() {
        // Arrange
        when(menuItemRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<MenuItemResponse> responses = menuItemService.findAll();

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(menuItemRepository).findAll();
    }

    @Test
    void createMenuItem_ShouldReturnMenuItemResponse_WhenValidRequest() {
        // Arrange
        MenuItem savedMenuItem = new MenuItem();
        savedMenuItem.setId(2L);
        savedMenuItem.setName(menuItemRequest.getName());
        savedMenuItem.setDescription(menuItemRequest.getDescription());
        savedMenuItem.setPrice(menuItemRequest.getPrice());
        savedMenuItem.setCategory(menuItemRequest.getCategory());
        savedMenuItem.setImageUrl(menuItemRequest.getImageUrl());
        savedMenuItem.setAvailable(true); // Default value

        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(savedMenuItem);

        // Act
        MenuItemResponse response = menuItemService.createMenuItem(menuItemRequest);

        // Assert
        assertNotNull(response);
        assertEquals(savedMenuItem.getName(), response.getName());
        assertEquals(savedMenuItem.getPrice(), response.getPrice());
        assertEquals(savedMenuItem.getCategory(), response.getCategory());
        assertTrue(response.isAvailable()); // Should be true by default

        verify(menuItemRepository).save(any(MenuItem.class));
    }

    @Test
    void updateAvailability_ShouldToggleToFalse_WhenCurrentlyTrue() {
        // Arrange
        testMenuItem.setAvailable(true);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(testMenuItem);

        // Act
        MenuItemResponse response = menuItemService.updateAvailability(1L);

        // Assert
        assertNotNull(response);
        assertFalse(testMenuItem.isAvailable()); // Should be toggled to false

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository).save(testMenuItem);
    }

    @Test
    void updateAvailability_ShouldToggleToTrue_WhenCurrentlyFalse() {
        // Arrange
        testMenuItem.setAvailable(false);
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(testMenuItem);

        // Act
        MenuItemResponse response = menuItemService.updateAvailability(1L);

        // Assert
        assertNotNull(response);
        assertTrue(testMenuItem.isAvailable()); // Should be toggled to true

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository).save(testMenuItem);
    }

    @Test
    void updateAvailability_ShouldThrowException_WhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MenuItemNotAvailableException.class, () -> menuItemService.updateAvailability(1L));

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void getAvailableMenuItems_ShouldReturnOnlyAvailableItems() {
        // Arrange
        MenuItem unavailableItem = new MenuItem();
        unavailableItem.setId(2L);
        unavailableItem.setName("Unavailable Item");
        unavailableItem.setAvailable(false);

        List<MenuItem> allItems = Arrays.asList(testMenuItem, unavailableItem);
        when(menuItemRepository.findAll()).thenReturn(allItems);

        // Act
        List<MenuItemResponse> responses = menuItemService.getAvailableMenuItems();

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Cheeseburger", responses.get(0).getName());
        assertTrue(responses.get(0).isAvailable());

        verify(menuItemRepository).findAll();
    }

    @Test
    void getAvailableMenuItems_ShouldReturnEmptyList_WhenNoAvailableItems() {
        // Arrange
        testMenuItem.setAvailable(false);
        when(menuItemRepository.findAll()).thenReturn(Arrays.asList(testMenuItem));

        // Act
        List<MenuItemResponse> responses = menuItemService.getAvailableMenuItems();

        // Assert
        assertNotNull(responses);
        assertTrue(responses.isEmpty());

        verify(menuItemRepository).findAll();
    }

    @Test
    void getMenuItemsByCategory_ShouldReturnItemsInCategory() {
        // Arrange
        MenuItem appetizer = new MenuItem();
        appetizer.setId(2L);
        appetizer.setName("Caesar Salad");
        appetizer.setCategory("Appetizer");

        List<MenuItem> mainCourseItems = Arrays.asList(testMenuItem);
        when(menuItemRepository.findByCategory("Main Course")).thenReturn(mainCourseItems);

        // Act
        List<MenuItemResponse> responses = menuItemService.getMenuItemsByCategory("Main Course");

        // Assert
        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals("Cheeseburger", responses.get(0).getName());
        assertEquals("Main Course", responses.get(0).getCategory());

        verify(menuItemRepository).findByCategory("Main Course");
    }

    @Test
    void updateCategory_ShouldUpdateCategory_WhenMenuItemExists() {
        // Arrange
        String newCategory = "Appetizer";
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(testMenuItem);

        // Act
        MenuItemResponse response = menuItemService.updateCategory(1L, newCategory);

        // Assert
        assertNotNull(response);
        assertEquals(newCategory, testMenuItem.getCategory());

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository).save(testMenuItem);
    }

    @Test
    void updateCategory_ShouldThrowException_WhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MenuItemIdNotFoundException.class, () ->
            menuItemService.updateCategory(1L, "New Category"));

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void updatePrice_ShouldUpdatePrice_WhenMenuItemExists() {
        // Arrange
        double newPrice = 15.99;
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(testMenuItem);

        // Act
        MenuItemResponse response = menuItemService.updatePrice(1L, newPrice);

        // Assert
        assertNotNull(response);
        assertEquals(newPrice, testMenuItem.getPrice());

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository).save(testMenuItem);
    }

    @Test
    void updatePrice_ShouldThrowException_WhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MenuItemIdNotFoundException.class, () ->
            menuItemService.updatePrice(1L, 15.99));

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository, never()).save(any());
    }

    @Test
    void updateName_ShouldUpdateName_WhenMenuItemExists() {
        // Arrange
        String newName = "Deluxe Cheeseburger";
        when(menuItemRepository.findById(1L)).thenReturn(Optional.of(testMenuItem));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(testMenuItem);

        // Act
        MenuItemResponse response = menuItemService.updateName(1L, newName);

        // Assert
        assertNotNull(response);
        assertEquals(newName, testMenuItem.getName());

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository).save(testMenuItem);
    }

    @Test
    void updateName_ShouldThrowException_WhenMenuItemNotFound() {
        // Arrange
        when(menuItemRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(MenuItemIdNotFoundException.class, () ->
            menuItemService.updateName(1L, "New Name"));

        verify(menuItemRepository).findById(1L);
        verify(menuItemRepository, never()).save(any());
    }
}
