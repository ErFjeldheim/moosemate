package dto;

// Request object for updating a moosage's content.
public class UpdateMoosageRequest {
    private String content;
    
    public UpdateMoosageRequest() {
    }
    
    public UpdateMoosageRequest(String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
