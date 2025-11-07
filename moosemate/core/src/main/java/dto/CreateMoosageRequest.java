package dto;

// Partially implemented by AI (Copilot, Claude sonnet 4.5), 
// in order to efficiently generate boilerplate code.

// Request DTO for creating a new moosage.
public class CreateMoosageRequest {
    private String content;
    
    public CreateMoosageRequest() {
    }
    
    public CreateMoosageRequest(String content) {
        this.content = content;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
}
