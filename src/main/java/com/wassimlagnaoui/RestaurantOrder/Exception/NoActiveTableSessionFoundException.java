package com.wassimlagnaoui.RestaurantOrder.Exception;

public class NoActiveTableSessionFoundException extends RuntimeException {
    public NoActiveTableSessionFoundException() {
        super("No Active TableSession Found");
    }
}
