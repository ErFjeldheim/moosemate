package dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for CreateMoosageRequest DTO.
 */
public class CreateMoosageRequestTest {

    @Test
    public void testDefaultConstructor() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        
        assertNotNull(request);
        assertNull(request.getContent());
    }

    @Test
    public void testConstructorWithContent() {
        CreateMoosageRequest request = new CreateMoosageRequest("Test content");
        
        assertEquals("Test content", request.getContent());
    }

    @Test
    public void testSetContent() {
        CreateMoosageRequest request = new CreateMoosageRequest();
        request.setContent("New content");
        
        assertEquals("New content", request.getContent());
    }

    @Test
    public void testEmptyContent() {
        CreateMoosageRequest request = new CreateMoosageRequest("");
        
        assertEquals("", request.getContent());
    }

    @Test
    public void testNullContent() {
        CreateMoosageRequest request = new CreateMoosageRequest(null);
        
        assertNull(request.getContent());
    }

    @Test
    public void testLongContent() {
        String longContent = "A".repeat(1000);
        CreateMoosageRequest request = new CreateMoosageRequest(longContent);
        
        assertEquals(longContent, request.getContent());
        assertEquals(1000, request.getContent().length());
    }

    @Test
    public void testContentWithSpecialCharacters() {
        String specialContent = "Test @#$% & *() 特殊文字 emoji 🦌";
        CreateMoosageRequest request = new CreateMoosageRequest(specialContent);
        
        assertEquals(specialContent, request.getContent());
    }

    @Test
    public void testContentWithNewlines() {
        String multilineContent = "Line 1\nLine 2\nLine 3";
        CreateMoosageRequest request = new CreateMoosageRequest(multilineContent);
        
        assertEquals(multilineContent, request.getContent());
        assertTrue(request.getContent().contains("\n"));
    }

    @Test
    public void testModifyingContent() {
        CreateMoosageRequest request = new CreateMoosageRequest("Initial content");
        assertEquals("Initial content", request.getContent());
        
        request.setContent("Modified content");
        assertEquals("Modified content", request.getContent());
    }

    @Test
    public void testWhitespaceContent() {
        CreateMoosageRequest request = new CreateMoosageRequest("   ");
        
        assertEquals("   ", request.getContent());
    }
}
