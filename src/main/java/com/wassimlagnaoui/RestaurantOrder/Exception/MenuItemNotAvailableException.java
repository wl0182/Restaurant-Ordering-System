package com.wassimlagnaoui.RestaurantOrder.Exception;

public class MenuItemNotAvailableException extends RuntimeException {
    public MenuItemNotAvailableException() {
        super("Menu Item not available");
    }
}

