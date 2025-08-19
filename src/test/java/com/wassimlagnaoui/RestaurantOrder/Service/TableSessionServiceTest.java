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
import com.wassimlagnaoui.RestaurantOrder.Repository.TableSessionRepository;
import com.wassimlagnaoui.RestaurantOrder.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TableSessionServiceTest {

    @Mock
    private TableSessionRepository tableSessionRepository;

    @InjectMocks
    private TableSessionService tableSessionService;

    private TableSession testTableSession;
    private StartSessionDTO startSessionDTO;
    private MenuItem testMenuItem;
    private Order testOrder;
    private OrderItem testOrderItem;

    @BeforeEach
    void setUp() {
        // Setup test table session
        testTableSession = new TableSession();
        testTableSession.setId(1L);
        testTableSession.setTableNumber("T1");
        testTableSession.setSessionStart(LocalDateTime.of(2025, 8, 19, 12, 0));


        // Setup start session DTO
        startSessionDTO = new StartSessionDTO();
        startSessionDTO.setTableNumber("T1");

        // Setup menu item
        testMenuItem = new MenuItem();
        testMenuItem.setId(1L);
        testMenuItem.setName("Burger");
        testMenuItem.setPrice(12.99);

        // Setup order
        testOrder = new Order();
        testOrder.setId(1L);
        testOrder.setTableSession(testTableSession);
        testOrder.setOrderDate(LocalDateTime.now());
        testOrder.setStatus(OrderStatus.PLACED.name());

        // Setup order item
        testOrderItem = new OrderItem();
        testOrderItem.setId(1L);
        testOrderItem.setMenuItem(testMenuItem);
        testOrderItem.setQuantity(2);
        testOrderItem.setOrder(testOrder);
        testOrderItem.setServed(false);

        // Setup relationships
        testOrder.setItems(Arrays.asList(testOrderItem));
        testTableSession.setOrders(Arrays.asList(testOrder));
    }

    @Test
    void startSession_ShouldReturnStartSessionResponse_WhenNoActiveSessionExists() {
        // Arrange
        TableSession savedSession = new TableSession();
        savedSession.setId(1L);
        savedSession.setTableNumber("T1");
        savedSession.setSessionStart(LocalDateTime.now());

        when(tableSessionRepository.findActiveTableSessionByTableNumber("T1")).thenReturn(Optional.empty());
        when(tableSessionRepository.save(any(TableSession.class))).thenAnswer(invocation -> {
            TableSession session = invocation.getArgument(0);
            session.setId(1L); // Set the ID to simulate database save
            return session;
        });

        // Act
        StartSessionResponse response = tableSessionService.startSession(startSessionDTO);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("T1", response.getTableNumber());

        assertNotNull(response.getStartTime());

        verify(tableSessionRepository).findActiveTableSessionByTableNumber("T1");
        verify(tableSessionRepository).save(any(TableSession.class));
    }

    @Test
    void startSession_ShouldThrowException_WhenActiveSessionExists() {
        // Arrange
        when(tableSessionRepository.findActiveTableSessionByTableNumber("T1")).thenReturn(Optional.of(testTableSession));

        // Act & Assert
        ActiveSessionExistsException exception = assertThrows(ActiveSessionExistsException.class,
            () -> tableSessionService.startSession(startSessionDTO));

        assertTrue(exception.getMessage().contains("There is already an active session for this table: T1"));

        verify(tableSessionRepository).findActiveTableSessionByTableNumber("T1");
        verify(tableSessionRepository, never()).save(any());
    }

    @Test
    void getSessionById_ShouldReturnTableSessionResponse_WhenSessionExists() {
        // Arrange
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.of(testTableSession));

        // Act
        TableSessionResponse response = tableSessionService.getSessionById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("T1", response.getTableNumber());
        assertEquals(testTableSession.getSessionStart(), response.getSessionStartTime());

        verify(tableSessionRepository).findById(1L);
    }

    @Test
    void getSessionById_ShouldThrowException_WhenSessionNotFound() {
        // Arrange
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TableSessionNotFound.class, () -> tableSessionService.getSessionById(1L));

        verify(tableSessionRepository).findById(1L);
    }

    @Test
    void getActiveTableSessions_ShouldReturnListOfActiveSessions_WhenSessionsExist() {
        // Arrange
        TableSession session2 = new TableSession();
        session2.setId(2L);
        session2.setTableNumber("T2");
        session2.setSessionStart(LocalDateTime.now());

        List<TableSession> activeSessions = Arrays.asList(testTableSession, session2);
        when(tableSessionRepository.findActiveTableSession()).thenReturn(activeSessions);

        // Act
        List<TableSessionResponse> responses = tableSessionService.getActiveTableSessions();

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        assertEquals("T1", responses.get(0).getTableNumber());
        assertEquals("T2", responses.get(1).getTableNumber());

        verify(tableSessionRepository).findActiveTableSession();
    }

    @Test
    void getActiveTableSessions_ShouldThrowException_WhenNoActiveSessionsFound() {
        // Arrange
        when(tableSessionRepository.findActiveTableSession()).thenReturn(Arrays.asList());

        // Act & Assert
        NoActiveSessionsFoundExceptions exception = assertThrows(NoActiveSessionsFoundExceptions.class,
            () -> tableSessionService.getActiveTableSessions());

        assertEquals("No active table sessions found", exception.getMessage());

        verify(tableSessionRepository).findActiveTableSession();
    }

    @Test
    void endSession_ShouldReturnEndSessionResponse_WhenActiveSessionExists() {
        // Arrange
        when(tableSessionRepository.findActiveTableSessionByTableNumber("T1")).thenReturn(Optional.of(testTableSession));
        when(tableSessionRepository.save(any(TableSession.class))).thenReturn(testTableSession);

        // Act
        EndSessionResponse response = tableSessionService.endSession("T1");

        // Assert
        assertNotNull(response);
        assertEquals("Session ended successfully", response.getMessage());
        assertEquals("T1", response.getTableNumber());
        assertEquals(testTableSession.getSessionStart(), response.getStartTime());
        assertNotNull(response.getEndTime());
        assertNotNull(testTableSession.getSessionEnd());

        verify(tableSessionRepository).findActiveTableSessionByTableNumber("T1");
        verify(tableSessionRepository).save(testTableSession);
    }

    @Test
    void endSession_ShouldThrowException_WhenNoActiveSessionFound() {
        // Arrange
        when(tableSessionRepository.findActiveTableSessionByTableNumber("T1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoActiveTableSessionFoundException.class, () -> tableSessionService.endSession("T1"));

        verify(tableSessionRepository).findActiveTableSessionByTableNumber("T1");
        verify(tableSessionRepository, never()).save(any());
    }

    @Test
    void getItemSummaryForSession_ShouldReturnItemSummaryList_WhenSessionExists() {
        // Arrange
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.of(testTableSession));

        // Act
        List<ItemSummaryDTO> result = tableSessionService.getItemSummaryForSession(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        ItemSummaryDTO itemSummary = result.get(0);
        assertEquals(1L, itemSummary.getOrderId());
        assertEquals(1L, itemSummary.getItemId());
        assertEquals("Burger", itemSummary.getItemName());
        assertEquals(2, itemSummary.getTotalQuantity());
        assertFalse(itemSummary.getServed());
        assertEquals(25.98, itemSummary.getTotalPrice(), 0.01); // 2 * 12.99

        verify(tableSessionRepository).findById(1L);
    }

    @Test
    void getItemSummaryForSession_ShouldThrowException_WhenSessionNotFound() {
        // Arrange
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TableSessionNotFound.class, () -> tableSessionService.getItemSummaryForSession(1L));

        verify(tableSessionRepository).findById(1L);
    }

    @Test
    void getAllOrderedItemNames_ShouldReturnItemNames_WhenSessionExists() {
        // Arrange
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.of(testTableSession));

        // Act
        List<String> result = tableSessionService.getAllOrderedItemNames(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Burger", result.get(0));

        verify(tableSessionRepository).findById(1L);
    }

    @Test
    void getAllOrderedItemNames_ShouldThrowException_WhenSessionNotFound() {
        // Arrange
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TableSessionNotFound.class, () -> tableSessionService.getAllOrderedItemNames(1L));

        verify(tableSessionRepository).findById(1L);
    }

    @Test
    void findActiveSessionByTableNumber_ShouldReturnTableSessionResponse_WhenActiveSessionExists() {
        // Arrange
        when(tableSessionRepository.findActiveTableSessionByTableNumber("T1")).thenReturn(Optional.of(testTableSession));

        // Act
        TableSessionResponse response = tableSessionService.findActiveSessionByTableNumber("T1");

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("T1", response.getTableNumber());
        assertEquals(testTableSession.getSessionStart(), response.getSessionStartTime());

        verify(tableSessionRepository).findActiveTableSessionByTableNumber("T1");
    }

    @Test
    void findActiveSessionByTableNumber_ShouldThrowException_WhenNoActiveSessionFound() {
        // Arrange
        when(tableSessionRepository.findActiveTableSessionByTableNumber("T1")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoActiveTableSessionFoundException.class,
            () -> tableSessionService.findActiveSessionByTableNumber("T1"));

        verify(tableSessionRepository).findActiveTableSessionByTableNumber("T1");
    }

    @Test
    void getSessionSummaryForCheckout_ShouldReturnSessionSummary_WhenSessionExists() {
        // Arrange
        // Mock findById to be called multiple times (once for main method, once for getItemSummaryForSession)
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.of(testTableSession));

        // Act
        SessionSummary result = tableSessionService.getSessionSummaryForCheckout(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getSessionId());
        assertEquals("T1", result.getTableNumber());
        assertEquals(1L, result.getTotalOrders());
        assertEquals(1L, result.getTotalItemOrdered());
        assertEquals(25.98, result.getTotalAmont(), 0.01); // 2 * 12.99
        assertNotNull(result.getItems());
        assertEquals(1, result.getItems().size());

        // The method calls findById twice: once in getSessionSummaryForCheckout and once in getItemSummaryForSession
        verify(tableSessionRepository, times(2)).findById(1L);
    }

    @Test
    void getSessionSummaryForCheckout_ShouldThrowException_WhenSessionNotFound() {
        // Arrange
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TableSessionNotFound.class,
            () -> tableSessionService.getSessionSummaryForCheckout(1L));

        verify(tableSessionRepository).findById(1L);
    }

    @Test
    void getAllSessionByDate_ShouldReturnSessionSummaryList_WhenSessionsExist() {
        // Arrange
        String testDate = "2025-08-19";
        when(tableSessionRepository.findSessionsByDate(testDate)).thenReturn(Arrays.asList(testTableSession));
        // Mock findById for the internal call to getItemSummaryForSession
        when(tableSessionRepository.findById(1L)).thenReturn(Optional.of(testTableSession));

        // Act
        List<SessionSummary> result = tableSessionService.getAllSessionByDate(testDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        SessionSummary sessionSummary = result.get(0);
        assertEquals(1L, sessionSummary.getSessionId());
        assertEquals("T1", sessionSummary.getTableNumber());
        assertEquals(1L, sessionSummary.getTotalOrders());
        assertEquals(1L, sessionSummary.getTotalItemOrdered());
        assertEquals(25.98, sessionSummary.getTotalAmont(), 0.01);

        verify(tableSessionRepository).findSessionsByDate(testDate);
        // Verify findById is called for getItemSummaryForSession
        verify(tableSessionRepository).findById(1L);
    }

    @Test
    void getAllSessionByDate_ShouldThrowException_WhenNoSessionsFoundForDate() {
        // Arrange
        String testDate = "2025-08-19";
        when(tableSessionRepository.findSessionsByDate(testDate)).thenReturn(Arrays.asList());

        // Act & Assert
        assertThrows(TableSessionNotFound.class,
            () -> tableSessionService.getAllSessionByDate(testDate));

        verify(tableSessionRepository).findSessionsByDate(testDate);
    }

    @Test
    void getSessionSummaryForCheckout_ShouldCalculateCorrectTotals_WithMultipleOrdersAndItems() {
        // Arrange
        // Create second order item
        OrderItem orderItem2 = new OrderItem();
        orderItem2.setId(2L);
        orderItem2.setMenuItem(testMenuItem);
        orderItem2.setQuantity(1);
        orderItem2.setOrder(testOrder);
        orderItem2.setServed(true);

        // Create second order
        Order order2 = new Order();
        order2.setId(2L);
        order2.setTableSession(testTableSession);
        order2.setItems(Arrays.asList(orderItem2));

        // Update test data
        testOrder.setItems(Arrays.asList(testOrderItem));
        testTableSession.setOrders(Arrays.asList(testOrder, order2));

        when(tableSessionRepository.findById(1L)).thenReturn(Optional.of(testTableSession));

        // Act
        SessionSummary result = tableSessionService.getSessionSummaryForCheckout(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2L, result.getTotalOrders()); // 2 orders
        assertEquals(2L, result.getTotalItemOrdered()); // 2 order items total
        assertEquals(38.97, result.getTotalAmont(), 0.01); // (2 * 12.99) + (1 * 12.99) = 38.97
    }
}
