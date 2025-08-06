package com.wassimlagnaoui.RestaurantOrder.Repository;

import com.wassimlagnaoui.RestaurantOrder.model.Order;
import com.wassimlagnaoui.RestaurantOrder.model.OrderItem;
import com.wassimlagnaoui.RestaurantOrder.model.TableSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByTableSession(TableSession tableSession);

    @Query("select o from Order o  where o.tableSession.id =:sessionId and o.status<>'SERVED' ")
    List<Order> findUnservedOrderByTableSession(@Param("sessionId") Long sessionId);


    @Query("select o from Order o  where o.tableSession.id =:sessionId and o.status='SERVED' ")
    List<Order> findServedOrderByTableSession(@Param("sessionId") Long sessionId);

    @Query("select o from Order o where o.status<>'SERVED' ")
    List<Order> findAllUnservedOrder();

    @Query("select o from Order o  where o.tableSession.id =:sessionId")
    List<Order> findByTableSession(Long sessionId);

    @Query("select o.items from Order o  where o.tableSession.id =:sessionId ")
    List<OrderItem> findItemsByTableSession(@Param("sessionId") Long sessionId);
}
