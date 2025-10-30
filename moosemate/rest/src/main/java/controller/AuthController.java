package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.LoginService;
import service.SignUpService;
import service.SessionService;
import dto.LoginRequest;
import dto.SignUpRequest;
import dto.ApiResponse;
import dto.LoginResponse;
import model.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private LoginService loginService;
    
    @Autowired
    private SignUpService signUpService;

    @Autowired
    private SessionService sessionService;
    
    @GetMapping("/test")
    public ResponseEntity<ApiResponse<String>> test() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Moosemate REST API is working!", "API Ready"));
    }
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        try {
            // validate and login
            User user = loginService.loginUser(request.getUsername(), request.getPassword());
            
            if (user != null) {
                // creates session and get UUID token
                String sessionToken = sessionService.createSession(user);
            
                LoginResponse loginResponse = new LoginResponse(
                    user.getUsername(),
                    user.getEmail(),
                    sessionToken
                );

                return ResponseEntity.ok(
                    new ApiResponse<>(true, "Login successful", loginResponse)
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Invalid username or password"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Login failed: " + e.getMessage()));
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
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>(true, "User registered successfully", "Registration complete"));
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse<>(false, "User already exists"));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse<>(false, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Signup failed: " + e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
        @RequestHeader("Session-Token") String sessionToken) {

        try {
            System.out.println("Logout endpoint called with token: " + sessionToken);
            sessionService.terminateSession(sessionToken);
            return ResponseEntity.ok(new ApiResponse<>(true, "Logged out succesfully"));
        } catch (Exception e) {
            System.out.println("Logout failed with error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(false, "Logout failed"));
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifySession(
        @RequestHeader("Session-Token") String sessionToken) {
            boolean valid = sessionService.isValidSession(sessionToken);

            if (valid) {
                User user = sessionService.getUser(sessionToken);
                System.out.println("Session verified for: " + user.getUsername());
                return ResponseEntity.ok(new ApiResponse<>(true, "Session valid", true));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ApiResponse<>(false, "Invalid session", false));
            }
        }    
}