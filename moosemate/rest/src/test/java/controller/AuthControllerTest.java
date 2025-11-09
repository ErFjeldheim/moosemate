package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dto.ApiResponse;
import dto.LoginRequest;
import dto.LoginResponse;
import dto.SignUpRequest;
import model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import service.LoginService;
import service.SessionService;
import service.SignUpService;

/**
 * Comprehensive tests for AuthController with focus on error handling.
 */
public class AuthControllerTest {
    
    @Mock
    private LoginService loginService;
    
    @Mock
    private SignUpService signUpService;
    
    @Mock
    private SessionService sessionService;
    
    @InjectMocks
    private AuthController authController;
    
    private User testUser;
    private LoginRequest loginRequest;
    private SignUpRequest signUpRequest;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("testuser", "test@example.com", "password", "user123");
        
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");
        
        signUpRequest = new SignUpRequest();
        signUpRequest.setUsername("newuser");
        signUpRequest.setEmail("new@example.com");
        signUpRequest.setPassword("Password123");
    }
    
    // ========== Test Endpoint Tests ==========
    
    @Test
    public void testTestEndpoint() {
        ResponseEntity<ApiResponse<String>> response = authController.test();
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("Moosemate REST API is working!", body.getMessage());
    }
    
    // ========== Login Tests ==========
    
    @Test
    public void testLoginSuccess() {
        when(loginService.loginUser("testuser", "password")).thenReturn(testUser);
        when(sessionService.createSession(testUser)).thenReturn("token123");
        
        ResponseEntity<ApiResponse<LoginResponse>> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse<LoginResponse> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("Login successful", body.getMessage());
        assertNotNull(body.getData());
        LoginResponse data = body.getData();
        assertNotNull(data);
        assertEquals("token123", data.getSessionToken());
        assertEquals("testuser", data.getUser().getUsername());
        
        verify(loginService, times(1)).loginUser("testuser", "password");
        verify(sessionService, times(1)).createSession(testUser);
    }
    
    @Test
    public void testLoginInvalidCredentials() {
        when(loginService.loginUser("testuser", "wrongpassword")).thenReturn(null);
        loginRequest.setPassword("wrongpassword");
        
        ResponseEntity<ApiResponse<LoginResponse>> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse<LoginResponse> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals("Invalid username or password", body.getMessage());
        assertNull(body.getData());
    }
    
    @Test
    public void testLoginWithEmptyUsername() {
        loginRequest.setUsername("");
        when(loginService.loginUser("", "password"))
                .thenThrow(new IllegalArgumentException("Username cannot be empty"));
        
        ResponseEntity<ApiResponse<LoginResponse>> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        ApiResponse<LoginResponse> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertTrue(body.getMessage().contains("Username cannot be empty"));
    }
    
    @Test
    public void testLoginWithNullUsername() {
        loginRequest.setUsername(null);
        when(loginService.loginUser(null, "password"))
            .thenThrow(new IllegalArgumentException("Username cannot be null"));
        
        ResponseEntity<ApiResponse<LoginResponse>> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<LoginResponse> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
    }
    
    @Test
    public void testLoginWithEmptyPassword() {
        loginRequest.setPassword("");
        when(loginService.loginUser("testuser", ""))
            .thenThrow(new IllegalArgumentException("Password cannot be empty"));
        
        ResponseEntity<ApiResponse<LoginResponse>> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<LoginResponse> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
    }
    
    @Test
    public void testLoginInternalServerError() {
        when(loginService.loginUser(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database connection failed"));
        
        ResponseEntity<ApiResponse<LoginResponse>> response = authController.login(loginRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiResponse<LoginResponse> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertTrue(body.getMessage().contains("Login failed"));
    }
    
    // ========== Signup Tests ==========
    
    @Test
    public void testSignupSuccess() {
        when(signUpService.signUpUser("newuser", "new@example.com", "Password123")).thenReturn(true);
        
        ResponseEntity<ApiResponse<String>> response = authController.signup(signUpRequest);
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("User registered successfully", body.getMessage());
        
        verify(signUpService, times(1)).signUpUser("newuser", "new@example.com", "Password123");
    }
    
    @Test
    public void testSignupUserAlreadyExists() {
        when(signUpService.signUpUser("newuser", "new@example.com", "Password123")).thenReturn(false);
        
        ResponseEntity<ApiResponse<String>> response = authController.signup(signUpRequest);
        
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals("User already exists", body.getMessage());
    }
    
    @Test
    public void testSignupWithInvalidEmail() {
        signUpRequest.setEmail("invalid-email");
        when(signUpService.signUpUser(anyString(), eq("invalid-email"), anyString()))
            .thenThrow(new IllegalArgumentException("Invalid email format"));
        
        ResponseEntity<ApiResponse<String>> response = authController.signup(signUpRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertTrue(body.getMessage().contains("Invalid email format"));
    }
    
    @Test
    public void testSignupWithWeakPassword() {
        signUpRequest.setPassword("weak");
        when(signUpService.signUpUser(anyString(), anyString(), eq("weak")))
            .thenThrow(new IllegalArgumentException("Password too weak"));
        
        ResponseEntity<ApiResponse<String>> response = authController.signup(signUpRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
    }
    
    @Test
    public void testSignupWithEmptyUsername() {
        signUpRequest.setUsername("");
        when(signUpService.signUpUser("", "new@example.com", "Password123"))
            .thenThrow(new IllegalArgumentException("Username cannot be empty"));
        
        ResponseEntity<ApiResponse<String>> response = authController.signup(signUpRequest);
        
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
    }
    
    @Test
    public void testSignupInternalServerError() {
        when(signUpService.signUpUser(anyString(), anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));
        
        ResponseEntity<ApiResponse<String>> response = authController.signup(signUpRequest);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertTrue(body.getMessage().contains("Signup failed"));
    }
    
    // ========== Logout Tests ==========
    
    @Test
    public void testLogoutSuccess() {
        doNothing().when(sessionService).terminateSession("token123");
        
        ResponseEntity<ApiResponse<String>> response = authController.logout("token123");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("Logged out successfully", body.getMessage());
        
        verify(sessionService, times(1)).terminateSession("token123");
    }
    
    @Test
    public void testLogoutWithInvalidToken() {
        doThrow(new RuntimeException("Invalid token")).when(sessionService).terminateSession("invalid");
        
        ResponseEntity<ApiResponse<String>> response = authController.logout("invalid");
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals("Logout failed", body.getMessage());
    }
    
    @Test
    public void testLogoutWithNullToken() {
        doThrow(new RuntimeException("Token is null")).when(sessionService).terminateSession(null);
        
        ResponseEntity<ApiResponse<String>> response = authController.logout(null);
        
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        ApiResponse<String> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
    }
    
    // ========== Verify Session Tests ==========
    
    @Test
    public void testVerifySessionValid() {
        when(sessionService.isValidSession("token123")).thenReturn(true);
        
        ResponseEntity<ApiResponse<Boolean>> response = authController.verifySession("token123");
        
        assertEquals(HttpStatus.OK, response.getStatusCode());
        ApiResponse<Boolean> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("Session valid", body.getMessage());
        Boolean data = body.getData();
        assertNotNull(data);
        assertTrue(data);
        
        verify(sessionService, times(1)).isValidSession("token123");
    }
    
    @Test
    public void testVerifySessionInvalid() {
        when(sessionService.isValidSession("invalid")).thenReturn(false);
        
        ResponseEntity<ApiResponse<Boolean>> response = authController.verifySession("invalid");
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponse<Boolean> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
        assertEquals("Invalid session", body.getMessage());
        
        verify(sessionService, times(1)).isValidSession("invalid");
    }
    
    @Test
    public void testVerifySessionWithNullToken() {
        when(sessionService.isValidSession(null)).thenReturn(false);
        
        ResponseEntity<ApiResponse<Boolean>> response = authController.verifySession(null);
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponse<Boolean> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
    }
    
    @Test
    public void testVerifySessionWithEmptyToken() {
        when(sessionService.isValidSession("")).thenReturn(false);
        
        ResponseEntity<ApiResponse<Boolean>> response = authController.verifySession("");
        
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ApiResponse<Boolean> body = response.getBody();
        assertNotNull(body);
        assertFalse(body.isSuccess());
    }
}
