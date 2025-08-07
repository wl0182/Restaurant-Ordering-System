package com.wassimlagnaoui.RestaurantOrder.Exception;

public class NoActiveTableSessionFoundException extends RuntimeException {
    public NoActiveTableSessionFoundException() {
        super("No Active tableSession with this ID");
    }
    public NoActiveTableSessionFoundException(String message) {
        super(message);
    }
}

