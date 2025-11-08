package dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Comprehensive tests for ApiResponse DTO.
 * Tests generic response wrapper used throughout the API.
 */
public class ApiResponseTest {

    @Test
    public void testDefaultConstructor() {
        ApiResponse<String> response = new ApiResponse<>();
        
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNull(response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void testConstructorWithSuccessAndMessage() {
        ApiResponse<String> response = new ApiResponse<>(true, "Operation successful");
        
        assertTrue(response.isSuccess());
        assertEquals("Operation successful", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void testConstructorWithAllParameters() {
        String testData = "test data";
        ApiResponse<String> response = new ApiResponse<>(true, "Success", testData);
        
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
        assertEquals(testData, response.getData());
    }

    @Test
    public void testSetSuccess() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setSuccess(true);
        
        assertTrue(response.isSuccess());
    }

    @Test
    public void testSetMessage() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setMessage("Test message");
        
        assertEquals("Test message", response.getMessage());
    }

    @Test
    public void testSetData() {
        ApiResponse<String> response = new ApiResponse<>();
        response.setData("test data");
        
        assertEquals("test data", response.getData());
    }

    @Test
    public void testWithStringData() {
        ApiResponse<String> response = new ApiResponse<>(true, "String response", "Hello");
        
        assertEquals("Hello", response.getData());
    }

    @Test
    public void testWithIntegerData() {
        ApiResponse<Integer> response = new ApiResponse<>(true, "Integer response", 42);
        
        assertEquals(42, response.getData());
    }

    @Test
    public void testWithListData() {
        List<String> list = Arrays.asList("item1", "item2", "item3");
        ApiResponse<List<String>> response = new ApiResponse<>(true, "List response", list);
        
        assertEquals(list, response.getData());
        assertEquals(3, response.getData().size());
    }

    @Test
    public void testWithNullData() {
        ApiResponse<String> response = new ApiResponse<>(true, "Null data", null);
        
        assertTrue(response.isSuccess());
        assertEquals("Null data", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    public void testFailureResponse() {
        ApiResponse<String> response = new ApiResponse<>(false, "Operation failed");
        
        assertFalse(response.isSuccess());
        assertEquals("Operation failed", response.getMessage());
    }

    @Test
    public void testEmptyMessage() {
        ApiResponse<String> response = new ApiResponse<>(true, "");
        
        assertTrue(response.isSuccess());
        assertEquals("", response.getMessage());
    }

    @Test
    public void testLongMessage() {
        String longMessage = "A".repeat(1000);
        ApiResponse<String> response = new ApiResponse<>(true, longMessage);
        
        assertEquals(longMessage, response.getMessage());
        assertEquals(1000, response.getMessage().length());
    }

    @Test
    public void testWithComplexObjectData() {
        UserDto user = new UserDto("testuser", "test@example.com");
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User retrieved", user);
        
        assertTrue(response.isSuccess());
        assertEquals("User retrieved", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("testuser", response.getData().getUsername());
        assertEquals("test@example.com", response.getData().getEmail());
    }

    @Test
    public void testModifyingResponseAfterCreation() {
        ApiResponse<String> response = new ApiResponse<>(false, "Initial message");
        
        assertFalse(response.isSuccess());
        assertEquals("Initial message", response.getMessage());
        
        response.setSuccess(true);
        response.setMessage("Updated message");
        response.setData("Updated data");
        
        assertTrue(response.isSuccess());
        assertEquals("Updated message", response.getMessage());
        assertEquals("Updated data", response.getData());
    }
}
