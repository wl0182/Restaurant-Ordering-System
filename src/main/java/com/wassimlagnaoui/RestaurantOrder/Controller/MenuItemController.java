package com.wassimlagnaoui.RestaurantOrder.Controller;


import com.wassimlagnaoui.RestaurantOrder.DTO.MenuItemRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.MenuItemResponse;
import com.wassimlagnaoui.RestaurantOrder.Service.MenuItemService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpClient;
import java.util.List;

@RestController
@RequestMapping("/api/menu-items")
public class MenuItemController {

    @Autowired
    private final MenuItemService menuItemService;

    public MenuItemController(MenuItemService menuItemService) {
        this.menuItemService = menuItemService;
    }

    @PostMapping("/add")
    public ResponseEntity<MenuItemResponse> createMenuItem(@RequestBody @Valid MenuItemRequest menuItemRequest){
        MenuItemResponse menuItemResponse = menuItemService.createMenuItem(menuItemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemResponse);

    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> getMenuItemByID(@PathVariable Long id){
        MenuItemResponse menuItemResponse = menuItemService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(menuItemResponse);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> getAllMenuItems(){
        List<MenuItemResponse> menuItemResponses = menuItemService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(menuItemResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> updateAvailability(@PathVariable Long id){
        MenuItemResponse menuItemResponse = menuItemService.updateAvailability(id);
        return ResponseEntity.status(HttpStatus.OK).body(menuItemResponse);
    }



    @GetMapping("/available")
    public ResponseEntity<List<MenuItemResponse>> getAvailableMenuItems() {
        List<MenuItemResponse> menuItemResponses = menuItemService.getAvailableMenuItems();
        return ResponseEntity.status(HttpStatus.OK).body(menuItemResponses);
    }



}
