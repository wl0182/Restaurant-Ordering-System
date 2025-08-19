package com.wassimlagnaoui.RestaurantOrder.Service;

import com.wassimlagnaoui.RestaurantOrder.DTO.AuthResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.AuthRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.RegisterRequestDTO;
import com.wassimlagnaoui.RestaurantOrder.Repository.StaffRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.UserRepository;
import com.wassimlagnaoui.RestaurantOrder.Security.JwtService;
import com.wassimlagnaoui.RestaurantOrder.model.Role;
import com.wassimlagnaoui.RestaurantOrder.model.Staff;
import com.wassimlagnaoui.RestaurantOrder.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private AuthRequest authRequest;
    private RegisterRequestDTO registerRequest;
    private Staff testStaff;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .password("encodedPassword")
                .name("Test User")
                .phone("1234567890")
                .role(Role.ROLE_USER)
                .build();

        authRequest = new AuthRequest();
        authRequest.setEmail("test@example.com");
        authRequest.setPassword("password123");

        registerRequest = new RegisterRequestDTO();
        registerRequest.setEmail("newuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setName("New User");
        registerRequest.setPhoneNumber("9876543210");
        registerRequest.setEmployeeId(1001L); // Changed to Long

        testStaff = new Staff();
        testStaff.setEmployeeId(1001L); // Changed to Long
        testStaff.setFirstName("John");
        testStaff.setLastName("Doe");
    }

    @Test
    void login_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        // Arrange
        String expectedToken = "jwt.token.here";

        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(authRequest.getPassword(), testUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(testUser)).thenReturn(expectedToken);

        // Act
        AuthResponse response = authService.login(authRequest);

        // Assert
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository, times(2)).findByEmail(authRequest.getEmail());
        verify(jwtService).generateToken(testUser);
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        when(userRepository.findByEmail(authRequest.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> authService.login(authRequest));

        // When user doesn't exist, the exception is thrown in the password encoder check
        // before authenticationManager.authenticate() is even called
        verify(userRepository, times(1)).findByEmail(authRequest.getEmail());
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtService);
    }

    @Test
    void register_ShouldReturnAuthResponse_WhenValidRequest() {
        // Arrange
        String expectedToken = "jwt.token.here";
        String encodedPassword = "encodedPassword123";

        when(staffRepository.findByEmployeeId(registerRequest.getEmployeeId())).thenReturn(Optional.of(testStaff));
        when(passwordEncoder.encode(registerRequest.getPassword())).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        when(jwtService.generateToken(any(User.class))).thenReturn(expectedToken);

        // Act
        AuthResponse response = authService.register(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals(expectedToken, response.getToken());

        verify(staffRepository).findByEmployeeId(registerRequest.getEmployeeId());
        verify(passwordEncoder).encode(registerRequest.getPassword());
        verify(userRepository).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenStaffNotFound() {
        // Arrange
        when(staffRepository.findByEmployeeId(registerRequest.getEmployeeId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> authService.register(registerRequest));

        verify(staffRepository).findByEmployeeId(registerRequest.getEmployeeId());
        verifyNoInteractions(passwordEncoder);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(jwtService);
    }

    @Test
    void register_ShouldCreateUserWithCorrectRole() {
        // Arrange
        when(staffRepository.findByEmployeeId(registerRequest.getEmployeeId())).thenReturn(Optional.of(testStaff));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        // Act
        authService.register(registerRequest);

        // Assert
        verify(userRepository).save(argThat(user ->
            user.getEmail().equals(registerRequest.getEmail()) &&
            user.getName().equals(registerRequest.getName()) &&
            user.getPhone().equals(registerRequest.getPhoneNumber()) &&
            user.getRole().equals(Role.ROLE_USER)
        ));
    }
}
