package repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.MoosageDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

// Repository class for managing Moosage data persistence using JSON files.
@Repository
public class MoosageRepository {
    
    private static final String DATA_FILE_PATH = "persistence/src/main/resources/data/moosages.json";
    
    private final ObjectMapper objectMapper;
    private final File dataFile;
    private final UserRepository userRepository;

    @Autowired
    public MoosageRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.dataFile = new File(getDataFilePath());
        initializeDataFile();
    }
    
    // Gets the data file path.
    protected String getDataFilePath() {
        // Find project root by walking up until we find the persistence directory
        File dir = new File(System.getProperty("user.dir")).getAbsoluteFile();
        while (dir != null && !new File(dir, "persistence").exists()) {
            dir = dir.getParentFile();
        }
        // Once we find the moosemate root, construct path directly to persistence module
        if (dir != null) {
            File persistenceDir = new File(dir, "persistence");
            File dataFile = new File(persistenceDir, "src/main/resources/data/moosages.json");
            return dataFile.getAbsolutePath();
        }
        return new File(DATA_FILE_PATH).getAbsolutePath();
    }

    // Initializes the data file if it doesn't exist.
    private void initializeDataFile() {
        try {
            if (!dataFile.exists() || dataFile.length() == 0) {
                dataFile.getParentFile().mkdirs();
                MoosageStorage storage = new MoosageStorage();
                objectMapper.writeValue(dataFile, storage);
            }
        } catch (IOException e) {
            System.err.println("Initializing moosages data file failed: " + e.getMessage());
        }
    }

    // Loads the moosage storage from file.
    private MoosageStorage loadStorage() {
        try {
            return objectMapper.readValue(dataFile, MoosageStorage.class);
        } catch (IOException e) {
            System.err.println("Error loading moosages: " + e.getMessage());
            return new MoosageStorage();
        }
    }

    // Saves the moosage storage to file
    private void saveStorage(MoosageStorage storage) {
        try {
            objectMapper.writeValue(dataFile, storage);
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
