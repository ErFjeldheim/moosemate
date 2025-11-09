package config;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Comprehensive tests for CorsConfig configuration class.
 * Tests CORS (Cross-Origin Resource Sharing) configuration.
 */
public class CorsConfigTest {

    private CorsConfig corsConfig = new CorsConfig();
    private WebMvcConfigurer configurer;
    private @NonNull TestCorsRegistry testRegistry = new TestCorsRegistry();

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
        public @NonNull CorsRegistration addMapping(@NonNull String pathPattern) {
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
        public @NonNull CorsRegistration allowedOrigins(@NonNull String... origins) {
            this.allowedOrigins = origins;
            return this;
        }

        @Override
        public @NonNull CorsRegistration allowedMethods(@NonNull String... methods) {
            this.allowedMethods = methods;
            return this;
        }

        @Override
        public @NonNull CorsRegistration allowedHeaders(@NonNull String... headers) {
            this.allowedHeaders = headers;
            return this;
        }

        @Override
        public @NonNull CorsRegistration allowCredentials(boolean allowCredentials) {
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
