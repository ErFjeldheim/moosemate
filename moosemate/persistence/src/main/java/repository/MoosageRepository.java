package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.MoosageDto;
import util.JsonFileHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Repository class for managing Moosage data persistence using JSON files.
 */
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

    /**
     * Constructor for testing that accepts a custom JsonFileHandler.
     * 
     * @param userRepository the UserRepository to use
     * @param fileHandler the JsonFileHandler to use
     */
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
    public List<MoosageDto> getAllMoosages() {
        MoosageStorage storage = loadStorage();
        return storage.moosages.stream()
            .sorted(Comparator.comparing(MoosageDto::getTime).reversed())
            .collect(Collectors.toList());
    }

    // Gets a moosage by moosageID.
    public Optional<MoosageDto> getMoosageById(Long id) {
        MoosageStorage storage = loadStorage();
        return storage.moosages.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst();
    }

    // Creates a new moosage
    public MoosageDto createMoosage(String content, String authorId, String authorUsername) {
        MoosageStorage storage = loadStorage();
        
        MoosageDto moosage = new MoosageDto();
        moosage.setId(storage.nextId++);
        moosage.setContent(content);
        moosage.setAuthorId(authorId);
        moosage.setAuthorUsername(authorUsername);
        moosage.setTime(LocalDateTime.now());
        moosage.setLikedByUserIds(new HashSet<>());
        
        storage.moosages.add(moosage);
        saveStorage(storage);
        
        return moosage;
    }

    // Toggles like on a moosage. Returns the updated moosage.
    public Optional<MoosageDto> toggleLike(Long moosageId, String userId) {
        MoosageStorage storage = loadStorage();
        
        Optional<MoosageDto> moosageOpt = storage.moosages.stream()
            .filter(m -> m.getId().equals(moosageId))
            .findFirst();
            
        if (moosageOpt.isPresent()) {
            MoosageDto moosage = moosageOpt.get();
            Set<String> likes = moosage.getLikedByUserIds();
            
            if (likes.contains(userId)) {
                likes.remove(userId);
            } else {
                likes.add(userId);
            }
            
            saveStorage(storage);
            return Optional.of(moosage);
        }
        
        return Optional.empty();
    }

    // Updates the content of a moosage by ID. Returns the updated moosage.
    public Optional<MoosageDto> updateMoosage(Long id, String newContent) {
        MoosageStorage storage = loadStorage();
        
        Optional<MoosageDto> moosageOpt = storage.moosages.stream()
            .filter(m -> m.getId().equals(id))
            .findFirst();
            
        if (moosageOpt.isPresent()) {
            MoosageDto moosage = moosageOpt.get();
            moosage.setContent(newContent);
            moosage.setEdited(true);
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
        public List<MoosageDto> moosages = new ArrayList<>();
        public Long nextId = 1L;
    }
}
