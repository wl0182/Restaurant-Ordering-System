package com.wassimlagnaoui.RestaurantOrder.Exception;

public class MenuItemIdNotFoundException extends RuntimeException {
    public MenuItemIdNotFoundException(Long id) {
        super("Menu item with id " + id + " not found");
    }
}

