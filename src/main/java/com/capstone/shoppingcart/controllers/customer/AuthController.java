package com.capstone.shoppingcart.controllers.customer;

import com.capstone.shoppingcart.dtos.AuthResponse;
import com.capstone.shoppingcart.dtos.LoginRequest;
import com.capstone.shoppingcart.dtos.RegisterRequest;
import com.capstone.shoppingcart.services.auth.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> signin(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.signin(request));
    }
}
