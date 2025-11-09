package dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class UpdateMoosageRequestTest {

    @Test
    void testDefaultConstructor() {
        UpdateMoosageRequest request = new UpdateMoosageRequest();
        assertNotNull(request);
        assertNull(request.getContent());
    }

    @Test
    void testParameterizedConstructor() {
        UpdateMoosageRequest request = new UpdateMoosageRequest("Updated content");
        assertEquals("Updated content", request.getContent());
    }

    @Test
    void testSetContent() {
        UpdateMoosageRequest request = new UpdateMoosageRequest();
        request.setContent("New moosage content");
        assertEquals("New moosage content", request.getContent());
    }

    @Test
    void testSetContentOverride() {
        UpdateMoosageRequest request = new UpdateMoosageRequest("Original");
        request.setContent("Modified");
        assertEquals("Modified", request.getContent());
    }

    @Test
    void testSetContentEmpty() {
        UpdateMoosageRequest request = new UpdateMoosageRequest("Some content");
        request.setContent("");
        assertEquals("", request.getContent());
    }

    @Test
    void testSetContentNull() {
        UpdateMoosageRequest request = new UpdateMoosageRequest("Some content");
        request.setContent(null);
        assertNull(request.getContent());
    }
}
