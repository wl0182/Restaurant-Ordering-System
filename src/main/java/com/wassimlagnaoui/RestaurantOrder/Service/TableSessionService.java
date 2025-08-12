package com.wassimlagnaoui.RestaurantOrder.Service;


import com.wassimlagnaoui.RestaurantOrder.DTO.ItemSummaryDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.StartSessionDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.EndSessionResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.SessionSummary;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.StartSessionResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.TableSessionResponse;
import com.wassimlagnaoui.RestaurantOrder.Exception.ActiveSessionExistsException;
import com.wassimlagnaoui.RestaurantOrder.Exception.NoActiveSessionsFoundExceptions;
import com.wassimlagnaoui.RestaurantOrder.Exception.TableSessionNotFound;
import com.wassimlagnaoui.RestaurantOrder.Exception.NoActiveTableSessionFoundException;
import com.wassimlagnaoui.RestaurantOrder.Mapper.TableSessionMapper;
import com.wassimlagnaoui.RestaurantOrder.Repository.TableSessionRepository;
import com.wassimlagnaoui.RestaurantOrder.model.TableSession;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service class for managing table sessions in the restaurant order system.
 * Handles session lifecycle, querying, and summary operations.
 */
@Service
public class TableSessionService {

    private final TableSessionRepository tableSessionRepository;

    /**
     * Constructor for TableSessionService.
     * @param tableSessionRepository the repository for table sessions
     */
    public TableSessionService(TableSessionRepository tableSessionRepository) {
        this.tableSessionRepository = tableSessionRepository;
    }

    /**
     * Starts a new session for a table if no active session exists.
     * @param startSessionDTO DTO containing table number to start session for
     * @return StartSessionResponse containing session details
     */
    public StartSessionResponse startSession(StartSessionDTO startSessionDTO) {
      // check if the session is active for this table
        Optional<TableSession> tableSession = tableSessionRepository.findActiveTableSessionByTableNumber(startSessionDTO.getTableNumber());
       if (tableSession.isPresent()){
           throw new ActiveSessionExistsException("There is already an active session for this table: " + startSessionDTO.getTableNumber());
       }

       TableSession newTableSession = new TableSession();
       newTableSession.setSessionStart(LocalDateTime.now());
       newTableSession.setTableNumber(startSessionDTO.getTableNumber());


       tableSessionRepository.save(newTableSession);

       // form the response object
        StartSessionResponse response = new StartSessionResponse();
        response.setId(newTableSession.getId());
        response.setStartTime(LocalDateTime.now());
        response.setTableNumber(newTableSession.getTableNumber());
        response.setActive(true);





       return response;

    }

    /**
     * Retrieves a session by its ID.
     * @param id the session ID
     * @return TableSessionResponse containing session details
     */
    public TableSessionResponse getSessionById(Long id) {
        TableSession tableSession = tableSessionRepository.findById(id).orElseThrow(TableSessionNotFound::new);
        return TableSessionMapper.fromTableSession(tableSession);

    }

    /**
     * Retrieves all currently active table sessions.
     * @return List of TableSessionResponse for active sessions
     */
    public List<TableSessionResponse> getActiveTableSessions() {
        List<TableSession> tableSessions = tableSessionRepository.findActiveTableSession();
        if (tableSessions.isEmpty()) {
            throw new NoActiveSessionsFoundExceptions("No active table sessions found");
        }

        List<TableSessionResponse> tableSessionResponses = new ArrayList<>();
        for (TableSession tableSession : tableSessions) {
            tableSessionResponses.add(TableSessionMapper.fromTableSession(tableSession));
        }
        return tableSessionResponses;
    }

    /**
     * Ends the active session for a given table number.
     * @param tableNumber the table number
     * @return EndSessionResponse with end session details
     */
    public EndSessionResponse endSession(String tableNumber) {
        TableSession tableSession = tableSessionRepository.findActiveTableSessionByTableNumber(tableNumber).orElseThrow(() -> new NoActiveTableSessionFoundException());
        tableSession.setSessionEnd(LocalDateTime.now());
        tableSessionRepository.save(tableSession);

        EndSessionResponse response = new EndSessionResponse();
        response.setMessage("Session ended successfully");
        response.setEndTime(LocalDateTime.now());
        response.setStartTime(tableSession.getSessionStart());
        response.setTableNumber(tableSession.getTableNumber());




        return response;
    }

    /**
     * Gets a summary of all items ordered in a session.
     * @param id the session ID
     * @return List of ItemSummaryDTO summarizing ordered items
     */
    public List<ItemSummaryDTO> getItemSummaryForSession(Long id) {
        TableSession tableSession = tableSessionRepository.findById(id).orElseThrow(TableSessionNotFound::new);

        return tableSession.getOrders().stream()
                .flatMap(order -> order.getItems().stream())
                .map(orderItem -> {

                    ItemSummaryDTO itemSummaryDTO = new ItemSummaryDTO();
                    itemSummaryDTO.setOrderId(orderItem.getId());
                    itemSummaryDTO.setItemId(orderItem.getId());
                    itemSummaryDTO.setItemName(orderItem.getMenuItem().getName());
                    itemSummaryDTO.setTotalQuantity(orderItem.getQuantity());
                    itemSummaryDTO.setServed(orderItem.getServed());
                    itemSummaryDTO.setTotalPrice(orderItem.getMenuItem().getPrice()* orderItem.getQuantity());
                    return itemSummaryDTO;
                }).collect(Collectors.toUnmodifiableList());

    }

    /**
     * Retrieves the names of all items ordered in a session.
     * @param id the session ID
     * @return List of item names
     */
    public List<String> getAllOrderedItemNames(Long id) {
        TableSession tableSession = tableSessionRepository.findById(id).orElseThrow(TableSessionNotFound::new);

        return tableSession.getOrders().stream()
                .flatMap(order -> order.getItems().stream() )
                .map(orderItem -> orderItem.getMenuItem().getName())
                .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Finds the active session for a specific table number.
     * @param tableNumber the table number
     * @return TableSessionResponse with session details
     */
    public TableSessionResponse findActiveSessionByTableNumber(String tableNumber) {
        TableSession tableSession = tableSessionRepository.findActiveTableSessionByTableNumber(tableNumber).orElseThrow(NoActiveTableSessionFoundException::new);


        TableSessionResponse tableSessionResponse = new TableSessionResponse();
        tableSessionResponse.setId(tableSession.getId());
        tableSessionResponse.setTableNumber(tableSession.getTableNumber());
        tableSessionResponse.setSessionStartTime(tableSession.getSessionStart());

        return tableSessionResponse;

    }

    /**
     * This method is used to get the session summary for checkout.
     * It returns a SessionSummary object containing the session details, total orders, total items ordered, and total amount.
     *
     * @param sessionId the ID of the session
     * @return SessionSummary object containing session details
     */

    public SessionSummary getSessionSummaryForCheckout(Long sessionId){
        TableSession tableSession = tableSessionRepository.findById(sessionId).orElseThrow(TableSessionNotFound::new);

        SessionSummary sessionSummary = new SessionSummary();
        sessionSummary.setSessionId(tableSession.getId());
        sessionSummary.setTableNumber(tableSession.getTableNumber());
        sessionSummary.setTotalOrders(tableSession.getOrders().stream().count());

        sessionSummary.setTotalItemOrdered(tableSession.getOrders().stream().flatMap(order -> order.getItems().stream()).count());

        Double totalAmount = 0.0;

        List<ItemSummaryDTO> itemsList = getItemSummaryForSession(tableSession.getId());

        for (ItemSummaryDTO item : itemsList){
            totalAmount = totalAmount+  item.getTotalPrice();
        }

        sessionSummary.setTotalAmont(totalAmount);
        sessionSummary.setItems(itemsList);


        return sessionSummary;

    }
}
