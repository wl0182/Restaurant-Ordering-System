package com.wassimlagnaoui.RestaurantOrder.Exception;

public class MenuItemNotFoundException extends RuntimeException {
    public MenuItemNotFoundException() {
        super("Menu Item not found");
    }
    public MenuItemNotFoundException(String message) {
        super(message);
    }
}

