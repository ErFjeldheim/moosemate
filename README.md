# MooseMate project

**MooseMate** is a **multimodule JavaFX application**.  
The source code is located in the [`moosemate`](./moosemate/) folder, which also contains its submodules:

- [core](./moosemate/core/)
- [persistence](./moosemate/persistence/)
- [rest](./moosemate/rest/)
- [ui](./moosemate/ui/)

## Build, run and test

The project is built with Maven in [moosemate](./moosemate/) folder. 

~~~
cd moosemate
mvn clean install
mvn -pl ui javafx:run
~~~

The project is tested by: 

~~~
mvn clean test
~~~

mvn clean test will run TestFX tests as well as spotbugs and surefire.
to test only javaFX coverage in JaCoCo, run:

~~~
mvn test -pl ui -Dtest="*TestFX"
~~~

HTML-link to *display test coverage* is provided as an echo in the terminal before the build data.

## Shortcuts

For simplicity, we have created startup scripts that run both the backend and frontend with a single command. **Make sure you are in the root folder before running the scripts.**
~~~
run-app.bat for **Windows**  
run-app.sh for **Mac/Linux**
~~~

We have also made the app shippable, allowing the user to start **MooseMate** by double-clicking the icon.  
It can be downloaded to any preferred location through the Windows “Browse” dialog during installation.
~~~
create-windows-shortcut.bat for **Windows**  
create-mac-launcher for **Mac/Linux**
~~~

## Dependencies

Core and frameworks
- Java (21)
- JavaFX (23.0.1)
- Spring Boot (3.4.0)
- Spring Framework (6.2.0)

Testing
- TestFX (4.0.18)
- JUnit 5 (5.12.2)
- Hamcrest (3.0)
- Mockito (5.7.0)
- JaCoCo (0.8.12)
- Spotbugs (4.9.5.0)
- Checkstyle (3.3.1)

Utilities and libraries
- Maven Surefire (3.12.1)
- Jackson (2.18.1)
- BCrypt (0.4)

## Eclipse Che

[Open project in Eclipse Che](https://che.stud.ntnu.no/#https://git.ntnu.no/IT1901-2025-groups/gr2524)

**Note:** Since the repository is private, you need to configure a Personal Access Token in Eclipse Che:
1. Go to Github: [Settings → Developer Settings → Personal Access tokens](https://git.ntnu.no/settings/tokens)
2. Generate new token (classic) → Create a token with `read_repository` scope
3. In Eclipse Che: [Profile → User Preferences → Personal Access Tokens](https://che.stud.ntnu.no/dashboard/#/user-preferences?tab=PersonalAccessTokens)
4. Add "https://git.ntnu.no/" under *Git Provider Endpoint*
5. Add Personal Access Token under *Token*
6. The Eclipse Che link above should now work

## Documentation

Documentation is found in the [docs](./docs) folder.

## Architecture Diagram

Architecture Diagram is to be found in the [diagrams](./docs/release3/diagrams/) folder.
```mermaid
flowchart TB
    %% User
    YOU(("User"))

    %% UI Module
    subgraph UI["UI Module (JavaFX)"]
        FXML["
        **signuppage.fxml**
        - usernameField
        - emailField
        - passwordField
        "]
        
        SC["
        **SignUpController.java**
        - usernameField
        - emailField
        - passwordField
        - apiClient: ApiClient
        --
        + handleSignUpButton()
        + handleBackToLoginButton()
        "]
        
        API["
        **ApiClient.java**
        --
        + signup(username, email, password)
        "]
    end

    %% REST Module
    subgraph REST["REST Module (Spring Boot)"]
        AC["
        **AuthController.java**
        @RestController
        --
        + POST /api/auth/signup
        + POST /api/auth/login
        "]
        
        SUS["
        **SignUpService.java**
        --
        + createUser(username, email, password)
        + validateInput()
        "]
        
        PS["
        **PasswordService.java**
        --
        + hashPassword(password)
        + verifyPassword(plain, hash)
        "]
        
        US["
        **UserService.java**
        --
        + saveUser(user)
        + userExists(username)
        + emailExists(email)
        "]
    end

    %% Persistence Module
    subgraph Persistence["Persistence Module"]
        UR["
        **UserRepository.java**
        @Repository
        --
        + save(user)
        + findByUsername(username)
        + findByEmail(email)
        "]
        
        DATA["
        **users.json**
        JSON storage
        "]
    end

    %% Core Module
    subgraph Core["Core Module (Domain)"]
        U["
        **User.java**
        - userId: String
        - username: String
        - email: String
        - password: String
        --
        + getters/setters
        "]
        
        DTO["
        **SignUpRequest.java**
        - username: String
        - email: String
        - password: String
        "]
    end

    %% Connections
    YOU -->|interacts with| FXML
    FXML -->|fx:controller| SC
    SC -->|HTTP POST| API
    API -->|REST API call| AC
    AC -->|calls| SUS
    SUS -->|validates & creates| US
    SUS -->|hashes password| PS
    US -->|uses| UR
    UR -->|reads/writes| DATA
    
    %% Data models
    SUS -->|creates| U
    AC -->|receives/returns| DTO
    UR -->|persists| U
    
    style YOU fill:#e1f5ff
    style UI fill:#fff4e1
    style REST fill:#e8f5e9
    style Persistence fill:#f3e5f5
    style Core fill:#fce4ec
```

**Flow explanation (User Signup):**
1. **User** interacts with `signuppage.fxml` in the JavaFX UI
2. **SignUpController** collects input and calls **ApiClient**
3. **ApiClient** sends HTTP POST request to REST API endpoint
4. **AuthController** receives request and delegates to **SignUpService**
5. **SignUpService** validates input, uses **PasswordService** to hash password
6. **UserService** checks if user/email exists via **UserRepository**
7. **UserRepository** persists the new **User** to `users.json`
8. Response flows back through the layers to UI

For further explanation see [technical-documentation.md](/docs/release2/technical-documentation.md)


