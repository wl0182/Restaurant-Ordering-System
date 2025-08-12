package com.wassimlagnaoui.RestaurantOrder.Exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND);
        error.put("error", "Not Found");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TableSessionNotFound.class)
    public ResponseEntity<Map<String,Object>> handleSessionNotFound(TableSessionNotFound ex){
        Map<String, Object > error = new HashMap<>();

        error.put("status",HttpStatus.NOT_FOUND.value());
        error.put("error","Not Found");
        error.put("message", "Table Session Not Found");
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);



    }

    @ExceptionHandler(ActiveSessionExistsException.class)
    public ResponseEntity<Map<String, Object>> handleActiveSessionExists(ActiveSessionExistsException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.CONFLICT.value());
        error.put("error", "Active Session Exists");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoActiveSessionsFoundExceptions.class)
    public ResponseEntity<Map<String, Object>> handleNoActiveSessionsFound(NoActiveSessionsFoundExceptions ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "No Active Sessions Found");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleAllOtherExceptions(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("error", "Internal Server Error");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MenuItemNotAvailableException.class)
    public ResponseEntity<Map<String, Object>> handleMenuItemNotAvailable(MenuItemNotAvailableException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Menu Item Not Available");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MenuItemIdNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMenuItemIdNotFound(MenuItemIdNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Menu Item Not Found");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MenuItemNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleMenuItemNotFound(MenuItemNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Menu Item Not Found");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoTableSessionFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoTableSessionFound(NoTableSessionFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "No Table Session Found");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoActiveTableSessionFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNoActiveTableSessionFound(NoActiveTableSessionFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "No Active Table Session Found");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrderNotFound(OrderNotFoundException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Order Not Found");
        error.put("message", ex.getMessage());
        error.put("timestamp", Instant.now());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }


}
