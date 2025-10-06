## Prosjektstruktur

The application is divided into four modules: `core`, `persistence`, `rest` and `ui`.  
This multi-module structure prepares the application for API inclusion in upcoming releases. The architecture follows a layered approach: UI → rest (services) → persistence (repository) → data storage.

### `core`
- Contains the domain model for the application.
- **User.java**: Class that holds the user information used to create and store individual users. Includes validation logic for user fields.

### `persistence`
- Handles data persistence and retrieval.
- **UserRepository**: 
  - Saves and retrieves user data from a JSON file.
  - Performs CRUD operations: creating new users, finding existing users, and checking whether usernames and emails already exist.

### `rest`
- Service layer containing business logic.
- Acts as the middle layer between the UI controllers and the persistence layer.
- Handles validation, password hashing, and user operations.
- **LoginService**: Handles user login authentication. Used by `LoginController`.  
- **SignUpService**: Handles new user registration. Used by `SignupController`.  
- **UserService**: General user operations like finding users and checking existence.  
- **PasswordService**: Responsible for password hashing and validation.

### `ui`
- Contains JavaFX-related classes and interface logic.
- **App**: `App.java` is the application's starting point (JavaFX Application).  
- **Controller**:
  - **BaseController**: Abstract class that provides common functionality to other controllers.  
  - **LoginController**: Linked to `Login.fxml`, handles login logic.  
  - **SignupController**: Linked to `Signup.fxml`, handles registration logic.  
  - **HomePageController**: Linked to `HomePage.fxml`, controls the home page logic.  
- Navigation between views is handled directly in the controllers, which manage `Stage` and `Scene` themselves.
