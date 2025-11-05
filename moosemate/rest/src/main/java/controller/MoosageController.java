package controller;

import dto.ApiResponse;
import dto.CreateMoosageRequest;
import dto.MoosageDto;
import dto.UpdateMoosageRequest;
import model.Moosage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import service.MoosageService;
import service.SessionService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import util.ValidationUtils;

// REST Controller for moosage-related endpoints.
@RestController
@RequestMapping("/api/moosages")
public class MoosageController {

    private final MoosageService moosageService;
    private final SessionService sessionService;

    @Autowired
    public MoosageController(MoosageService moosageService, SessionService sessionService) {
        this.moosageService = moosageService;
        this.sessionService = sessionService;
    }

    // Get all moosages (GET /api/moosages)
    @GetMapping
    public ResponseEntity<ApiResponse<List<MoosageDto>>> getAllMoosages(
            @RequestHeader("Session-Token") String sessionToken) {

        // Verify session
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid session token", null));
        }

        try {
            List<Moosage> moosages = moosageService.getAllMoosages();
            List<MoosageDto> moosageDtos = moosages.stream()
                    .map(MoosageDto::fromMoosage)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(new ApiResponse<>(true, "Moosages retrieved successfully", moosageDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error retrieving moosages: " + e.getMessage(), null));
        }
    }

    // Get a specific moosage by ID. (GET /api/moosages/{id})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MoosageDto>> getMoosageById(
            @PathVariable Long id,
            @RequestHeader("Session-Token") String sessionToken) {

        // Verify session
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid session token", null));
        }

        Optional<Moosage> moosage = moosageService.getMoosageById(id);
        if (moosage.isPresent()) {
            MoosageDto dto = MoosageDto.fromMoosage(moosage.get());
            return ResponseEntity.ok(new ApiResponse<>(true, "Moosage found", dto));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Moosage not found", null));
        }
    }

    // Create a new moosage. (POST /api/moosages)
    @PostMapping
    public ResponseEntity<ApiResponse<MoosageDto>> createMoosage(
            @RequestBody CreateMoosageRequest request,
            @RequestHeader("Session-Token") String sessionToken) {

        // Verify session and get user ID
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid session token", null));
        }

        // Validate content
        try {
            ValidationUtils.requireNonEmpty(request.getContent(), "Content");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }

        try {
            Moosage moosage = moosageService.createMoosage(request.getContent(), userId);
            MoosageDto dto = MoosageDto.fromMoosage(moosage);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(true, "Moosage created successfully", dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error creating moosage: " + e.getMessage(), null));
        }
    }

    // Toggle like on a moosage. (POST /api/moosages/{id}/like)
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<MoosageDto>> toggleLike(
            @PathVariable Long id,
            @RequestHeader("Session-Token") String sessionToken) {

        // Verify session and get user ID
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid session token", null));
        }

        try {
            Optional<Moosage> moosage = moosageService.toggleLike(id, userId);
            if (moosage.isPresent()) {
                MoosageDto dto = MoosageDto.fromMoosage(moosage.get());
                return ResponseEntity.ok(new ApiResponse<>(true, "Like toggled successfully", dto));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Moosage not found", null));
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error toggling like: " + e.getMessage(), null));
        }
    }

    // Update content in a Moosage. (PUT /api/moosages/{id})
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MoosageDto>> updateMoosage(
            @PathVariable Long id,
            @RequestBody UpdateMoosageRequest request,
            @RequestHeader("Session-Token") String sessionToken) {

        // Verify session
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid session token", null));
        }

        // Validate content
        try {
            ValidationUtils.requireNonEmpty(request.getContent(), "Content");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(false, e.getMessage(), null));
        }

        // Check if user is the author of the moosage
        Optional<Moosage> moosage = moosageService.getMoosageById(id);
        if (moosage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Moosage not found", null));
        }

        if (!moosage.get().getAuthor().getUserID().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "You can only edit your own moosages", null));
        }

        try {
            Optional<Moosage> updatedMoosage = moosageService.updateMoosage(id, request.getContent().trim());
            if (updatedMoosage.isPresent()) {
                MoosageDto dto = MoosageDto.fromMoosage(updatedMoosage.get());
                return ResponseEntity.ok(new ApiResponse<>(true, "Moosage updated successfully", dto));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "Moosage not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(false, "Error updating moosage: " + e.getMessage(), null));
        }
    }

    // Delete a moosage. (DELETE /api/moosages/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMoosage(
            @PathVariable Long id,
            @RequestHeader("Session-Token") String sessionToken) {

        // Verify session
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(false, "Invalid session token", null));
        }

        // Check if user is the author of the moosage
        Optional<Moosage> moosage = moosageService.getMoosageById(id);
        if (moosage.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Moosage not found", null));
        }

        if (!moosage.get().getAuthor().getUserID().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(false, "You can only delete your own moosages", null));
        }

        boolean deleted = moosageService.deleteMoosage(id);
        if (deleted) {
            return ResponseEntity.ok(new ApiResponse<>(true, "Moosage deleted successfully", null));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>(false, "Moosage not found", null));
        }
    }
}
