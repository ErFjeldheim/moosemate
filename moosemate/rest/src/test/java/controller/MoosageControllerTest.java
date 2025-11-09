package controller;

import dto.ApiResponse;
import dto.CreateMoosageRequest;
import dto.MoosageDto;
import dto.UpdateMoosageRequest;
import model.Moosage;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import service.MoosageService;
import service.SessionService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Comprehensive tests for MoosageController with focus on error handling.
 */
public class MoosageControllerTest {

    @Mock
    private MoosageService moosageService;

    @Mock
    private SessionService sessionService;

    @InjectMocks
    private MoosageController moosageController;

    private Moosage testMoosage;
    private User testUser;
    private String validToken = "valid-token-123";
    private String invalidToken = "invalid-token";
    private String userId = "user123";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testUser = new User("testuser", "test@example.com", "password", userId);
        testMoosage = new Moosage(1L, "Test moosage content", testUser, LocalDateTime.now());
    }

        // ============== GET ALL MOOSAGES TESTS ==============

    @Test
    public void testGetAllMoosagesSuccess() {
        List<Moosage> moosages = Arrays.asList(testMoosage);
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getAllMoosages()).thenReturn(moosages);

        ResponseEntity<ApiResponse<List<MoosageDto>>> response = 
            moosageController.getAllMoosages(validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals(1, response.getBody().getData().size());
        verify(sessionService, times(1)).getUserIdByToken(validToken);
        verify(moosageService, times(1)).getAllMoosages();
    }

    @Test
    public void testGetAllMoosagesUnauthorized() {
        when(sessionService.getUserIdByToken(invalidToken)).thenReturn(null);

        ResponseEntity<ApiResponse<List<MoosageDto>>> response = 
            moosageController.getAllMoosages(invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Invalid session token", response.getBody().getMessage());
        verify(moosageService, never()).getAllMoosages();
    }

    @Test
    public void testGetAllMoosagesException() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getAllMoosages()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ApiResponse<List<MoosageDto>>> response = 
            moosageController.getAllMoosages(validToken);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Error retrieving moosages"));
    }

    // ============== GET MOOSAGE BY ID TESTS ==============

    @Test
    public void testGetMoosageByIdSuccess() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(1L)).thenReturn(Optional.of(testMoosage));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.getMoosageById(1L, validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Moosage found", response.getBody().getMessage());
        assertNotNull(response.getBody().getData());
    }

    @Test
    public void testGetMoosageByIdNotFound() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.getMoosageById(999L, validToken);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Moosage not found", response.getBody().getMessage());
    }

    @Test
    public void testGetMoosageByIdUnauthorized() {
        when(sessionService.getUserIdByToken(invalidToken)).thenReturn(null);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.getMoosageById(1L, invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        verify(moosageService, never()).getMoosageById(anyLong());
    }

    // ============== CREATE MOOSAGE TESTS ==============

    @Test
    public void testCreateMoosageSuccess() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        request.setContent("New moosage");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.createMoosage("New moosage", userId)).thenReturn(testMoosage);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.createMoosage(request, validToken);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Moosage created successfully", response.getBody().getMessage());
    }

    @Test
    public void testCreateMoosageUnauthorized() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        request.setContent("New moosage");
        
        when(sessionService.getUserIdByToken(invalidToken)).thenReturn(null);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.createMoosage(request, invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        verify(moosageService, never()).createMoosage(anyString(), anyString());
    }

    @Test
    public void testCreateMoosageEmptyContent() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        request.setContent("");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.createMoosage(request, validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Content cannot be empty", response.getBody().getMessage());
    }

    @Test
    public void testCreateMoosageNullContent() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        request.setContent(null);
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.createMoosage(request, validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    public void testCreateMoosageWhitespaceContent() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        request.setContent("   ");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.createMoosage(request, validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    public void testCreateMoosageIllegalArgumentException() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        request.setContent("Valid content");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.createMoosage(anyString(), anyString()))
            .thenThrow(new IllegalArgumentException("User not found"));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.createMoosage(request, validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("User not found", response.getBody().getMessage());
    }

    @Test
    public void testCreateMoosageException() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        request.setContent("Valid content");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.createMoosage(anyString(), anyString()))
            .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.createMoosage(request, validToken);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Error creating moosage"));
    }

    // ============== TOGGLE LIKE TESTS ==============

    @Test
    public void testToggleLikeSuccess() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.toggleLike(1L, userId)).thenReturn(Optional.of(testMoosage));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.toggleLike(1L, validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Like toggled successfully", response.getBody().getMessage());
    }

    @Test
    public void testToggleLikeUnauthorized() {
        when(sessionService.getUserIdByToken(invalidToken)).thenReturn(null);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.toggleLike(1L, invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        verify(moosageService, never()).toggleLike(anyLong(), anyString());
    }

    @Test
    public void testToggleLikeMoosageNotFound() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.toggleLike(999L, userId)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.toggleLike(999L, validToken);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Moosage not found", response.getBody().getMessage());
    }

    @Test
    public void testToggleLikeIllegalArgumentException() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.toggleLike(1L, userId))
            .thenThrow(new IllegalArgumentException("User not found"));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.toggleLike(1L, validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    public void testToggleLikeException() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.toggleLike(1L, userId))
            .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.toggleLike(1L, validToken);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Error toggling like"));
    }

    // ============== UPDATE MOOSAGE TESTS ==============

    @Test
    public void testUpdateMoosageSuccess() {
        UpdateMoosageRequest request = new UpdateMoosageRequest();
        request.setContent("Updated content");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(1L)).thenReturn(Optional.of(testMoosage));
        when(moosageService.updateMoosage(1L, "Updated content")).thenReturn(Optional.of(testMoosage));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.updateMoosage(1L, request, validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Moosage updated successfully", response.getBody().getMessage());
    }

    @Test
    public void testUpdateMoosageUnauthorized() {
        UpdateMoosageRequest request = new UpdateMoosageRequest();
        request.setContent("Updated content");
        
        when(sessionService.getUserIdByToken(invalidToken)).thenReturn(null);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.updateMoosage(1L, request, invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    public void testUpdateMoosageEmptyContent() {
        UpdateMoosageRequest request = new UpdateMoosageRequest();
        request.setContent("");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.updateMoosage(1L, request, validToken);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Content cannot be empty", response.getBody().getMessage());
    }

    @Test
    public void testUpdateMoosageNotFound() {
        UpdateMoosageRequest request = new UpdateMoosageRequest();
        request.setContent("Updated content");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.updateMoosage(999L, request, validToken);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Moosage not found", response.getBody().getMessage());
    }

    @Test
    public void testUpdateMoosageForbidden() {
        UpdateMoosageRequest request = new UpdateMoosageRequest();
        request.setContent("Updated content");
        
        User otherUser = new User("otheruser", "other@example.com", "password", "otherUserId");
        Moosage otherUserMoosage = new Moosage(1L, "Original content", otherUser, LocalDateTime.now());
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(1L)).thenReturn(Optional.of(otherUserMoosage));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.updateMoosage(1L, request, validToken);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("You can only edit your own moosage", response.getBody().getMessage());
    }

    @Test
    public void testUpdateMoosageException() {
        UpdateMoosageRequest request = new UpdateMoosageRequest();
        request.setContent("Updated content");
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(1L)).thenReturn(Optional.of(testMoosage));
        when(moosageService.updateMoosage(anyLong(), anyString()))
            .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ApiResponse<MoosageDto>> response = 
            moosageController.updateMoosage(1L, request, validToken);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertTrue(response.getBody().getMessage().contains("Error updating moosage"));
    }

    // ============== DELETE MOOSAGE TESTS ==============

    @Test
    public void testDeleteMoosageSuccess() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(1L)).thenReturn(Optional.of(testMoosage));
        when(moosageService.deleteMoosage(1L)).thenReturn(true);

        ResponseEntity<ApiResponse<Void>> response = 
            moosageController.deleteMoosage(1L, validToken);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isSuccess());
        assertEquals("Moosage deleted successfully", response.getBody().getMessage());
    }

    @Test
    public void testDeleteMoosageUnauthorized() {
        when(sessionService.getUserIdByToken(invalidToken)).thenReturn(null);

        ResponseEntity<ApiResponse<Void>> response = 
            moosageController.deleteMoosage(1L, invalidToken);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    public void testDeleteMoosageNotFound() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(999L)).thenReturn(Optional.empty());

        ResponseEntity<ApiResponse<Void>> response = 
            moosageController.deleteMoosage(999L, validToken);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Moosage not found", response.getBody().getMessage());
    }

    @Test
    public void testDeleteMoosageForbidden() {
        User otherUser = new User("otheruser", "other@example.com", "password", "otherUserId");
        Moosage otherUserMoosage = new Moosage(1L, "Content", otherUser, LocalDateTime.now());
        
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(1L)).thenReturn(Optional.of(otherUserMoosage));

        ResponseEntity<ApiResponse<Void>> response = 
            moosageController.deleteMoosage(1L, validToken);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("You can only delete your own moosage", response.getBody().getMessage());
    }

    @Test
    public void testDeleteMoosageReturnsFalse() {
        when(sessionService.getUserIdByToken(validToken)).thenReturn(userId);
        when(moosageService.getMoosageById(1L)).thenReturn(Optional.of(testMoosage));
        when(moosageService.deleteMoosage(1L)).thenReturn(false);

        ResponseEntity<ApiResponse<Void>> response = 
            moosageController.deleteMoosage(1L, validToken);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isSuccess());
        assertEquals("Moosage not found", response.getBody().getMessage());
    }
}
