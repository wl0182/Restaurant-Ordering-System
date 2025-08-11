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


    // find all table sessions by date formatted as "yyyy-MM-dd"
    @Query("SELECT DISTINCT DATE(t.sessionStart) FROM TableSession t")
    List<String> findAllDates();

    // find all table sessions by date formatted as "yyyy-MM-dd"
    @Query("SELECT t FROM TableSession t WHERE DATE(t.sessionStart) = :date")
    List<TableSession> findByDate(@Param("date") String date);

    // find sum of order totals
    @Query(value = "SELECT AVG(session_total) FROM (SELECT SUM(o.total) as session_total FROM table_session ts JOIN orders o ON ts.id = o.table_session_id GROUP BY ts.id) as session_totals", nativeQuery = true)
    Double findAverageTotalByTableSession();

    @Query(nativeQuery = true,value="SELECT\n" +
            "    DATE(session_totals.session_start) AS session_date,\n" +
            "    AVG(session_totals.session_total) AS avg_total\n" +
            "FROM (\n" +
            "    SELECT\n" +
            "        ts.id,\n" +
            "        ts.session_start,\n" +
            "        COALESCE(SUM(o.total), 0) AS session_total\n" +
            "    FROM\n" +
            "        table_session ts\n" +
            "    LEFT JOIN\n" +
            "        orders o ON ts.id = o.table_session_id\n" +
            "    GROUP BY\n" +
            "        ts.id, ts.session_start\n" +
            ") AS session_totals\n" +
            "GROUP BY\n" +
            "    session_date\n" +
            "ORDER BY\n" +
            "    session_date;")
    List<Object[]> findAverageTotalByTableSessionGroupedByDate();






}
