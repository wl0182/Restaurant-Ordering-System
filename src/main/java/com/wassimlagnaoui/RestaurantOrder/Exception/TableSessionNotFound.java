package com.wassimlagnaoui.RestaurantOrder.Exception;

public class TableSessionNotFound extends RuntimeException{
    public TableSessionNotFound(String s) {
        super(s);
    }
}
