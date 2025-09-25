package moosemate.ui;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for SignUpController
 * Credits: Claude Sonnet 4
 */
public class SignUpControllerTest {

    @Test
    public void testControllerCanBeInstantiated() {
        SignUpController controller = new SignUpController();
        assertNotNull(controller);
    }

    @Test
    public void testControllerExtendsBaseController() {
        SignUpController controller = new SignUpController();
        assertTrue(controller instanceof BaseController);
    }

    @Test
    public void testControllerIsCorrectType() {
        SignUpController controller = new SignUpController();
        assertTrue(controller instanceof SignUpController);
    }

    @Test
    public void testMultipleInstancesCanBeCreated() {
        SignUpController controller1 = new SignUpController();
        SignUpController controller2 = new SignUpController();
        
        assertNotNull(controller1);
        assertNotNull(controller2);
        assertNotSame(controller1, controller2);
    }
    
    @Test
    public void testControllerHasRequiredMethods() {
        Class<SignUpController> clazz = SignUpController.class;
        
        // Test that required methods exist with correct signatures
        assertDoesNotThrow(() -> {
            Method signUpMethod = clazz.getDeclaredMethod("handleSignUpButton", javafx.event.ActionEvent.class);
            assertNotNull(signUpMethod);
            assertTrue(Modifier.isPrivate(signUpMethod.getModifiers()));
            assertNotNull(signUpMethod.getAnnotation(javafx.fxml.FXML.class));
        });
        
        assertDoesNotThrow(() -> {
            Method backToLoginMethod = clazz.getDeclaredMethod("handleBackToLoginButton", javafx.event.ActionEvent.class);
            assertNotNull(backToLoginMethod);
            assertTrue(Modifier.isPrivate(backToLoginMethod.getModifiers()));
            assertNotNull(backToLoginMethod.getAnnotation(javafx.fxml.FXML.class));
        });
    }
    
    @Test
    public void testControllerFields() {
        Class<SignUpController> clazz = SignUpController.class;
        
        // Test that all FXML fields exist with correct types and annotations
        assertDoesNotThrow(() -> {
            Field usernameField = clazz.getDeclaredField("usernameField");
            assertNotNull(usernameField);
            assertEquals("javafx.scene.control.TextField", usernameField.getType().getName());
            assertTrue(Modifier.isPrivate(usernameField.getModifiers()));
            assertNotNull(usernameField.getAnnotation(javafx.fxml.FXML.class));
        });
        
        assertDoesNotThrow(() -> {
            Field emailField = clazz.getDeclaredField("emailField");
            assertNotNull(emailField);
            assertEquals("javafx.scene.control.TextField", emailField.getType().getName());
            assertTrue(Modifier.isPrivate(emailField.getModifiers()));
            assertNotNull(emailField.getAnnotation(javafx.fxml.FXML.class));
        });
        
        assertDoesNotThrow(() -> {
            Field passwordField = clazz.getDeclaredField("passwordField");
            assertNotNull(passwordField);
            assertEquals("javafx.scene.control.PasswordField", passwordField.getType().getName());
            assertTrue(Modifier.isPrivate(passwordField.getModifiers()));
            assertNotNull(passwordField.getAnnotation(javafx.fxml.FXML.class));
        });
    }
    
    @Test
    public void testControllerInheritance() {
        SignUpController controller = new SignUpController();
        
        // Verify inheritance
        assertTrue(BaseController.class.isAssignableFrom(SignUpController.class));
        
        // Verify package
        assertEquals("moosemate.ui", controller.getClass().getPackage().getName());
    }
    
    @Test
    public void testControllerClassName() {
        SignUpController controller = new SignUpController();
        assertEquals("moosemate.ui.SignUpController", controller.getClass().getName());
    }
    
    @Test
    public void testControllerInheritedMethods() {
        // Test that SignUpController inherits BaseController methods
        // These are protected methods in BaseController, so we check the parent class
        assertDoesNotThrow(() -> {
            BaseController.class.getDeclaredMethod("showError", String.class);
        });
        
        assertDoesNotThrow(() -> {
            BaseController.class.getDeclaredMethod("showSuccess", String.class);
        });
        
        assertDoesNotThrow(() -> {
            BaseController.class.getDeclaredMethod("clearError");
        });
        
        assertDoesNotThrow(() -> {
            BaseController.class.getDeclaredMethod("navigateToOtherPage", 
                javafx.event.ActionEvent.class, String.class, String.class);
        });
        
        assertDoesNotThrow(() -> {
            BaseController.class.getDeclaredMethod("navigateToOtherPageWithSuccess", 
                javafx.event.ActionEvent.class, String.class, String.class, String.class);
        });
    }

    @Test
    public void testClassStructure() {
        SignUpController controller = new SignUpController();
        Class<?> clazz = controller.getClass();
        
        // Test class is public
        assertTrue(Modifier.isPublic(clazz.getModifiers()));
        
        // Test class is not abstract or final
        assertFalse(Modifier.isAbstract(clazz.getModifiers()));
        assertFalse(Modifier.isFinal(clazz.getModifiers()));
        
        // Test class has public no-args constructor
        assertDoesNotThrow(() -> {
            clazz.getConstructor();
        });
    }

    @Test
    public void testMethodParameterTypes() {
        Class<SignUpController> clazz = SignUpController.class;
        
        // Test parameter types for event handler methods
        assertDoesNotThrow(() -> {
            Method signUpMethod = clazz.getDeclaredMethod("handleSignUpButton", javafx.event.ActionEvent.class);
            Class<?>[] paramTypes = signUpMethod.getParameterTypes();
            assertEquals(1, paramTypes.length);
            assertEquals(javafx.event.ActionEvent.class, paramTypes[0]);
        });
        
        assertDoesNotThrow(() -> {
            Method backToLoginMethod = clazz.getDeclaredMethod("handleBackToLoginButton", javafx.event.ActionEvent.class);
            Class<?>[] paramTypes = backToLoginMethod.getParameterTypes();
            assertEquals(1, paramTypes.length);
            assertEquals(javafx.event.ActionEvent.class, paramTypes[0]);
        });
    }

    @Test
    public void testFieldCount() {
        Class<SignUpController> clazz = SignUpController.class;
        
        // Count declared fields with FXML annotation
        Field[] declaredFields = clazz.getDeclaredFields();
        long fxmlFields = java.util.Arrays.stream(declaredFields)
            .filter(f -> f.isAnnotationPresent(javafx.fxml.FXML.class))
            .count();
        
        // SignUpController should have 3 FXML fields
        assertEquals(3, fxmlFields);
    }

    @Test
    public void testMethodAnnotations() {
        Class<SignUpController> clazz = SignUpController.class;
        
        // Count declared methods with FXML annotation
        Method[] declaredMethods = clazz.getDeclaredMethods();
        long fxmlMethods = java.util.Arrays.stream(declaredMethods)
            .filter(m -> m.isAnnotationPresent(javafx.fxml.FXML.class))
            .count();
        
        // SignUpController should have 2 FXML methods
        assertEquals(2, fxmlMethods);
    }
}