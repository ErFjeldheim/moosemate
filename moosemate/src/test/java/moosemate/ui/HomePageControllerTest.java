package moosemate.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for HomePageController
 * Credits: Claude Sonnet 4
 */
public class HomePageControllerTest {

    private HomePageController controller;

    @BeforeEach
    public void setUp() {
        controller = new HomePageController();
    }

    @Test
    public void testControllerInstantiation() {
        assertNotNull(controller);
    }

    @Test
    public void testControllerExtendsBaseController() {
        assertTrue(controller instanceof BaseController);
    }

    @Test
    public void testControllerCanBeCreatedMultipleTimes() {
        HomePageController controller1 = new HomePageController();
        HomePageController controller2 = new HomePageController();
        
        assertNotNull(controller1);
        assertNotNull(controller2);
        assertNotSame(controller1, controller2);
    }

    @Test
    public void testControllerIsInstanceOfCorrectClass() {
        assertTrue(controller instanceof HomePageController);
        assertTrue(controller instanceof BaseController);
    }

    @Test
    public void testNoExceptionOnInstantiation() {
        assertDoesNotThrow(() -> new HomePageController());
    }
    
    @Test
    public void testControllerHasRequiredMethods() {
        Class<HomePageController> clazz = HomePageController.class;
        
        // Test that required methods exist with correct signatures and annotations
        assertDoesNotThrow(() -> {
            Method logoutMethod = clazz.getDeclaredMethod("handleLogoutButton", javafx.event.ActionEvent.class);
            assertNotNull(logoutMethod);
            assertTrue(Modifier.isPrivate(logoutMethod.getModifiers()));
            assertNotNull(logoutMethod.getAnnotation(javafx.fxml.FXML.class));
        });
    }
    
    @Test
    public void testControllerInheritance() {
        // Verify inheritance
        assertTrue(BaseController.class.isAssignableFrom(HomePageController.class));
        
        // Verify package
        assertEquals("moosemate.ui", controller.getClass().getPackage().getName());
    }
    
    @Test
    public void testControllerClassName() {
        assertEquals("moosemate.ui.HomePageController", controller.getClass().getName());
    }
    
    @Test
    public void testControllerInheritedMethods() {
        // Test that HomePageController inherits BaseController methods
        // These are protected methods in BaseController, so we need to check the parent class
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
        Class<HomePageController> clazz = HomePageController.class;
        
        // Test parameter types for event handler methods
        assertDoesNotThrow(() -> {
            Method logoutMethod = clazz.getDeclaredMethod("handleLogoutButton", javafx.event.ActionEvent.class);
            Class<?>[] paramTypes = logoutMethod.getParameterTypes();
            assertEquals(1, paramTypes.length);
            assertEquals(javafx.event.ActionEvent.class, paramTypes[0]);
        });
    }

    @Test
    public void testMethodAnnotations() {
        Class<HomePageController> clazz = HomePageController.class;
        
        // Count declared methods with FXML annotation
        Method[] declaredMethods = clazz.getDeclaredMethods();
        long fxmlMethods = java.util.Arrays.stream(declaredMethods)
            .filter(m -> m.isAnnotationPresent(javafx.fxml.FXML.class))
            .count();
        
        // HomePageController should have 1 FXML method
        assertEquals(1, fxmlMethods);
    }

    @Test
    public void testInheritanceHierarchy() {
        // Verify full inheritance chain
        assertTrue(controller instanceof HomePageController);
        assertTrue(controller instanceof BaseController);
        assertTrue(controller instanceof Object);
        
        // Verify direct superclass
        assertEquals(BaseController.class, HomePageController.class.getSuperclass());
    }

    @Test
    public void testControllerMethodReturnTypes() {
        Class<HomePageController> clazz = HomePageController.class;
        
        // Test return types of FXML methods
        assertDoesNotThrow(() -> {
            Method logoutMethod = clazz.getDeclaredMethod("handleLogoutButton", javafx.event.ActionEvent.class);
            assertEquals(void.class, logoutMethod.getReturnType());
        });
    }

    @Test
    public void testControllerModifiers() {
        Class<HomePageController> clazz = HomePageController.class;
        
        // Test class modifiers
        int modifiers = clazz.getModifiers();
        assertTrue(Modifier.isPublic(modifiers));
        assertFalse(Modifier.isAbstract(modifiers));
        assertFalse(Modifier.isFinal(modifiers));
        assertFalse(Modifier.isInterface(modifiers));
    }

    @Test
    public void testNoFieldsDefinedInController() {
        Class<HomePageController> clazz = HomePageController.class;
        
        // HomePageController has no declared fields (all UI elements managed by BaseController)
        Field[] declaredFields = clazz.getDeclaredFields();
        assertEquals(0, declaredFields.length);
    }

    @Test
    public void testMethodCount() {
        Class<HomePageController> clazz = HomePageController.class;
        
        // Count declared methods (should have 1 method: handleLogoutButton)
        Method[] declaredMethods = clazz.getDeclaredMethods();
        long realMethods = java.util.Arrays.stream(declaredMethods)
            .filter(m -> !m.isSynthetic())  // Exclude synthetic methods
            .count();
        
        assertEquals(1, realMethods);
    }

    @Test
    public void testControllerSimplicity() {
        Class<HomePageController> clazz = HomePageController.class;
        
        // Test that this is a simple controller with minimal complexity
        // It should only have the one logout method and inherit everything else
        Method[] declaredMethods = clazz.getDeclaredMethods();
        boolean hasLogoutMethod = false;
        
        for (Method method : declaredMethods) {
            if (!method.isSynthetic() && method.getName().equals("handleLogoutButton")) {
                hasLogoutMethod = true;
                break;
            }
        }
        
        assertTrue(hasLogoutMethod, "Controller should have handleLogoutButton method");
    }
}