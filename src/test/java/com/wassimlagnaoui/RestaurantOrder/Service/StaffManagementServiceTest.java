package com.wassimlagnaoui.RestaurantOrder.Service;

import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.AddStaffDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.StaffInfoDTO;
import com.wassimlagnaoui.RestaurantOrder.DTO.Response.staffAddedResponse;
import com.wassimlagnaoui.RestaurantOrder.Repository.StaffRepository;
import com.wassimlagnaoui.RestaurantOrder.model.Staff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StaffManagementServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private StaffManagementService staffManagementService;

    private AddStaffDTO addStaffDTO;
    private Staff testStaff;
    private Staff savedStaff;

    @BeforeEach
    void setUp() {
        // Setup AddStaffDTO for testing
        addStaffDTO = new AddStaffDTO();
        addStaffDTO.setFirstName("John");
        addStaffDTO.setLastName("Doe");
        addStaffDTO.setEmail("john.doe@restaurant.com");
        addStaffDTO.setEmployeeId(1001L);
        addStaffDTO.setRole("WAITER");

        // Setup test staff object (before saving)
        testStaff = new Staff();
        testStaff.setFirstName("John");
        testStaff.setLastName("Doe");
        testStaff.setEmail("john.doe@restaurant.com");
        testStaff.setEmployeeId(1001L);
        testStaff.setRole("WAITER");

        // Setup saved staff object (after saving - with ID)
        savedStaff = new Staff();
        savedStaff.setId(1L);
        savedStaff.setFirstName("John");
        savedStaff.setLastName("Doe");
        savedStaff.setEmail("john.doe@restaurant.com");
        savedStaff.setEmployeeId(1001L);
        savedStaff.setRole("WAITER");
    }

    @Test
    void addStaff_ShouldReturnStaffAddedResponse_WhenValidStaffDTO() {
        // Arrange
        when(staffRepository.save(any(Staff.class))).thenReturn(savedStaff);

        // Act
        staffAddedResponse response = staffManagementService.addStaff(addStaffDTO);

        // Assert
        assertNotNull(response);
        assertEquals("Staff member added successfully", response.getMessage());
        assertEquals(1001L, response.getStaffId());

        // Verify that staff was saved with correct properties
        verify(staffRepository).save(argThat(staff ->
            staff.getFirstName().equals("John") &&
            staff.getLastName().equals("Doe") &&
            staff.getEmail().equals("john.doe@restaurant.com") &&
            staff.getEmployeeId().equals(1001L) &&
            staff.getRole().equals("WAITER")
        ));
    }

    @Test
    void addStaff_ShouldCreateStaffWithAllProperties_WhenCalled() {
        // Arrange
        when(staffRepository.save(any(Staff.class))).thenReturn(savedStaff);

        // Act
        staffManagementService.addStaff(addStaffDTO);

        // Assert - Verify the staff object passed to save has all correct properties
        verify(staffRepository).save(argThat(staff -> {
            assertEquals("John", staff.getFirstName());
            assertEquals("Doe", staff.getLastName());
            assertEquals("john.doe@restaurant.com", staff.getEmail());
            assertEquals(1001L, staff.getEmployeeId());
            assertEquals("WAITER", staff.getRole());
            return true;
        }));
    }

    @Test
    void addStaff_ShouldReturnCorrectEmployeeId_WhenStaffSaved() {
        // Arrange
        Long expectedEmployeeId = 2002L;
        AddStaffDTO managerDTO = new AddStaffDTO();
        managerDTO.setFirstName("Jane");
        managerDTO.setLastName("Smith");
        managerDTO.setEmail("jane.smith@restaurant.com");
        managerDTO.setEmployeeId(expectedEmployeeId);
        managerDTO.setRole("MANAGER");

        Staff savedManager = new Staff();
        savedManager.setId(2L);
        savedManager.setFirstName("Jane");
        savedManager.setLastName("Smith");
        savedManager.setEmail("jane.smith@restaurant.com");
        savedManager.setEmployeeId(expectedEmployeeId);
        savedManager.setRole("MANAGER");

        when(staffRepository.save(any(Staff.class))).thenReturn(savedManager);

        // Act
        staffAddedResponse response = staffManagementService.addStaff(managerDTO);

        // Assert
        assertEquals(expectedEmployeeId, response.getStaffId());
        assertEquals("Staff member added successfully", response.getMessage());
    }

    @Test
    void getAllStaff_ShouldReturnListOfStaffInfoDTO_WhenStaffExists() {
        // Arrange
        Staff staff1 = new Staff();
        staff1.setId(1L);
        staff1.setFirstName("John");
        staff1.setLastName("Doe");
        staff1.setEmail("john.doe@restaurant.com");
        staff1.setEmployeeId(1001L);
        staff1.setRole("WAITER");

        Staff staff2 = new Staff();
        staff2.setId(2L);
        staff2.setFirstName("Jane");
        staff2.setLastName("Smith");
        staff2.setEmail("jane.smith@restaurant.com");
        staff2.setEmployeeId(2002L);
        staff2.setRole("MANAGER");

        List<Staff> staffList = Arrays.asList(staff1, staff2);
        when(staffRepository.findAll()).thenReturn(staffList);

        // Act
        List<StaffInfoDTO> result = staffManagementService.getAllStaff();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        // Verify first staff member
        StaffInfoDTO firstStaff = result.get(0);
        assertEquals("John", firstStaff.getFirstName());
        assertEquals("Doe", firstStaff.getLastName());
        assertEquals("john.doe@restaurant.com", firstStaff.getEmail());
        assertEquals(1001L, firstStaff.getEmployeeId());
        assertEquals("WAITER", firstStaff.getRole());

        // Verify second staff member
        StaffInfoDTO secondStaff = result.get(1);
        assertEquals("Jane", secondStaff.getFirstName());
        assertEquals("Smith", secondStaff.getLastName());
        assertEquals("jane.smith@restaurant.com", secondStaff.getEmail());
        assertEquals(2002L, secondStaff.getEmployeeId());
        assertEquals("MANAGER", secondStaff.getRole());

        verify(staffRepository).findAll();
    }

    @Test
    void getAllStaff_ShouldReturnEmptyList_WhenNoStaffExists() {
        // Arrange
        when(staffRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<StaffInfoDTO> result = staffManagementService.getAllStaff();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(staffRepository).findAll();
    }

    @Test
    void getAllStaff_ShouldMapAllFieldsCorrectly_WhenStaffExists() {
        // Arrange
        Staff testStaffMember = new Staff();
        testStaffMember.setId(999L);
        testStaffMember.setFirstName("Alice");
        testStaffMember.setLastName("Johnson");
        testStaffMember.setEmail("alice.johnson@restaurant.com");
        testStaffMember.setEmployeeId(3003L);
        testStaffMember.setRole("CHEF");

        when(staffRepository.findAll()).thenReturn(Arrays.asList(testStaffMember));

        // Act
        List<StaffInfoDTO> result = staffManagementService.getAllStaff();

        // Assert
        assertEquals(1, result.size());

        StaffInfoDTO staffInfo = result.get(0);
        assertAll("Verify all fields are mapped correctly",
            () -> assertEquals("Alice", staffInfo.getFirstName()),
            () -> assertEquals("Johnson", staffInfo.getLastName()),
            () -> assertEquals("alice.johnson@restaurant.com", staffInfo.getEmail()),
            () -> assertEquals(3003L, staffInfo.getEmployeeId()),
            () -> assertEquals("CHEF", staffInfo.getRole())
        );
    }

    @Test
    void addStaff_ShouldHandleDifferentRoles_WhenCalled() {
        // Arrange
        String[] roles = {"WAITER", "CHEF", "MANAGER", "HOST", "BARTENDER"};

        for (int i = 0; i < roles.length; i++) {
            String role = roles[i];
            AddStaffDTO staffDTO = new AddStaffDTO();
            staffDTO.setFirstName("Employee" + i);
            staffDTO.setLastName("Test" + i);
            staffDTO.setEmail("employee" + i + "@restaurant.com");
            staffDTO.setEmployeeId((long) (1000 + i));
            staffDTO.setRole(role);

            Staff savedStaff = new Staff();
            savedStaff.setId((long) i);
            savedStaff.setFirstName("Employee" + i);
            savedStaff.setLastName("Test" + i);
            savedStaff.setEmail("employee" + i + "@restaurant.com");
            savedStaff.setEmployeeId((long) (1000 + i));
            savedStaff.setRole(role);

            when(staffRepository.save(any(Staff.class))).thenReturn(savedStaff);

            // Act
            staffAddedResponse response = staffManagementService.addStaff(staffDTO);

            // Assert
            assertEquals("Staff member added successfully", response.getMessage());
            assertEquals((long) (1000 + i), response.getStaffId());
        }

        // Verify save was called for each role
        verify(staffRepository, times(roles.length)).save(any(Staff.class));
    }

    @Test
    void getAllStaff_ShouldPreserveOrderFromRepository_WhenCalled() {
        // Arrange
        Staff staff1 = createStaff(1L, "Alice", "Anderson", "alice@test.com", 1001L, "WAITER");
        Staff staff2 = createStaff(2L, "Bob", "Brown", "bob@test.com", 1002L, "CHEF");
        Staff staff3 = createStaff(3L, "Charlie", "Clark", "charlie@test.com", 1003L, "MANAGER");

        List<Staff> orderedStaffList = Arrays.asList(staff1, staff2, staff3);
        when(staffRepository.findAll()).thenReturn(orderedStaffList);

        // Act
        List<StaffInfoDTO> result = staffManagementService.getAllStaff();

        // Assert
        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0).getFirstName());
        assertEquals("Bob", result.get(1).getFirstName());
        assertEquals("Charlie", result.get(2).getFirstName());
    }

    // Helper method to create staff objects for testing
    private Staff createStaff(Long id, String firstName, String lastName, String email, Long employeeId, String role) {
        Staff staff = new Staff();
        staff.setId(id);
        staff.setFirstName(firstName);
        staff.setLastName(lastName);
        staff.setEmail(email);
        staff.setEmployeeId(employeeId);
        staff.setRole(role);
        return staff;
    }
}
