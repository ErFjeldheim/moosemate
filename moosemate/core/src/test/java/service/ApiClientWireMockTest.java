//Generated with the help of CoPilot Claude Sonnet 4.5

 // NB: Backend must NOT be running when these tests are run. If tests fail, make sure no backend is running in hidden terminal. 

package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.WireMockServer;
import dto.ApiResponse;
import dto.LoginResponse;
import dto.MoosageDto;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.jupiter.api.Assertions.*;

// WireMock-based tests for ApiClient.
// Tests HTTP communication by mocking the backend API server.

class ApiClientWireMockTest {

    private static WireMockServer wireMockServer;
    private static ApiClient apiClient;
    private static SessionManager sessionManager;
    private static ObjectMapper objectMapper;

    @BeforeAll
    static void setUpClass() throws Exception {
        // Start WireMock server on port 8080 (matching ApiClient BASE_URL)
       
        wireMockServer = new WireMockServer(wireMockConfig().port(8080));
        wireMockServer.start();

        // Initialize ApiClient and SessionManager
        apiClient = ApiClient.getInstance();
        sessionManager = SessionManager.getInstance();
        
        // Initialize ObjectMapper for creating test data
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @AfterAll
    static void tearDownClass() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setUp() {
        // Reset WireMock stubs before each test
        wireMockServer.resetAll();
        
        // Clear session
        sessionManager.logout();
    }

    @Test
    void testLogin_Success() throws Exception {
        // Arrange
        String responseJson = """
            {
                "message": "Login successful",
                "data": {
                    "sessionToken": "test-token-123",
                    "userId": "user-uuid-123",
                    "user": {
                        "username": "testuser",
                        "email": "testuser@example.com"
                    }
                }
            }
            """;

        wireMockServer.stubFor(post(urlEqualTo("/api/auth/login"))
            .withRequestBody(containing("testuser"))
            .withRequestBody(containing("password123"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

       
        ApiResponse<LoginResponse> response = apiClient.login("testuser", "password123");

      
        assertNotNull(response);
        assertEquals("Login successful", response.getMessage());
        assertNotNull(response.getData());
        assertEquals("test-token-123", response.getData().getSessionToken());
        assertEquals("user-uuid-123", response.getData().getUserId());
        assertNotNull(response.getData().getUser());
        assertEquals("testuser", response.getData().getUser().getUsername());
        assertEquals("testuser@example.com", response.getData().getUser().getEmail());
      
        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/auth/login"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        // Arrange
        String responseJson = """
            {
                "message": "Invalid username or password",
                "data": null
            }
            """;

        wireMockServer.stubFor(post(urlEqualTo("/api/auth/login"))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));


        ApiResponse<LoginResponse> response = apiClient.login("wronguser", "wrongpass");

   
        assertNotNull(response);
        assertEquals("Invalid username or password", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testSignUp_Success() throws Exception {
        String responseJson = """
            {
                "message": "User created successfully",
                "data": "User registered with ID: 1"
            }
            """;

        wireMockServer.stubFor(post(urlEqualTo("/api/auth/signup"))
            .withRequestBody(containing("newuser"))
            .withRequestBody(containing("newuser@example.com"))
            .withRequestBody(containing("password123"))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

        ApiResponse<String> response = apiClient.signUp("newuser", "newuser@example.com", "password123");
   
        assertNotNull(response);
        assertEquals("User created successfully", response.getMessage());
        assertEquals("User registered with ID: 1", response.getData());
        
        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/auth/signup"))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void testSignUp_DuplicateUsername() throws Exception {
        // Arrange
        String responseJson = """
            {
                "message": "Username already exists",
                "data": null
            }
            """;

        wireMockServer.stubFor(post(urlEqualTo("/api/auth/signup"))
            .willReturn(aResponse()
                .withStatus(400)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

       
        ApiResponse<String> response = apiClient.signUp("existinguser", "test@example.com", "password123");

        
        assertNotNull(response);
        assertEquals("Username already exists", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testLogout_Success() throws Exception {
        // Arrange
        String testToken = "test-session-token";
        setSessionToken(testToken);
        
        String responseJson = """
            {
                "message": "Logout successful",
                "data": "Session terminated"
            }
            """;

        wireMockServer.stubFor(post(urlEqualTo("/api/auth/logout"))
            .withHeader("Session-Token", equalTo(testToken))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

       
        ApiResponse<String> response = apiClient.logout(testToken);

    
        assertNotNull(response);
        assertEquals("Logout successful", response.getMessage());
        assertEquals("Session terminated", response.getData());
        
        // Verify request
        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/auth/logout"))
            .withHeader("Session-Token", equalTo(testToken)));
    }

    @Test
    void testGetMoosages_Success() throws Exception {
        // Arrange
        String testToken = "test-token";
        setSessionToken(testToken);
        
        String responseJson = """
            {
                "message": "Moosages fetched successfully",
                "data": [
                    {
                        "id": 1,
                        "content": "First moosage",
                        "time": "2025-11-08T10:00:00",
                        "authorId": "user-uuid-1",
                        "authorUsername": "user1",
                        "likedByUserIds": [],
                        "edited": false
                    },
                    {
                        "id": 2,
                        "content": "Second moosage",
                        "time": "2025-11-08T11:00:00",
                        "authorId": "user-uuid-2",
                        "authorUsername": "user2",
                        "likedByUserIds": ["user-uuid-1"],
                        "edited": false
                    }
                ]
            }
            """;

        wireMockServer.stubFor(get(urlEqualTo("/api/moosages"))
            .withHeader("Session-Token", equalTo(testToken))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

        
        ApiResponse<List<MoosageDto>> response = apiClient.getMoosages();

        
        assertNotNull(response);
        assertEquals("Moosages fetched successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(2, response.getData().size());
        
        MoosageDto firstMoosage = response.getData().get(0);
        assertEquals(1L, firstMoosage.getId());
        assertEquals("First moosage", firstMoosage.getContent());
        assertEquals("user1", firstMoosage.getAuthorUsername());
        
        MoosageDto secondMoosage = response.getData().get(1);
        assertEquals(2L, secondMoosage.getId());
        assertEquals("Second moosage", secondMoosage.getContent());
        assertEquals(1, secondMoosage.getLikedByUserIds().size());
        
        // Verify request
        wireMockServer.verify(getRequestedFor(urlEqualTo("/api/moosages"))
            .withHeader("Session-Token", equalTo(testToken)));
    }

    @Test
    void testGetMoosages_Unauthorized() throws Exception {
        // Arrange
        String testToken = "invalid-token";
        setSessionToken(testToken);
        
        String responseJson = """
            {
                "message": "Unauthorized",
                "data": null
            }
            """;

        wireMockServer.stubFor(get(urlEqualTo("/api/moosages"))
            .willReturn(aResponse()
                .withStatus(401)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

       
        ApiResponse<List<MoosageDto>> response = apiClient.getMoosages();

        
        assertNotNull(response);
        assertEquals("Unauthorized", response.getMessage());
        assertNull(response.getData());
    }

    @Test
    void testPostMoosage_Success() throws Exception {
        // Arrange
        String testToken = "test-token";
        setSessionToken(testToken);
        
        String responseJson = """
            {
                "message": "Moosage created successfully",
                "data": {
                    "id": 3,
                    "content": "New moosage content",
                    "time": "2025-11-08T12:00:00",
                    "authorId": "user-uuid-1",
                    "authorUsername": "testuser",
                    "likedByUserIds": [],
                    "edited": false
                }
            }
            """;

        wireMockServer.stubFor(post(urlEqualTo("/api/moosages"))
            .withHeader("Session-Token", equalTo(testToken))
            .withRequestBody(containing("New moosage content"))
            .willReturn(aResponse()
                .withStatus(201)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

        
        ApiResponse<MoosageDto> response = apiClient.postMoosage("New moosage content");

      
        assertNotNull(response);
        assertEquals("Moosage created successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(3L, response.getData().getId());
        assertEquals("New moosage content", response.getData().getContent());
        assertEquals("testuser", response.getData().getAuthorUsername());
        
        // Verify request
        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/moosages"))
            .withHeader("Session-Token", equalTo(testToken))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void testToggleLike_Success() throws Exception {
        // Arrange
        String testToken = "test-token";
        setSessionToken(testToken);
        Long moosageId = 5L;
        
        String responseJson = """
            {
                "message": "Like toggled successfully",
                "data": {
                    "id": 5,
                    "content": "Liked moosage",
                    "time": "2025-11-08T12:00:00",
                    "authorId": "user-uuid-2",
                    "authorUsername": "otheruser",
                    "likedByUserIds": ["user-uuid-1"],
                    "edited": false
                }
            }
            """;

        wireMockServer.stubFor(post(urlEqualTo("/api/moosages/" + moosageId + "/like"))
            .withHeader("Session-Token", equalTo(testToken))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

       
        ApiResponse<MoosageDto> response = apiClient.toggleLike(moosageId);

        
        assertNotNull(response);
        assertEquals("Like toggled successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(5L, response.getData().getId());
        assertEquals(1, response.getData().getLikedByUserIds().size());
        assertTrue(response.getData().getLikedByUserIds().contains("user-uuid-1"));
        
        // Verify request
        wireMockServer.verify(postRequestedFor(urlEqualTo("/api/moosages/5/like"))
            .withHeader("Session-Token", equalTo(testToken)));
    }

    @Test
    void testUpdateMoosage_Success() throws Exception {
        // Arrange
        String testToken = "test-token";
        setSessionToken(testToken);
        Long moosageId = 3L;
        String newContent = "Updated moosage content";
        
        String responseJson = """
            {
                "message": "Moosage updated successfully",
                "data": {
                    "id": 3,
                    "content": "Updated moosage content",
                    "time": "2025-11-08T12:30:00",
                    "authorId": "user-uuid-1",
                    "authorUsername": "testuser",
                    "likedByUserIds": [],
                    "edited": true
                }
            }
            """;

        wireMockServer.stubFor(put(urlEqualTo("/api/moosages/" + moosageId))
            .withHeader("Session-Token", equalTo(testToken))
            .withRequestBody(containing(newContent))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

      
        ApiResponse<MoosageDto> response = apiClient.updateMoosage(moosageId, newContent);

       
        assertNotNull(response);
        assertEquals("Moosage updated successfully", response.getMessage());
        assertNotNull(response.getData());
        assertEquals(3L, response.getData().getId());
        assertEquals("Updated moosage content", response.getData().getContent());
        
        // Verify request
        wireMockServer.verify(putRequestedFor(urlEqualTo("/api/moosages/3"))
            .withHeader("Session-Token", equalTo(testToken))
            .withHeader("Content-Type", equalTo("application/json")));
    }

    @Test
    void testDeleteMoosage_Success() throws Exception {
        // Arrange
        String testToken = "test-token";
        setSessionToken(testToken);
        Long moosageId = 7L;
        
        String responseJson = """
            {
                "message": "Moosage deleted successfully",
                "data": null
            }
            """;

        wireMockServer.stubFor(delete(urlEqualTo("/api/moosages/" + moosageId))
            .withHeader("Session-Token", equalTo(testToken))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

     
        ApiResponse<Void> response = apiClient.deleteMoosage(moosageId);

   
        assertNotNull(response);
        assertEquals("Moosage deleted successfully", response.getMessage());
        assertNull(response.getData());
        
        // Verify request
        wireMockServer.verify(deleteRequestedFor(urlEqualTo("/api/moosages/7"))
            .withHeader("Session-Token", equalTo(testToken)));
    }

    @Test
    void testDeleteMoosage_NotFound() throws Exception {
        // Arrange
        String testToken = "test-token";
        setSessionToken(testToken);
        Long moosageId = 999L;
        
        String responseJson = """
            {
                "message": "Moosage not found",
                "data": null
            }
            """;

        wireMockServer.stubFor(delete(urlEqualTo("/api/moosages/" + moosageId))
            .willReturn(aResponse()
                .withStatus(404)
                .withHeader("Content-Type", "application/json")
                .withBody(responseJson)));

    
        ApiResponse<Void> response = apiClient.deleteMoosage(moosageId);

        assertNotNull(response);
        assertEquals("Moosage not found", response.getMessage());
    }

    // Helper method to set session token using reflection since SessionManager is a singleton
     
    private void setSessionToken(String token) throws Exception {
        Field sessionTokenField = SessionManager.class.getDeclaredField("sessionToken");
        sessionTokenField.setAccessible(true);
        sessionTokenField.set(sessionManager, token);
    }
}
