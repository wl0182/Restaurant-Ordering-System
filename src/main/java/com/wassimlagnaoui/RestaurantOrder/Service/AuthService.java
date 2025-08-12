package com.wassimlagnaoui.RestaurantOrder.Service;


import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.AuthRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.AuthResponse;
import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.RegisterRequestDTO;
import com.wassimlagnaoui.RestaurantOrder.Repository.StaffRepository;
import com.wassimlagnaoui.RestaurantOrder.Repository.UserRepository;
import com.wassimlagnaoui.RestaurantOrder.Security.JwtService;
import com.wassimlagnaoui.RestaurantOrder.model.Role;
import com.wassimlagnaoui.RestaurantOrder.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final StaffRepository staffRepository;


    public AuthResponse login(AuthRequest authRequest){


        try {
            System.out.println("⏳ Attempting login for: " + authRequest.getEmail());

            System.out.println("🔐 Does encoder match? " +
                    passwordEncoder.matches(authRequest.getPassword(), userRepository.findByEmail(authRequest.getEmail())
                            .orElseThrow().getPassword()));

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );

            System.out.println("✅ Authenticated");

            User user = userRepository.findByEmail(authRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            String jwtToken = jwtService.generateToken(user);

            return new AuthResponse(jwtToken);

        } catch (Exception ex) {
            System.out.println(" Login failed: " + ex.getClass().getSimpleName() + " - " + ex.getMessage());
            throw ex;
        }




}


    public AuthResponse register(RegisterRequestDTO request) {
        System.out.println("🔄 Registering user: " + request.getEmail());

        boolean staffExists = staffRepository.findByEmployeeId(request.getEmployeeId()).isPresent();

        if (!staffExists){
            System.out.println("Staff with Employee ID " + request.getEmployeeId() + " does not exist.");
            throw new UsernameNotFoundException("Staff with Employee ID " + request.getEmployeeId() + " does not exist.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .phone(request.getPhoneNumber())
                .role(Role.ROLE_USER)
                .build();

        System.out.println("🔐 Password encoded: " + user.getPassword());

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);

        System.out.println("✅ User registered successfully, JWT token generated");

        return new AuthResponse(jwtToken);
    }
}