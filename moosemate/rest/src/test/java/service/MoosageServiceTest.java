package service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import model.Moosage;
import model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import repository.MoosageRepository;
import repository.UserRepository;

/**
 * Comprehensive tests for MoosageService with focus on error handling.
 */
public class MoosageServiceTest {
    
    @Mock
    private MoosageRepository moosageRepository;
    
    @Mock
    private UserRepository userRepository;
    
    @InjectMocks
    private MoosageService moosageService;
    
    private User testUser;
    private Moosage testMoosage;
    
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User("testuser", "test@example.com", "password", "user123");
        testMoosage = new Moosage(1L, "Test content", testUser, LocalDateTime.now());
    }
    
    @Test
    public void testGetAllMoosages() {
        List<Moosage> moosages = Arrays.asList(testMoosage);
        when(moosageRepository.getAllMoosages()).thenReturn(moosages);
        
        List<Moosage> result = moosageService.getAllMoosages();
        
        assertEquals(1, result.size());
        assertEquals(testMoosage.getId(), result.get(0).getId());
        verify(moosageRepository, times(1)).getAllMoosages();
    }
    
    @Test
    public void testGetAllMoosagesWhenEmpty() {
        when(moosageRepository.getAllMoosages()).thenReturn(Arrays.asList());
        
        List<Moosage> result = moosageService.getAllMoosages();
        
        assertTrue(result.isEmpty());
        verify(moosageRepository, times(1)).getAllMoosages();
    }
    
    @Test
    public void testGetMoosageById() {
        when(moosageRepository.getMoosageById(1L)).thenReturn(Optional.of(testMoosage));
        
        Optional<Moosage> result = moosageService.getMoosageById(1L);
        
        assertTrue(result.isPresent());
        assertEquals(testMoosage.getId(), result.get().getId());
        verify(moosageRepository, times(1)).getMoosageById(1L);
    }
    
    @Test
    public void testGetMoosageByIdNotFound() {
        when(moosageRepository.getMoosageById(999L)).thenReturn(Optional.empty());
        
        Optional<Moosage> result = moosageService.getMoosageById(999L);
        
        assertFalse(result.isPresent());
        verify(moosageRepository, times(1)).getMoosageById(999L);
    }
    
    @Test
    public void testCreateMoosageSuccess() {
        when(userRepository.getUserById("user123")).thenReturn(Optional.of(testUser));
        when(moosageRepository.createMoosage(anyString(), anyString(), anyString()))
                .thenReturn(testMoosage);
        
        Moosage result = moosageService.createMoosage("Test content", "user123");
        
        assertNotNull(result);
        assertEquals(testMoosage.getId(), result.getId());
        verify(userRepository, times(1)).getUserById("user123");
        verify(moosageRepository, times(1)).createMoosage("Test content", "user123", "testuser");
    }
    
    @Test
    public void testCreateMoosageUserNotFound() {
        when(userRepository.getUserById("nonexistent")).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            moosageService.createMoosage("Test content", "nonexistent");
        });
        
        assertEquals("User not found with ID: nonexistent", exception.getMessage());
        verify(userRepository, times(1)).getUserById("nonexistent");
        verify(moosageRepository, never()).createMoosage(anyString(), anyString(), anyString());
    }
    
    @Test
    public void testCreateMoosageWithNullUserId() {
        when(userRepository.getUserById(null)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> {
            moosageService.createMoosage("Test content", null);
        });
    }
    
    @Test
    public void testToggleLikeSuccess() {
        when(userRepository.getUserById("user123")).thenReturn(Optional.of(testUser));
        when(moosageRepository.toggleLike(1L, "user123")).thenReturn(Optional.of(testMoosage));
        
        Optional<Moosage> result = moosageService.toggleLike(1L, "user123");
        
        assertTrue(result.isPresent());
        verify(userRepository, times(1)).getUserById("user123");
        verify(moosageRepository, times(1)).toggleLike(1L, "user123");
    }
    
    @Test
    public void testToggleLikeUserNotFound() {
        when(userRepository.getUserById("nonexistent")).thenReturn(Optional.empty());
        
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            moosageService.toggleLike(1L, "nonexistent");
        });
        
        assertEquals("User not found with ID: nonexistent", exception.getMessage());
        verify(userRepository, times(1)).getUserById("nonexistent");
        verify(moosageRepository, never()).toggleLike(anyLong(), anyString());
    }
    
    @Test
    public void testToggleLikeMoosageNotFound() {
        when(userRepository.getUserById("user123")).thenReturn(Optional.of(testUser));
        when(moosageRepository.toggleLike(999L, "user123")).thenReturn(Optional.empty());
        
        Optional<Moosage> result = moosageService.toggleLike(999L, "user123");
        
        assertFalse(result.isPresent());
        verify(moosageRepository, times(1)).toggleLike(999L, "user123");
    }
    
    @Test
    public void testUpdateMoosageSuccess() {
        when(moosageRepository.updateMoosage(1L, "Updated content")).thenReturn(Optional.of(testMoosage));
        
        Optional<Moosage> result = moosageService.updateMoosage(1L, "Updated content");
        
        assertTrue(result.isPresent());
        verify(moosageRepository, times(1)).updateMoosage(1L, "Updated content");
    }
    
    @Test
    public void testUpdateMoosageNotFound() {
        when(moosageRepository.updateMoosage(999L, "Updated content")).thenReturn(Optional.empty());
        
        Optional<Moosage> result = moosageService.updateMoosage(999L, "Updated content");
        
        assertFalse(result.isPresent());
        verify(moosageRepository, times(1)).updateMoosage(999L, "Updated content");
    }
    
    @Test
    public void testUpdateMoosageWithEmptyContent() {
        when(moosageRepository.updateMoosage(1L, "")).thenReturn(Optional.of(testMoosage));
        
        Optional<Moosage> result = moosageService.updateMoosage(1L, "");
        
        assertTrue(result.isPresent());
        verify(moosageRepository, times(1)).updateMoosage(1L, "");
    }
    
    @Test
    public void testUpdateMoosageWithNullContent() {
        when(moosageRepository.updateMoosage(1L, null)).thenReturn(Optional.of(testMoosage));
        
        Optional<Moosage> result = moosageService.updateMoosage(1L, null);
        
        assertTrue(result.isPresent());
        verify(moosageRepository, times(1)).updateMoosage(1L, null);
    }
    
    @Test
    public void testDeleteMoosageSuccess() {
        when(moosageRepository.deleteMoosage(1L)).thenReturn(true);
        
        boolean result = moosageService.deleteMoosage(1L);
        
        assertTrue(result);
        verify(moosageRepository, times(1)).deleteMoosage(1L);
    }
    
    @Test
    public void testDeleteMoosageNotFound() {
        when(moosageRepository.deleteMoosage(999L)).thenReturn(false);
        
        boolean result = moosageService.deleteMoosage(999L);
        
        assertFalse(result);
        verify(moosageRepository, times(1)).deleteMoosage(999L);
    }
    
    @Test
    public void testDeleteMoosageWithNullId() {
        when(moosageRepository.deleteMoosage(null)).thenReturn(false);
        
        boolean result = moosageService.deleteMoosage(null);
        
        assertFalse(result);
        verify(moosageRepository, times(1)).deleteMoosage(null);
    }
    
    @Test
    public void testRepositoryThrowsException() {
        when(moosageRepository.getAllMoosages()).thenThrow(new RuntimeException("Database error"));
        
        assertThrows(RuntimeException.class, () -> {
            moosageService.getAllMoosages();
        });
    }
    
    @Test
    public void testCreateMoosageWithEmptyContent() {
        when(userRepository.getUserById("user123")).thenReturn(Optional.of(testUser));
        when(moosageRepository.createMoosage("", "user123", "testuser"))
                .thenReturn(testMoosage);
        
        Moosage result = moosageService.createMoosage("", "user123");
        
        assertNotNull(result);
        verify(moosageRepository, times(1)).createMoosage("", "user123", "testuser");
    }
    
    @Test
    public void testToggleLikeWithNullUserId() {
        when(userRepository.getUserById(null)).thenReturn(Optional.empty());
        
        assertThrows(IllegalArgumentException.class, () -> {
            moosageService.toggleLike(1L, null);
        });
    }
    
    @Test
    public void testToggleLikeWithNullMoosageId() {
        when(userRepository.getUserById("user123")).thenReturn(Optional.of(testUser));
        when(moosageRepository.toggleLike(null, "user123")).thenReturn(Optional.empty());
        
        Optional<Moosage> result = moosageService.toggleLike(null, "user123");
        
        assertFalse(result.isPresent());
    }
}
