# Release 3 Diagrams Overview

This document provides a brief explanation of the architectural and design diagrams included in Release 3.

**Note:** The Mermaid diagram files (`.mmd` extension) are exported as SVG files. Due to their use of `foreignObject` elements for text rendering, they may not display correctly in standard image viewers. For best viewing experience, open the SVG files in a web browser (e.g., Firefox, Chrome) or use the provided `.mmd` source files with a Mermaid-compatible tool.

## Package Diagram (`package-diagram.puml`)

The package diagram illustrates the high-level modular structure of the MooseMate application. It shows the four main modules and their package organization:

- **Core Module** (green): Contains DTOs, domain models, business services, and utility classes
- **Persistence Module** (blue): Handles data storage with repositories and JSON file utilities
- **REST Module** (orange): Provides the REST API layer with controllers, services, and configuration
- **UI Module** (purple): Contains the JavaFX user interface with controllers and app initialization

The diagram visualizes dependencies between modules, showing that the UI and REST layers both depend on Core, while REST also depends on Persistence for data access.

## Login Class Diagram (`login-class-diagram.mmd`)

This class diagram details the complete login flow architecture across all layers:

- **UI Layer**: `LoginController` extends `BaseController` and handles user input
- **HTTP Client**: `ApiClient` sends login requests to the REST API
- **REST Layer**: `AuthController` processes authentication requests
- **Service Layer**: `LoginService` coordinates authentication logic using `UserService` and `PasswordService`
- **Data Layer**: `UserRepository` retrieves user data from JSON storage

The diagram shows how data flows from the UI through DTOs (`LoginRequest`, `LoginResponse`) and how session management is handled via `SessionManager` and `SessionService`.

## Signup Class Diagram (`signup-class-diagram.mmd`)

Similar to the login diagram, this shows the complete user registration flow:

- **UI Layer**: `SignUpController` handles the registration form
- **REST Layer**: `AuthController` processes signup requests
- **Service Layer**: `SignUpService` orchestrates user creation using `UserService` and `PasswordService`
- **Validation**: `ValidationUtils` ensures username, email, and password meet requirements
- **Data Layer**: `UserRepository` stores new user data

The diagram emphasizes the validation and security aspects of user registration, including password hashing and duplicate checking.

## Post Moosage Class Diagram (`post-moosage-class-diagram.mmd`)

This class diagram illustrates how users create posts (moosages) in the system:

- **UI Layer**: `HomePageController` manages the post creation interface
- **REST Layer**: `MoosageController` handles moosage creation requests
- **Service Layer**: `MoosageService` coordinates moosage creation logic
- **Data Layer**: `MoosageRepository` persists moosages to JSON storage

The diagram shows how `Moosage` entities relate to `User` entities and how DTOs (`MoosageDto`, `CreateMoosageRequest`) transfer data between layers.

## Post Moosage Sequence Diagram (`post-moosage-sequence-diagram.mmd`)

This sequence diagram provides a detailed step-by-step view of the moosage creation process:

1. User enters text and clicks the "Post" button
2. UI validates input and sends request via `ApiClient`
3. REST API authenticates the user via session token
4. Service layer processes the request and coordinates with repository
5. Repository loads existing storage, creates new moosage, and saves to JSON
6. Response flows back through layers to update the UI

The diagram emphasizes the temporal flow of operations, including validation steps, data transformation, and error handling points.
