package com.wassimlagnaoui.RestaurantOrder.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class TableServiceTest {

    @InjectMocks
    private TableService tableService;

    @Test
    void getTableNames_ShouldReturnListOfTableNames() {
        // Act
        List<String> result = tableService.getTableNames();

        // Assert
        assertNotNull(result);
        assertEquals(12, result.size());
        assertEquals("T1", result.get(0));
        assertEquals("T2", result.get(1));
        assertEquals("T3", result.get(2));
        assertEquals("T4", result.get(3));
        assertEquals("T5", result.get(4));
        assertEquals("T6", result.get(5));
        assertEquals("T7", result.get(6));
        assertEquals("T8", result.get(7));
        assertEquals("T9", result.get(8));
        assertEquals("T10", result.get(9));
        assertEquals("T11", result.get(10));
        assertEquals("T12", result.get(11));
    }

    @Test
    void getTableNames_ShouldReturnImmutableList() {
        // Act
        List<String> result = tableService.getTableNames();

        // Assert
        assertNotNull(result);
        // Verify that the returned list contains the expected elements
        assertTrue(result.contains("T1"));
        assertTrue(result.contains("T12"));
        assertFalse(result.contains("T13"));
    }

    @Test
    void getTableNames_ShouldReturnConsistentResults() {
        // Act - Call the method multiple times
        List<String> result1 = tableService.getTableNames();
        List<String> result2 = tableService.getTableNames();

        // Assert - Results should be consistent
        assertEquals(result1, result2);
        assertEquals(result1.size(), result2.size());

        for (int i = 0; i < result1.size(); i++) {
            assertEquals(result1.get(i), result2.get(i));
        }
    }

    @Test
    void getTableNames_ShouldContainExpectedTableFormat() {
        // Act
        List<String> result = tableService.getTableNames();

        // Assert - All table names should follow the "T" + number format
        for (String tableName : result) {
            assertTrue(tableName.startsWith("T"));
            assertTrue(tableName.length() >= 2); // At least "T" + one digit
        }
    }

    @Test
    void getTableNames_ShouldHaveCorrectTableCount() {
        // Act
        List<String> result = tableService.getTableNames();

        // Assert
        assertEquals(12, result.size());

        // Verify specific table names exist
        assertTrue(result.contains("T1"));
        assertTrue(result.contains("T6"));
        assertTrue(result.contains("T12"));

        // Verify non-existent table names don't exist
        assertFalse(result.contains("T0"));
        assertFalse(result.contains("T13"));
        assertFalse(result.contains("Table1"));
    }
}
