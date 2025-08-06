package com.wassimlagnaoui.RestaurantOrder.Controller;


import com.wassimlagnaoui.RestaurantOrder.DTO.Requests.AuthRequest;
import com.wassimlagnaoui.RestaurantOrder.DTO.AuthResponse;
import com.wassimlagnaoui.RestaurantOrder.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
