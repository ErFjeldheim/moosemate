package dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

// Partially implemented by AI (Copilot, Claude sonnet 4.5), 
// in order to efficiently generate boilerplate code.

// Used for JSON serialization and communication between client and server.
public class MoosageDto {
    private Long id;
    private String content;
    private String authorId;  // userID UUID String
    private String authorUsername;  // For display purposes
    private LocalDateTime time;
    private Set<String> likedByUserIds;  // Set of userID UUID Strings
    private boolean edited;  // True if moosage has been edited
    
    public MoosageDto() {
        this.likedByUserIds = new HashSet<>();
        this.edited = false;
    }
    
    public MoosageDto(Long id, String content, String authorId,
                      String authorUsername, LocalDateTime time,
                      Set<String> likedByUserIds) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorUsername = authorUsername;
        this.time = time;
        this.likedByUserIds = likedByUserIds != null ? likedByUserIds : new HashSet<>();
        this.edited = false;
    }
    
    // Factory method for converting from Moosage model
    public static MoosageDto fromMoosage(model.Moosage moosage) {
        if (moosage == null) {
            return null;
        }
        MoosageDto dto = new MoosageDto();
        dto.setId(moosage.getId());
        dto.setContent(moosage.getContent());
        dto.setAuthorId(moosage.getAuthor().getUserID());
        dto.setAuthorUsername(moosage.getAuthor().getUsername());
        dto.setTime(moosage.getTime());
        dto.setLikedByUserIds(new HashSet<>(moosage.getLikedByUserIds()));
        dto.setEdited(moosage.isEdited());
        return dto;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getAuthorId() {
        return authorId;
    }
    
    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }
    
    public String getAuthorUsername() {
        return authorUsername;
    }
    
    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }
    
    public LocalDateTime getTime() {
        return time;
    }
    
    public void setTime(LocalDateTime time) {
        this.time = time;
    }
    
    public Set<String> getLikedByUserIds() {
        // Return defensive copy to avoid exposing internal representation
        return new HashSet<>(likedByUserIds);
    }
    
    public void setLikedByUserIds(Set<String> likedByUserIds) {
        // Create defensive copy to avoid exposing internal representation
        this.likedByUserIds = likedByUserIds != null ? new HashSet<>(likedByUserIds) : new HashSet<>();
    }
    
    public boolean isEdited() {
        return edited;
    }
    
    public void setEdited(boolean edited) {
        this.edited = edited;
    }
    
    @JsonIgnore
    public int getLikeCount() {
        return likedByUserIds.size();
    }
    
    @JsonIgnore
    public boolean isLikedBy(String userId) {
        return likedByUserIds.contains(userId);
    }
}
