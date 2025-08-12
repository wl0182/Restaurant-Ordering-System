package com.wassimlagnaoui.RestaurantOrder.Exception;

public class NoTableSessionFoundException extends RuntimeException {
    public NoTableSessionFoundException() {
        super("No TableSession found for this table");
    }
}

