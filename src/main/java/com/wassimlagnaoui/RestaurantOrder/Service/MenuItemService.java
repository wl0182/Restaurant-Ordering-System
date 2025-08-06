package com.wassimlagnaoui.RestaurantOrder.Service;


import com.wassimlagnaoui.RestaurantOrder.DTO.MenuItemRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.MenuItemResponse;
import com.wassimlagnaoui.RestaurantOrder.Mapper.MenuItemMapper;
import com.wassimlagnaoui.RestaurantOrder.Repository.MenuItemRepository;
import com.wassimlagnaoui.RestaurantOrder.model.MenuItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Service class for managing menu items in the restaurant order system.
 * Handles CRUD operations and availability toggling for menu items.
 */
@Slf4j
@Service
public class MenuItemService {

    // Dependency Injection for MenuItemRepository
    private final MenuItemRepository menuItemRepository;

    /**
     * Constructor for MenuItemService.
     * @param menuItemRepository the repository for menu items
     */
    public MenuItemService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * Finds a menu item by its ID.
     * @param id the menu item ID
     * @return MenuItemResponse with menu item details
     */
    public MenuItemResponse findById(Long id) {


        MenuItem menuItem = menuItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Menu item with id " + id + " not found"));
        return MenuItemMapper.fromMenuItem(menuItem);

    }

    /**
     * Retrieves all menu items.
     * @return List of MenuItemResponse for all menu items
     */
    public List<MenuItemResponse> findAll() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        List<MenuItemResponse> menuItemResponses = new ArrayList<>();
        for (MenuItem menuItem : menuItems) {
            MenuItemResponse menuItemResponse = MenuItemMapper.fromMenuItem(menuItem);
            menuItemResponses.add(menuItemResponse);
        }
        return menuItemResponses;
    }


    /**
     * Creates a new menu item.
     * @param menuItemRequest the request DTO for menu item creation
     * @return MenuItemResponse with created menu item details
     */
    public MenuItemResponse createMenuItem(MenuItemRequest menuItemRequest) {
        MenuItemResponse menuItemResponse = new MenuItemResponse();

        MenuItem menuItem = MenuItemMapper.toMenuItem(menuItemRequest);
        this.menuItemRepository.save(menuItem);



        return MenuItemMapper.fromMenuItem(menuItem);
    }

    /**
     * Toggles the availability of a menu item by its ID.
     * @param id the menu item ID
     * @return MenuItemResponse with updated availability status
     */
    public MenuItemResponse updateAvailability(Long id){
        MenuItem menuItem = menuItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Menu Item not available"));

        if (menuItem.isAvailable()){
            menuItem.setAvailable(false);
        }
        else {
            menuItem.setAvailable(true);
        }

        menuItemRepository.save(menuItem);

        return MenuItemMapper.fromMenuItem(menuItem);
    }




    /**
     * Retrieves all available menu items.
     * @return List of MenuItemResponse for available menu items
     */
    public List<MenuItemResponse> getAvailableMenuItems() {
        List<MenuItem> menuItems = menuItemRepository.findAll();
        List<MenuItemResponse> menuItemResponses = new ArrayList<>();

        for (MenuItem menuItem : menuItems) {
            if (menuItem.isAvailable()) {
                MenuItemResponse menuItemResponse = MenuItemMapper.fromMenuItem(menuItem);
                menuItemResponses.add(menuItemResponse);
            }
        }
        return menuItemResponses;
    }




}
