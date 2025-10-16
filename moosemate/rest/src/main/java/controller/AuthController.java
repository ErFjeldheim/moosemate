package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.LoginService;
import service.SignUpService;
import dto.LoginRequest;
import dto.SignUpRequest;
import dto.ApiResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private SignUpService signUpService;
    
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Moosemate REST API is working!", "API Ready"));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<String>> login(@RequestBody LoginRequest request) {
        try {
            boolean success = loginService.loginUser(request.getUsername(), request.getPassword());
            if (success) {
                return ResponseEntity.ok(new ApiResponse<>(true, "Login successful", "User authenticated"));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid credentials"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Login failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignUpRequest request) {
        try {
            boolean success = signUpService.signUpUser(
                request.getUsername(), 
                request.getEmail(),
                request.getPassword()
            );
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "User created successfully", "Registration complete"));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(false, "User already exists"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Signup failed: " + e.getMessage()));
        }
    }
}