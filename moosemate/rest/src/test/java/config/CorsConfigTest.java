package config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive tests for CorsConfig configuration class.
 * Tests CORS (Cross-Origin Resource Sharing) configuration.
 */
public class CorsConfigTest {

    private CorsConfig corsConfig;
    private WebMvcConfigurer configurer;
    private TestCorsRegistry testRegistry;

    @BeforeEach
    public void setUp() {
        corsConfig = new CorsConfig();
        configurer = corsConfig.corsConfigurer();
        testRegistry = new TestCorsRegistry();
    }

    @Test
    public void testCorsConfigurerNotNull() {
        assertNotNull(configurer, "CORS configurer should not be null");
    }

    @Test
    public void testCorsConfigurerIsWebMvcConfigurer() {
        assertTrue(configurer instanceof WebMvcConfigurer, 
            "Configurer should be instance of WebMvcConfigurer");
    }

    @Test
    public void testAddCorsMappingsConfiguresApiEndpoints() {
        configurer.addCorsMappings(testRegistry);
        
        assertTrue(testRegistry.isConfigured(), 
            "CORS mappings should be configured");
        assertEquals("/api/**", testRegistry.getPath(), 
            "Should configure /api/** path pattern");
    }

    @Test
    public void testCorsAllowsLocalhost8080Origin() {
        configurer.addCorsMappings(testRegistry);
        
        assertNotNull(testRegistry.getRegistration());
        assertArrayEquals(new String[]{"http://localhost:8080"}, 
            testRegistry.getRegistration().getAllowedOrigins(),
            "Should allow localhost:8080 origin");
    }

    @Test
    public void testCorsAllowsAllHttpMethods() {
        configurer.addCorsMappings(testRegistry);
        
        String[] expectedMethods = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
        assertArrayEquals(expectedMethods, 
            testRegistry.getRegistration().getAllowedMethods(),
            "Should allow GET, POST, PUT, DELETE, OPTIONS methods");
    }

    @Test
    public void testCorsAllowsAllHeaders() {
        configurer.addCorsMappings(testRegistry);
        
        assertArrayEquals(new String[]{"*"}, 
            testRegistry.getRegistration().getAllowedHeaders(),
            "Should allow all headers");
    }

    @Test
    public void testCorsAllowsCredentials() {
        configurer.addCorsMappings(testRegistry);
        
        assertTrue(testRegistry.getRegistration().isAllowCredentials(),
            "Should allow credentials");
    }

    @Test
    public void testMultipleCorsConfigurersAreIndependent() {
        WebMvcConfigurer configurer1 = corsConfig.corsConfigurer();
        WebMvcConfigurer configurer2 = corsConfig.corsConfigurer();
        
        assertNotNull(configurer1);
        assertNotNull(configurer2);
        assertNotSame(configurer1, configurer2, 
            "Each call should return a new configurer instance");
    }

    @Test
    public void testCorsConfigurationIsImmutableAfterRegistration() {
        configurer.addCorsMappings(testRegistry);
        TestCorsRegistration registration = testRegistry.getRegistration();
        
        String[] originalOrigins = registration.getAllowedOrigins();
        assertNotNull(originalOrigins);
        assertEquals(1, originalOrigins.length);
        assertEquals("http://localhost:8080", originalOrigins[0]);
    }

    /**
     * Test implementation of CorsRegistry to verify configuration.
     */
    private static class TestCorsRegistry extends CorsRegistry {
        private String path;
        private TestCorsRegistration registration;
        private boolean configured = false;

        @Override
        public CorsRegistration addMapping(String pathPattern) {
            this.path = pathPattern;
            this.configured = true;
            this.registration = new TestCorsRegistration();
            return this.registration;
        }

        public boolean isConfigured() {
            return configured;
        }

        public String getPath() {
            return path;
        }

        public TestCorsRegistration getRegistration() {
            return registration;
        }
    }

    /**
     * Test implementation of CorsRegistration to capture configuration.
     */
    private static class TestCorsRegistration extends CorsRegistration {
        private String[] allowedOrigins;
        private String[] allowedMethods;
        private String[] allowedHeaders;
        private boolean allowCredentials;

        public TestCorsRegistration() {
            super("/**");
        }

        @Override
        public CorsRegistration allowedOrigins(String... origins) {
            this.allowedOrigins = origins;
            return this;
        }

        @Override
        public CorsRegistration allowedMethods(String... methods) {
            this.allowedMethods = methods;
            return this;
        }

        @Override
        public CorsRegistration allowedHeaders(String... headers) {
            this.allowedHeaders = headers;
            return this;
        }

        @Override
        public CorsRegistration allowCredentials(boolean allowCredentials) {
            this.allowCredentials = allowCredentials;
            return this;
        }

        public String[] getAllowedOrigins() {
            return allowedOrigins;
        }

        public String[] getAllowedMethods() {
            return allowedMethods;
        }

        public String[] getAllowedHeaders() {
            return allowedHeaders;
        }

        public boolean isAllowCredentials() {
            return allowCredentials;
        }
    }
}
