# Refactoring project. Transitioning to multi-modularity.

One of the biggest issues we encountered was in release 2, when we had to refactor the entire project to make a multi-modular application. In release 1 we only had one pom-file that were running through the App class. According to the release 2 task-description, that had to change. So we had to incorporate multiple modules and additional pom files in order to be able to run rest, core, persitance and ui seperately. This was easier said than done.

We believe it would have been easier if we were informed of the requirement from the start. However, we did learn that refactoring may require a lot of work, emphasizing that thoroughness and structure is key when initializing the project-structure.

More specifically, we spent a few full work days making this implementation work seemless. It was a huge challenge modifying the POM files to work across the application. We appreciated individual assignment 1, as it provided valuable experience.

Originally, we tried to implement this manually in pairs, but quickly realized that this task was more complicated than we anticipated. We decided on the following standup that the entire group needed to work on refactoring. In addition to online resources like geek4geeks, we also took advantage of the integrated CoPilot to help implement the correct syntax in certain classes, as it could be confusing when relocating imports, methods etc.

# Configuring tests.

Working with Test classes was also a huge challenge that cost us a lot of time. We had to install a few different dependencies and make them work with eachother. For instance we used TestFX as a testing framework and JaCoCo for measuring our test-coverage. After a lot of trial and error, deliberately configuring older versions of the frameworks to make them compatible and tweaking the dependency formats, we got it working. We landed on the decision on skipping javaFX tests on macOS as it caused a lot of issues on one of the peer's computer.

Antoher issue was that JaCoCo did not register UI coverage properly, which led us to run UI tests separately. At one point, the test suite took over 3.5 minutes to complete because of the large number of tests. We managed to optimize execution time significantly while maintaining good coverage by simplifying and restructuring the tests. All TestFX tests were grouped in a single file, while backend tests were organized in another. Since our prior experience with testing and TestFX was limited, we experienced a steep learning curve. 

The test process is now streamlined, and all tests can be executed efficiently by running:

```bash
mvn clean test 
mvn clean install
```