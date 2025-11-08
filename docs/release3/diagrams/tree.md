## Extended tree ##
```
.
├── CONTRACT.md
├── README.md
├── run-app.bat
├── run-app.sh
├── simple_tree.txt
├── tree.txt
├── docs
│   ├── ADR.md
│   ├── CommitTemplate.md
│   ├── GIT-HowTo.md
│   ├── release1
│   │   ├── ai-tools.md
│   │   └── GroupReflection.md
│   ├── release2
│   │   ├── ai-tools.md
│   │   ├── code-quality.md
│   │   ├── persistence.md
│   │   ├── teamwork.md
│   │   ├── technical-documentation.md
│   │   ├── workflow.md
│   │   └── work-practices.md
│   └── release3
│       ├── ai-tools.md
│       ├── challenges.md
│       ├── contribution.md
│       ├── rest-format.md
│       ├── sustainability.md
│       ├── teamwork.md
│       ├── workpractices.md
│       ├── Diagrams
│       │   ├── diagrams-overview.md
│       │   ├── login-class-diagram.md
│       │   ├── login-class-diagram.svg
│       │   ├── package-diagram.puml
│       │   ├── package-diagram.svg
│       │   ├── post-moosage-class-diagram.md
│       │   ├── post-moosage-class-diagram.svg
│       │   ├── post-moosage-sequence-diagram-1.svg
│       │   ├── post-moosage-sequence-diagram.md
│       │   ├── signup-class-diagram.md
│       │   └── signup-class-diagram.svg
│       └── Screenshots
│           ├── MooseMate-editmoosage.png
│           ├── MooseMate-homepage.png
│           ├── MooseMate-loadingscreen.png
│           ├── MooseMate-loginpage.png
│           └── MooseMate-signuppage.png
└── moosemate
    ├── checkstyle.xml
    ├── pom.xml
    ├── README.md
    ├── spotbugs-exclude.xml
    ├── core
    │   ├── pom.xml
    │   └── src
    │       ├── main
    │       │   └── java
    │       │       ├── dto
    │       │       │   ├── ApiResponse.java
    │       │       │   ├── CreateMoosageRequest.java
    │       │       │   ├── LoginRequest.java
    │       │       │   ├── LoginResponse.java
    │       │       │   ├── MoosageDto.java
    │       │       │   ├── SignUpRequest.java
    │       │       │   ├── UpdateMoosageRequest.java
    │       │       │   └── UserDto.java
    │       │       ├── model
    │       │       │   ├── Moosage.java
    │       │       │   └── User.java
    │       │       ├── service
    │       │       │   ├── ApiClient.java
    │       │       │   └── SessionManager.java
    │       │       └── util
    │       │           ├── IdGenerator.java
    │       │           └── ValidationUtils.java
    │       └── test
    │           └── java
    │               └── model
    │                   └── UserTest.java
    ├── persistence
    │   ├── pom.xml
    │   ├── spotbugs-exclude.xml
    │   └── src
    │       ├── main
    │       │   ├── java
    │       │   │   ├── repository
    │       │   │   │   ├── MoosageRepository.java
    │       │   │   │   └── UserRepository.java
    │       │   │   └── util
    │       │   │       └── JsonFileHandler.java
    │       │   └── resources
    │       │       └── data
    │       │           ├── data.json
    │       │           └── moosages.json
    │       └── test
    │           └── java
    │               ├── repository
    │               │   └── UserRepositoryTest.java
    │               └── util
    │                   └── JsonFileHandlerTest.java
    ├── rest
    │   ├── pom.xml
    │   ├── rest
    │   └── src
    │       ├── main
    │       │   └── java
    │       │       ├── app
    │       │       │   └── RestApplication.java
    │       │       ├── config
    │       │       │   └── CorsConfig.java
    │       │       ├── controller
    │       │       │   ├── AuthController.java
    │       │       │   └── MoosageController.java
    │       │       ├── service
    │       │       │   ├── LoginService.java
    │       │       │   ├── MoosageService.java
    │       │       │   ├── PasswordService.java
    │       │       │   ├── SessionService.java
    │       │       │   ├── SignUpService.java
    │       │       │   └── UserService.java
    │       │       └── util
    │       │           └── ResponseUtils.java
    │       └── test
    │           └── java
    │               └── service
    │                   ├── LoginServiceTest.java
    │                   ├── PasswordServiceTest.java
    │                   ├── SignUpServiceTest.java
    │                   └── UserServiceTest.java
    └── ui
        ├── pom.xml
        └── src
            ├── main
            │   ├── java
            │   │   ├── app
            │   │   │   └── App.java
            │   │   └── controller
            │   │       ├── BaseController.java
            │   │       ├── EditMoosageController.java
            │   │       ├── HomePageController.java
            │   │       ├── LoadingScreenController.java
            │   │       ├── LoginController.java
            │   │       ├── MoosageListCell.java
            │   │       └── SignUpController.java
            │   └── resources
            │       ├── css
            │       │   ├── auth.css
            │       │   ├── common.css
            │       │   ├── editmoosage.css
            │       │   ├── homepage.css
            │       │   └── moosagecell.css
            │       ├── fonts
            │       │   ├── Chewy-Regular.ttf
            │       │   └── Poppins-Medium.ttf
            │       ├── fxml
            │       │   ├── editmoosage.fxml
            │       │   ├── homepage.fxml
            │       │   ├── loadingscreen.fxml
            │       │   ├── loginpage.fxml
            │       │   ├── moosagecell.fxml
            │       │   └── signuppage.fxml
            │       └── images
            │           ├── logout.png
            │           ├── Moosemate-background.png
            │           ├── Moosemate-icon.png
            │           ├── MooseMate-logo-white-transparent-withtext.png
            │           └── MooseMate-logo-white-transparent-withtext-v2.png
            └── test
                ├── java
                │   ├── app
                │   │   ├── AppTestFX.java
                │   │   └── AppTest.java
                │   └── controller
                │       └── AllControllersTestFX.java
                └── resources
                    └── logging.properties

60 directories, 117 files
```