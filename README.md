# MooseMate project

MooseMate is a simple JavaFX application. The source code is located in the [moosemate](./moosemate/src/) folder.

## Build, run and test

The project is built with Maven. 

~~~
cd moosemate
mvn clean install
mvn javafx:run
~~~

The project is tested by: 

~~~
mvn clean test
~~~

to include javaFX coverage in JaCoCo, run: 
~~~
mvn test -Dtest="*TestFX"
~~~



HTML-link to test coverage is provided in the terminal.

## Dependencies

- Java (21.0.8)
- Maven (3.9.11)
- JUnit 5 (5.12.2)
- Jackson (2.17.2)
- JavaFX (21)
- TestFX (4.0.18)

## Eclipse Che

[Open project in Eclipse Che](https://che.stud.ntnu.no/#https://git.ntnu.no/IT1901-2025-groups/gr2524)

**Note**: To build the project in Eclipse Che, navigate to pom.xml and change the all lines with Java version 21 to Java version 17. Eclipse Che does not support Java versions newer than 17.

## Documentation

Documentation is found in the [docs](./docs) folder.'

## License

TBA
