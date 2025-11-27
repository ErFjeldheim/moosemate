# MooseMate

**MooseMate** is a social media application built with JavaFX and Spring Boot, designed as a multimodule Maven project. It provides users with a platform to share posts, interact with content, and connect with others.

## Table of Contents

- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
- [Build and Test](#build-and-test)
- [Running the Application](#running-the-application)
- [Shortcuts](#shortcuts)
- [Dependencies](#dependencies)
- [Eclipse Che](#eclipse-che)
- [Documentation](#documentation)
- [Architecture Diagram](#architecture-diagram)

## Project Structure

The source code is located in the [`moosemate`](./moosemate/) folder, organized into four main modules:

- **[core](./moosemate/core/)** - Core business logic, models, and DTOs
- **[persistence](./moosemate/persistence/)** - Data persistence layer and repositories
- **[rest](./moosemate/rest/)** - Spring Boot REST API backend
- **[ui](./moosemate/ui/)** - JavaFX user interface

## Prerequisites

Before running MooseMate, ensure you have the following installed:

- **Java JDK 21** or higher
- **Apache Maven 3.8+**
- **Git** (for cloning the repository)

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/ErFjeldheim/moosemate.git
   cd moosemate
   ```

2. **Build the project**
   ```bash
   cd moosemate
   mvn clean install
   ```

### Running Tests

Make sure backend/springboot is **not** running when running the following command, as the testing will fail. Execute the test suite with: 

```bash
mvn clean test
```

After running tests, HTML links to view the **test coverage reports** will be displayed in the terminal output before the build summary. One covers persistence, rest and core, while the other covers UI. There are also separate links to each of these for macOS/linux and windows users respectively. 

## Running the Application

MooseMate requires both the backend and frontend to be running. You'll need **two separate terminal windows**.

### Manual Start

**Terminal 1 - Start the Backend (Spring Boot):**
```bash
cd moosemate/rest
mvn spring-boot:run
```

**Terminal 2 - Start the Frontend (JavaFX):**
```bash
cd moosemate/ui
mvn javafx:run
```

> **Note:** Wait for the backend to fully start before launching the frontend.

## Shortcuts

For convenience, we provide startup scripts that launch both backend and frontend automatically.

### Quick Start Scripts

**Make sure you are in the project root folder** before running these scripts:

- **Windows (powershell)**
  ```bash
  .\run-app.bat
  ```

- **Mac/Linux:**
  ```bash
  ./run-app.sh
  ```

### Desktop Launcher Scripts

Create a desktop shortcut or launcher for easy access:

- **Windows (powershell):**
  ```bash
  .\create-windows-shortcut.bat
  ```

- **Mac/Linux:**
  ```bash
  ./create-mac-launcher
  ```

These scripts allow you to save MooseMate to a location by own choice on your computer, and start it by double-clicking an icon on your desktop.

## Dependencies

### Core and Frameworks
- Java (21)
- JavaFX (23.0.1)
- Spring Boot (3.4.0)
- Spring Framework (6.2.0)

### Testing
- TestFX (4.0.18)
- JUnit 5 (5.12.2)
- Hamcrest (3.0)
- Mockito (5.7.0)
- JaCoCo (0.8.12)
- Spotbugs (4.9.5.0)
- Checkstyle (3.3.1)

### Utilities and Libraries
- Maven Surefire (3.12.1)
- Jackson (2.18.1)
- BCrypt (0.4)

## Documentation

Documentation is found in the [docs](./docs) folder, including:

- [Architecture Decision Records (ADR)](./docs/ADR.md)
- [Git Workflow Guidelines](./docs/GIT-HowTo.md)
- [Commit Template](./docs/CommitTemplate.md)
- Release documentation ([release1](./docs/release1/), [release2](./docs/release2/), [release3](./docs/release3/))

## Architecture Diagram

For a detailed file tree, see [architecture-diagram.md](./docs/release3/diagrams/architecture-diagram.md)

```
.
├── docs
│   ├── release1
│   ├── release2
│   └── release3
│       ├── diagrams
│       └── screenshots
└── moosemate
    ├── core
    │   └── src
    │       ├── main
    │       │   └── java
    │       │       ├── dto
    │       │       ├── model
    │       │       ├── service
    │       │       └── util
    │       └── test
    │           └── java
    │               └── model
    ├── persistence
    │   └── src
    │       ├── main
    │       │   ├── java
    │       │   │   ├── repository
    │       │   │   └── util
    │       │   └── resources
    │       │       └── data
    │       └── test
    │           └── java
    │               ├── repository
    │               └── util
    ├── rest
    │   ├── rest
    │   └── src
    │       ├── main
    │       │   └── java
    │       │       ├── app
    │       │       ├── config
    │       │       ├── controller
    │       │       ├── service
    │       │       └── util
    │       └── test
    │           └── java
    │               └── service
    └── ui
        └── src
            ├── main
            │   ├── java
            │   │   ├── app
            │   │   └── controller
            │   └── resources
            │       ├── css
            │       ├── fonts
            │       ├── fxml
            │       └── images
            └── test
                ├── java
                │   ├── app
                │   └── controller
                └── resources
```

---

**MooseMate** - Developed by Group gr2524 for IT1901, Fall 2025.
Original repository: https://git.ntnu.no/IT1901-2025-groups/gr2524

Forked for further improvements.
