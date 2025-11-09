# Challenges

# Refactoring project. Transitioning to multi-modularity.

One of the biggest issues we encountered was in release 2, when we had to refactor the entire project to make a multi-modular application. In release 1 we only had one pom-file that were running through the App class. According to the release 2 task-description, that had to change. We had to incorporate multiple modules and additional pom files in order to be able to run rest, core, persitance and ui seperately. This was easier said than done.

We believe it would have been easier if we were informed of the requirement from the start. However, we did learn that refactoring may require a lot of work, emphasizing that thoroughness and structure is key when initializing the project-structure.

More specifically, we spent a few full work days making this implementation work seemlessly. It was a huge challenge modifying the files and classes to cooperate between modules, but luckily we had similar experience from the individual assigment.

Originally, we tried to implement this manually in pairs, but quickly realized that this task was more complicated than we first anticipated. We decided on the following standup that the entire group needed to work on refactoring. In addition to using online resources like geek4geeks, we also took advantage of the integrated Github CoPilot to help implement certain classes as it could be confusing when refactoring the code structure.


# Configuring tests.

Working with Test classes was also a challenge that cost us a lot of time. We had to install a few different dependencies and make them work with each other. For instance we used TestFX as a testing framework and JaCoCo for measuring our test-coverage. After a lot of trial and error, deliberately configuring older versions of the frameworks to make them compatible and tweaking the dependency formats, we got it working. We originally landed on the decision on skipping javaFX tests on macOS as it caused a lot of issues on one of the peer's computer. However we managed to solve this issue with some configuration settings.

Another issue was that JaCoCo did not register UI coverage properly, which led us to run UI tests separately. At one point, the test suite took over 3.5 minutes to complete because of the large number of tests. We managed to optimize execution time significantly while maintaining good coverage by simplifying and restructuring the tests. All TestFX tests were grouped in a single file, while backend tests were organized in another. Since our prior experience with testing and TestFX was limited, we experienced a steep learning curve. 

We finally landed on 82% test coverage on average, in which core, persistence and rest have a totalt average above 90%. The ui tests covers just above 64%, and were complicated to create. We used AI as support tool when we got into complicated mockito testing, as this syllabus is beyond scope of work. Although UI didn't reach 80% coverage, the group is very much satisfied with a total average of 82%, with the testing of the most crucial code, logic and functionality is highly tested. 
