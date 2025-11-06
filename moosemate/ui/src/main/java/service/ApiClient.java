package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.ApiResponse;
import dto.CreateMoosageRequest;
import dto.LoginRequest;
import dto.LoginResponse;
import dto.MoosageDto;
import dto.SignUpRequest;
import dto.UpdateMoosageRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    private static class SingletonHolder {
        private static final ApiClient INSTANCE = new ApiClient();
    }

    public static ApiClient getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ApiResponse<LoginResponse> login(String username, String password) throws IOException, InterruptedException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        TypeReference<ApiResponse<LoginResponse>> typeRef = new TypeReference<ApiResponse<LoginResponse>>() {};
        return objectMapper.readValue(response.body(), typeRef);
    }

    // Logout user by terminating session on server
    public ApiResponse<String> logout(String sessionToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/logout"))
                .header("Content-Type", "application/json")
                .header("Session-Token", sessionToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        TypeReference<ApiResponse<String>> typeRef = new TypeReference<ApiResponse<String>>() {};
        return objectMapper.readValue(response.body(), typeRef);
    }
    

    // HTTP-POST request for signup to API
    public ApiResponse<String> signUp(String username, String email, String password) throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(username, password, email);
        String requestBody = objectMapper.writeValueAsString(signUpRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/signup"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        TypeReference<ApiResponse<String>> typeRef = new TypeReference<ApiResponse<String>>() { };
        return objectMapper.readValue(response.body(), typeRef);
    }

    // HTTP-GET request to fetch all moosages
    public ApiResponse<List<MoosageDto>> getMoosages() throws IOException, InterruptedException {
        String sessionToken = SessionManager.getInstance().getSessionToken();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/moosages"))
                .header("Content-Type", "application/json")
                .header("Session-Token", sessionToken)
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        TypeReference<ApiResponse<List<MoosageDto>>> typeRef =
                new TypeReference<ApiResponse<List<MoosageDto>>>() { };
        return objectMapper.readValue(response.body(), typeRef);
    }

    // HTTP-POST request to create a new moosage
    public ApiResponse<MoosageDto> postMoosage(String content) throws IOException, InterruptedException {
        String sessionToken = SessionManager.getInstance().getSessionToken();
        CreateMoosageRequest request = new CreateMoosageRequest(content);
        String requestBody = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/moosages"))
                .header("Content-Type", "application/json")
                .header("Session-Token", sessionToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        TypeReference<ApiResponse<MoosageDto>> typeRef = new TypeReference<ApiResponse<MoosageDto>>() { };
        return objectMapper.readValue(response.body(), typeRef);
    }

    // HTTP-POST request to toggle like on a moosage
    public ApiResponse<MoosageDto> toggleLike(Long moosageId) throws IOException, InterruptedException {
        String sessionToken = SessionManager.getInstance().getSessionToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/moosages/" + moosageId + "/like"))
                .header("Content-Type", "application/json")
                .header("Session-Token", sessionToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        TypeReference<ApiResponse<MoosageDto>> typeRef = new TypeReference<ApiResponse<MoosageDto>>() { };
        return objectMapper.readValue(response.body(), typeRef);
    }

    // HTTP-PUT request to update a moosage
    public ApiResponse<MoosageDto> updateMoosage(Long moosageId, String newContent)
            throws IOException, InterruptedException {
        String sessionToken = SessionManager.getInstance().getSessionToken();
        UpdateMoosageRequest request = new UpdateMoosageRequest(newContent);
        String requestBody = objectMapper.writeValueAsString(request);

        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/moosages/" + moosageId))
                .header("Content-Type", "application/json")
                .header("Session-Token", sessionToken)
                .PUT(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        
        TypeReference<ApiResponse<MoosageDto>> typeRef = new TypeReference<ApiResponse<MoosageDto>>() { };
        return objectMapper.readValue(response.body(), typeRef);
    }

    // HTTP-DELETE request to delete a moosage
    public ApiResponse<Void> deleteMoosage(Long moosageId) throws IOException, InterruptedException {
        String sessionToken = SessionManager.getInstance().getSessionToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/moosages/" + moosageId))
                .header("Content-Type", "application/json")
                .header("Session-Token", sessionToken)
                .DELETE()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        TypeReference<ApiResponse<Void>> typeRef = new TypeReference<ApiResponse<Void>>() { };
        return objectMapper.readValue(response.body(), typeRef);
    }
}
