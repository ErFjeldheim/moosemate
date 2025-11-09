# AI Tools

We've continued using GitHub Copilot in Visual Studio Code throughout this release. It proved especially helpful when implementing the REST API, where we had to refactor many tests to work with the new structure. For instance, LoginServiceTest had numerous boolean values that needed changing to User objects. AI helped us handle these repetitive modifications efficiently.

We also used Copilot as a design partner for JavaFX, helping us create a more adaptable and user-friendly interface since JavaFX styling can be tricky to get right. Beyond design, AI has been valuable for quickly generating boilerplate code. Simple classes with constructors, getters, and setters are easy to create and rarely produce bugs. AI is also powerful for generating code in unfamiliar syntax like FXML and PUML, laying a solid foundation that's much easier to tweak than writing from scratch.

However, we learned some important lessons along the way. Early in the project, we sometimes blindly applied AI-generated code without fully understanding it. For instance, when generating test files, AI created far too many tests, and we lost track of what each one was doing. We had to review the tests and cut them down substantially and be more selective. Going forward we used AI more sparingly, focusing on quality code rather than quantity.

Overall, AI has been most useful for quickly generating a basic code foundation that we can then refine and adapt to our specific needs.