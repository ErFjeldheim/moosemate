package model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class Moosage {
    private Long id;
    private String content;
    private User author;
    private LocalDateTime time;
    private Set<String> likedByUserIds;  // Each moosage has a set of UserID Strings (users who have liked the moosage)

    public Moosage() {
        this.likedByUserIds = new HashSet<>();
    }

    public Moosage(Long id, String content, User author, LocalDateTime time) {
        this.id = id;
        this.content = content;
        this.author = author;
        this.time = time;
        this.likedByUserIds = new HashSet<>();
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Set<String> getLikedByUserIds() {
        return likedByUserIds;
    }

    public void addLike(String userId) {
        likedByUserIds.add(userId);
    }

    public void removeLike(String userId) {
        likedByUserIds.remove(userId);
    }
}