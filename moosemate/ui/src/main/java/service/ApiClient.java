package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ApiResponse;
import dto.LoginRequest;
import dto.LoginResponse;
import dto.SignUpRequest;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// HTTP client for communicating with the REST API.
public class ApiClient {
    private static final String BASE_URL = "http://localhost:8080/api";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ApiClient() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    // HTTP-POST request for login to API
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
        
        TypeReference<ApiResponse<String>> typeRef = new TypeReference<ApiResponse<String>>() {};
        return objectMapper.readValue(response.body(), typeRef);
    }
}
