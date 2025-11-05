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
import util.ResponseUtils;

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
        return ResponseUtils.ok("Moosemate REST API is working!", "API Ready");
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
                        sessionToken,
                        user.getUserID());

                return ResponseUtils.ok("Login successful", loginResponse);
            } else {
                return ResponseUtils.unauthorized("Invalid username or password");
            }
        } catch (IllegalArgumentException e) {
            return ResponseUtils.badRequest(e.getMessage());
        } catch (Exception e) {
            return ResponseUtils.internalError("Login failed: " + e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@RequestBody SignUpRequest request) {
        try {
            boolean success = signUpService.signUpUser(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword());
            if (success) {
                return ResponseUtils.created("User registered successfully", "Registration complete");
            } else {
                return ResponseUtils.conflict("User already exists");
            }
        } catch (IllegalArgumentException e) {
            return ResponseUtils.badRequest(e.getMessage());
        } catch (Exception e) {
            return ResponseUtils.internalError("Signup failed: " + e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<String>> logout(
            @RequestHeader("Session-Token") String sessionToken) {

        try {
            System.out.println("Logout endpoint called with token: " + sessionToken); // logging for testing purposes
            sessionService.terminateSession(sessionToken);
            return ResponseUtils.ok("Logged out successfully");
        } catch (Exception e) {
            System.out.println("Logout failed with error: " + e.getMessage()); // logging for testing purposes
            return ResponseUtils.internalError("Logout failed");
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<ApiResponse<Boolean>> verifySession(
            @RequestHeader("Session-Token") String sessionToken) {

        if (sessionService.isValidSession(sessionToken)) {
            return ResponseUtils.ok("Session valid", true);
        } else {
            return ResponseUtils.unauthorized("Invalid session");
        }
    }
}