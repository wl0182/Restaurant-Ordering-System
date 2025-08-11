package com.wassimlagnaoui.RestaurantOrder.Repository;

import com.wassimlagnaoui.RestaurantOrder.DTO.Response.PopularItemsResponseDTO;
import com.wassimlagnaoui.RestaurantOrder.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.nio.channels.FileChannel;
import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem,Long> {

    @Query("SELECT m FROM MenuItem m WHERE m.category = :category")
    List<MenuItem> findByCategory(@Param("category") String category);


    // find all available menu items
    @Query("SELECT m FROM MenuItem m WHERE m.available = true")
    List<MenuItem> findAvailableMenuItems();

    // find menu items by name
    @Query("SELECT m FROM MenuItem m WHERE m.name LIKE %:name%")
    List<MenuItem> findByNameContaining(@Param("name") String name);

    // find menu items by category and availability
    @Query("SELECT m FROM MenuItem m WHERE m.category = :category AND m.available = true")
    List<MenuItem> findAvailableMenuItemsByCategory(@Param("category") String category);

    // find menu items by name and availability
    @Query("SELECT m FROM MenuItem m WHERE m.name LIKE %:name% AND m.available = true")
    List<MenuItem> findAvailableMenuItemsByNameContaining(@Param("name") String name);



}
