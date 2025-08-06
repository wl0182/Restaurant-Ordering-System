package com.wassimlagnaoui.RestaurantOrder.Repository;

import com.wassimlagnaoui.RestaurantOrder.model.TableSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TableSessionRepository extends JpaRepository<TableSession,Long> {

    @Query("select t from TableSession t where t.tableNumber = :tableNumber and t.sessionEnd is null ")
    Optional<TableSession> findActiveTableSessionByTableNumber(@Param("tableNumber") String tableNumber);

    @Query("select tb from TableSession tb where tb.sessionEnd is null")
    List<TableSession> findActiveTableSession();

    @Query("select tb from TableSession tb where tb.id=:sessionId and tb.sessionEnd is null ")
    Optional<TableSession> findActiveTableSessionById(@Param("sessionId") long sessionId);


    Long id(Long id);
}
