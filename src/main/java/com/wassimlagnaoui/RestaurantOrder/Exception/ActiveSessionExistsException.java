package com.wassimlagnaoui.RestaurantOrder.Exception;

public class ActiveSessionExistsException extends RuntimeException{
    public ActiveSessionExistsException(String message) {
        super(message);
    }

    public ActiveSessionExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
