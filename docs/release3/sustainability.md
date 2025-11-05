# Artificial Intelligence (AI)

Choosing not to use artificial intelligence (AI) in the MooseMate app is a sustainable decision, both environmentally and socially. AI technologies require significant computational power, which in turn demands large amounts of energy.  
According to a report from the University of Massachusetts Amherst, training a single large language model can emit up to 284 tons of CO₂, roughly equivalent to the lifetime emissions of five cars.

By developing MooseMate without AI, we reduce the need for powerful servers and continuous data processing, resulting in a smaller carbon footprint. This approach encourages a mindful use of digital infrastructure, focusing on lightweight and efficient solutions rather than high-energy automation.

The absence of AI also enables a simpler and more resource-efficient app that requires less data storage and bandwidth. MooseMate runs smoothly on older or more affordable devices, extending the lifespan of existing technology and supporting the principles of a circular economy.  
By prioritizing accessibility and inclusivity, MooseMate contributes to reduced electronic waste and longer hardware life. This design philosophy emphasizes software sustainability — writing code for long-term efficiency and environmental responsibility rather than just short-term performance.

Avoiding heavy AI integrations also simplifies data privacy compliance. Since AI models often depend on large-scale data collection and storage, excluding them lowers the risk of data misuse and reduces operational complexity. As a result, MooseMate can focus on transparent and privacy-friendly solutions that respect both users and the environment.

---

# Low Energy Consumption

No database currently runs continuously (24/7). This reduces server costs, energy consumption, and the overall carbon footprint. Instead of maintaining always-on infrastructure, MooseMate uses an on-demand model where computing resources are active only when needed.

This design minimizes idle computing power and eliminates unnecessary background processes. By consuming energy only when users engage with the app, MooseMate avoids the “always-on” inefficiency common in modern cloud applications.

Lower energy use also delivers practical benefits. Reduced server utilization translates directly to lower operational costs and improved scalability. As the app grows, this foundation allows the platform to expand responsibly while maintaining performance and minimizing environmental impact.

---

# Multi-Modularity

A multi-modular architecture using separate modules for UI, Core, Persistence, and REST provides a sustainable and efficient structure. It supports cleaner code organization, better maintainability, and enhanced scalability.

- Running individual modules consumes less energy than executing the entire system each time.  
- Teams can work on separate modules without affecting the whole project, reducing development time and resource waste.  
- Limited technical depth keeps refactoring efforts contained within each module, improving maintainability and reducing costs.

This modular approach also improves adaptability. When technologies evolve, individual modules can be upgraded or replaced without a full rewrite, reducing redundancy and extending the system’s lifecycle. Each module can be tested and deployed independently, improving quality control and minimizing cascading errors. Over time, this approach reduces workload, rework cycles, and the app’s overall computational footprint.

---

# Testing

Extensive test coverage prevents costly hotfixes and emergency deployments, saving developer time and reducing rollbacks and redeployments. Automated testing ensures that potential issues are caught early, before reaching production environments. By identifying bugs sooner, MooseMate minimizes unnecessary builds, redeployments, and server activity.

Testing supports long-term software health by enabling continuous integration and deployment pipelines that run efficiently. Stable, predictable software requires fewer interventions and consumes fewer computing resources.  
In this way, testing becomes a key aspect of sustainability — conserving both human effort and digital energy, and ensuring that MooseMate remains reliable, maintainable, and environmentally responsible over time.
