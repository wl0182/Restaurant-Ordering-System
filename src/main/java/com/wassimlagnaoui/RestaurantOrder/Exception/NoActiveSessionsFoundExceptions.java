package com.wassimlagnaoui.RestaurantOrder.Exception;

public class NoActiveSessionsFoundExceptions extends RuntimeException {
    public NoActiveSessionsFoundExceptions(String message) {
        super(message);
    }

    public NoActiveSessionsFoundExceptions(String message, Throwable cause) {
        super(message, cause);
    }

}
