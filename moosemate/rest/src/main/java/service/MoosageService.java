package service;

import model.Moosage;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.MoosageRepository;
import repository.UserRepository;

import java.util.List;
import java.util.Optional;

// Service class for handling moosage business logic.
@Service
public class MoosageService {
    
    private final MoosageRepository moosageRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public MoosageService(MoosageRepository moosageRepository, UserRepository userRepository) {
        this.moosageRepository = moosageRepository;
        this.userRepository = userRepository;
    }
    
    public List<Moosage> getAllMoosages() {
        return moosageRepository.getAllMoosages();
    }
    
    public Optional<Moosage> getMoosageById(Long id) {
        return moosageRepository.getMoosageById(id);
    }
    
    public Moosage createMoosage(String content, String authorId) {
        // Get author username
        Optional<User> userOpt = userRepository.getUserById(authorId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + authorId);
        }
        
        String authorUsername = userOpt.get().getUsername();
        return moosageRepository.createMoosage(content, authorId, authorUsername);
    }
    
    public Optional<Moosage> toggleLike(Long moosageId, String userId) {
        // Verify user exists
        if (userRepository.getUserById(userId).isEmpty()) {
            throw new IllegalArgumentException("User not found with ID: " + userId);
        }
        
        return moosageRepository.toggleLike(moosageId, userId);
    }
    
    public Optional<Moosage> updateMoosage(Long id, String newContent) {
        return moosageRepository.updateMoosage(id, newContent);
    }
    
    public boolean deleteMoosage(Long id) {
        return moosageRepository.deleteMoosage(id);
    }
}
