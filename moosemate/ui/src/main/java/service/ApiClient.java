package service;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.ApiResponse;
import dto.LoginRequest;
import dto.SignUpRequest;

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
    public ApiResponse<?> login(String username, String password) throws Exception {
        LoginRequest loginRequest = new LoginRequest(username, password);
        String requestBody = objectMapper.writeValueAsString(loginRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), ApiResponse.class);
    }

    // HTTP-POST request for signup to API
    public ApiResponse<?> signUp(String username, String email, String password) throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest(username, password, email);
        String requestBody = objectMapper.writeValueAsString(signUpRequest);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/auth/signup"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readValue(response.body(), ApiResponse.class);
    }
}
