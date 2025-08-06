package com.wassimlagnaoui.RestaurantOrder.Mapper;

import com.wassimlagnaoui.RestaurantOrder.DTO.MenuItemRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.MenuItemResponse;
import com.wassimlagnaoui.RestaurantOrder.model.MenuItem;
import org.springframework.stereotype.Component;

@Component
public class MenuItemMapper {
    public static MenuItemResponse fromMenuItem(MenuItem menuItem){
        MenuItemResponse menuItemResponse = new MenuItemResponse();
        menuItemResponse.setAvailable(menuItem.isAvailable());
        menuItemResponse.setCategory(menuItem.getCategory());
        menuItemResponse.setName(menuItem.getName());
        menuItemResponse.setPrice(menuItem.getPrice());
        menuItemResponse.setImageUrl(menuItem.getImageUrl());
        menuItemResponse.setId(menuItem.getId());

        return menuItemResponse;
    }
    public static MenuItem toMenuItem(MenuItemRequest menuItemRequest ){
        MenuItem menuItem = new MenuItem();
        menuItem.setName(menuItemRequest.getName());
        menuItem.setPrice(menuItemRequest.getPrice());
        menuItem.setImageUrl(menuItemRequest.getImageUrl());
        menuItem.setCategory(menuItemRequest.getCategory());
        menuItem.setAvailable(menuItemRequest.isAvailable());
        menuItem.setDescription(menuItemRequest.getDescription());
        return menuItem;
    }


}
