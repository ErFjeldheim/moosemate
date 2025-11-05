package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import model.Moosage;
import model.User;
import util.JsonFileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//Repository class for managing Moosage data persistence using JSON files.
@Repository
public class MoosageRepository {
    
    private static final String DATA_FILE_PATH = "persistence/src/main/resources/data/moosages.json";
    
    private final JsonFileHandler fileHandler;
    private final File dataFile;
    private final UserRepository userRepository;

    @Autowired
    public MoosageRepository(UserRepository userRepository) {
        this(userRepository, new JsonFileHandler());
    }

    //Constructor for testing that accepts a custom JsonFileHandler.
    public MoosageRepository(UserRepository userRepository, JsonFileHandler fileHandler) {
        this.userRepository = userRepository;
        this.fileHandler = fileHandler;
        
        // Register JavaTimeModule for LocalDateTime serialization
        ObjectMapper mapper = fileHandler.getObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        
        this.dataFile = new File(fileHandler.getDataFilePath(DATA_FILE_PATH));
        initializeDataFile();
    }

    /**
     * Initializes the data file if it doesn't exist.
     */
    private void initializeDataFile() {
        try {
            MoosageStorage storage = new MoosageStorage();
            fileHandler.initializeDataFile(dataFile, storage);
        } catch (IOException e) {
            System.err.println("Initializing moosages data file failed: " + e.getMessage());
        }
    }

    // Loads the moosage storage from file.
    private MoosageStorage loadStorage() {
        try {
            return fileHandler.readJsonFromFile(dataFile, new TypeReference<MoosageStorage>() {});
        } catch (IOException e) {
            System.err.println("Error loading moosages: " + e.getMessage());
            return new MoosageStorage();
        }
    }

    // Saves the moosage storage to file
    private void saveStorage(MoosageStorage storage) {
        try {
            fileHandler.writeJsonToFile(dataFile, storage);
        } catch (IOException e) {
            System.err.println("Error saving moosages: " + e.getMessage());
        }
    }

    // Gets all moosages sorted by time (newest first)
    public List<Moosage> getAllMoosages() {
        MoosageStorage storage = loadStorage();
        return storage.moosages.stream()
            .sorted(Comparator.comparing(Moosage::getTime).reversed())
            .collect(Collectors.toList());
    }

    // Gets a moosage by moosageID.
    public Optional<Moosage> getMoosageById(Long id) {
        MoosageStorage storage = loadStorage();
        return storage.moosages.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst();
    }

    // Creates a new moosage
    public Moosage createMoosage(String content, String authorId, String authorUsername) {
        MoosageStorage storage = loadStorage();
        
        // Find the User object from repository
        Optional<User> authorOpt = userRepository.getUserById(authorId);
        if (!authorOpt.isPresent()) {
            throw new IllegalArgumentException("Author user not found: " + authorId);
        }
        
        Moosage moosage = new Moosage(
            storage.nextId++,
            content,
            authorOpt.get(),
            LocalDateTime.now()
        );
        
        storage.moosages.add(moosage);
        saveStorage(storage);
        
        return moosage;
    }

    // Toggles like on a moosage. Returns the updated moosage.
    public Optional<Moosage> toggleLike(Long moosageId, String userId) {
        MoosageStorage storage = loadStorage();
        
        Optional<Moosage> moosageOpt = storage.moosages.stream()
            .filter(m -> m.getId().equals(moosageId))
            .findFirst();
            
        if (moosageOpt.isPresent()) {
            Moosage moosage = moosageOpt.get();
            Set<String> likes = moosage.getLikedByUserIds();
            
            if (likes.contains(userId)) {
                moosage.removeLike(userId);
            } else {
                moosage.addLike(userId);
            }
            
            saveStorage(storage);
            return Optional.of(moosage);
        }
        
        return Optional.empty();
    }

    // Updates the content of a moosage by ID. Returns the updated moosage.
    public Optional<Moosage> updateMoosage(Long id, String newContent) {
        MoosageStorage storage = loadStorage();
        
        Optional<Moosage> moosageOpt = storage.moosages.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst();
            
        if (moosageOpt.isPresent()) {
            Moosage moosage = moosageOpt.get();
            moosage.setContent(newContent);
            saveStorage(storage);
            return Optional.of(moosage);
        }
        
        return Optional.empty();
    }

    // Deletes a moosage by ID.
    public boolean deleteMoosage(Long id) {
        MoosageStorage storage = loadStorage();
        boolean removed = storage.moosages.removeIf(m -> m.getId().equals(id));
        if (removed) {
            saveStorage(storage);
        }
        return removed;
    }

    // Storage class for JSON serialization.
    private static class MoosageStorage {
        public List<Moosage> moosages = new ArrayList<>();
        public Long nextId = 1L;
    }
}
