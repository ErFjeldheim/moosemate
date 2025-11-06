package controller;

import dto.ApiResponse;
import dto.CreateMoosageRequest;
import dto.MoosageDto;
import dto.UpdateMoosageRequest;
import model.Moosage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.MoosageService;
import service.SessionService;
import util.ResponseUtils;
import util.ValidationUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        if (ValidationUtils.isNullOrEmpty(sessionService.getUserIdByToken(sessionToken))) {
            return ResponseUtils.unauthorized("Invalid session token");
        }

        try {
            List<Moosage> moosages = moosageService.getAllMoosages();
            List<MoosageDto> moosageDtos = moosages.stream()
                    .map(MoosageDto::fromMoosage)
                    .collect(Collectors.toList());
            return ResponseUtils.ok("Moosages retrieved successfully", moosageDtos);
        } catch (Exception e) {
            return ResponseUtils.internalError("Error retrieving moosages: " + e.getMessage());
        }
    }

    // Get a specific moosage by ID. (GET /api/moosages/{id})
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MoosageDto>> getMoosageById(
            @PathVariable Long id,
            @RequestHeader("Session-Token") String sessionToken) {

        // Verify session
        if (ValidationUtils.isNullOrEmpty(sessionService.getUserIdByToken(sessionToken))) {
            return ResponseUtils.unauthorized("Invalid session token");
        }

        Optional<Moosage> moosage = moosageService.getMoosageById(id);

        if (moosage.isPresent()) {
            MoosageDto dto = MoosageDto.fromMoosage(moosage.get());
            return ResponseUtils.ok("Moosage found", dto);
        } else {
            return ResponseUtils.notFound("Moosage not found");
        }
    }

    // Create a new moosage. (POST /api/moosages)
    @PostMapping
    public ResponseEntity<ApiResponse<MoosageDto>> createMoosage(
            @RequestBody CreateMoosageRequest request,
            @RequestHeader("Session-Token") String sessionToken) {

        // Get userID and verify session
        String userId = sessionService.getUserIdByToken(sessionToken);

        if (ValidationUtils.isNullOrEmpty(userId)) {
            return ResponseUtils.unauthorized("Invalid session token");
        }

        // Validate content
        try {
            ValidationUtils.requireNonEmpty(request.getContent(), "Content");
        } catch (IllegalArgumentException e) {
            return ResponseUtils.badRequest(e.getMessage());
        }

        try {
            Moosage moosage = moosageService.createMoosage(request.getContent(), userId);
            MoosageDto dto = MoosageDto.fromMoosage(moosage);
            return ResponseUtils.created("Moosage created successfully", dto);
        } catch (IllegalArgumentException e) {
            return ResponseUtils.badRequest(e.getMessage());
        } catch (Exception e) {
            return ResponseUtils.internalError("Error creating moosage: " + e.getMessage());
        }
    }

    // Toggle like on a moosage. (POST /api/moosages/{id}/like)
    @PostMapping("/{id}/like")
    public ResponseEntity<ApiResponse<MoosageDto>> toggleLike(
            @PathVariable Long id,
            @RequestHeader("Session-Token") String sessionToken) {

        // Get userID and verify session
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (ValidationUtils.isNullOrEmpty(userId)) {
            return ResponseUtils.unauthorized("Invalid session token");
        }

        try {
            Optional<Moosage> moosage = moosageService.toggleLike(id, userId);
            if (moosage.isPresent()) {
                MoosageDto dto = MoosageDto.fromMoosage(moosage.get());
                return ResponseUtils.ok("Like toggled successfully", dto);
            } else {
                return ResponseUtils.notFound("Moosage not found");
            }
        } catch (IllegalArgumentException e) {
            return ResponseUtils.badRequest(e.getMessage());
        } catch (Exception e) {
            return ResponseUtils.internalError("Error toggling like: " + e.getMessage());
        }
    }

    // Update content in a Moosage. (PUT /api/moosages/{id})
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MoosageDto>> updateMoosage(
            @PathVariable Long id,
            @RequestBody UpdateMoosageRequest request,
            @RequestHeader("Session-Token") String sessionToken) {

        // Get userID and verify session
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (ValidationUtils.isNullOrEmpty(userId)) {
            return ResponseUtils.unauthorized("Invalid session token");
        }

        // Validate content
        try {
            ValidationUtils.requireNonEmpty(request.getContent(), "Content");
        } catch (IllegalArgumentException e) {
            return ResponseUtils.badRequest(e.getMessage());
        }

        // Check if user is the author of the moosage
        Optional<Moosage> moosage = moosageService.getMoosageById(id);
        if (moosage.isEmpty()) {
            return ResponseUtils.notFound("Moosage not found");
        }

        if (!moosage.get().getAuthor().getUserID().equals(userId)) {
            return ResponseUtils.forbidden("You can only edit your own moosage");
        }

        try {
            Optional<Moosage> updatedMoosage = moosageService.updateMoosage(id, request.getContent().trim());
            if (updatedMoosage.isPresent()) {
                MoosageDto dto = MoosageDto.fromMoosage(updatedMoosage.get());
                return ResponseUtils.ok("Moosage updated successfully", dto);
            } else {
                return ResponseUtils.notFound("Moosage not found");
            }
        } catch (Exception e) {
            return ResponseUtils.internalError("Error updating moosage: " + e.getMessage());
        }
    }

    // Delete a moosage. (DELETE /api/moosages/{id})
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMoosage(
            @PathVariable Long id,
            @RequestHeader("Session-Token") String sessionToken) {

        // Get userID and verify session
        String userId = sessionService.getUserIdByToken(sessionToken);
        if (ValidationUtils.isNullOrEmpty(userId)) {
            return ResponseUtils.unauthorized("Invalid session token");
        }

        // Check if user is moosage author
        Optional<Moosage> moosage = moosageService.getMoosageById(id);
        if (moosage.isEmpty()) {
            return ResponseUtils.notFound("Moosage not found");
        }

        if (!moosage.get().getAuthor().getUserID().equals(userId)) {
            return ResponseUtils.forbidden("You can only delete your own moosage");
        }

        boolean deleted = moosageService.deleteMoosage(id);
        if (deleted) {
            return ResponseUtils.ok("Moosage deleted successfully");
        } else {
            return ResponseUtils.notFound("Moosage not found");
        }
    }
}
