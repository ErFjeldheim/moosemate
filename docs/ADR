# ADR: Java 21 LTS vs Java 23

- **Status:** Accepted
- **Date:** 2025-09-10

## Context
We must choose a JDK version for development, CI, and Eclipse Che. Course requirements emphasize reproducibility and smooth Maven/JavaFX builds.

## Options
1) **Java 21 (LTS)**  
   - Long-term support, broad tooling compatibility (IDEs, Maven plugins, CI images).  
   - Stable features which results in less possible conflicts

2) **Java 23 (non-LTS)**  
   - Newer features which possible saves time during developing.  
   - Short support window; possible plugin/tooling lag.

## Decision
We standardize on **Java 21 LTS** for development and CI.

## Consequences
- Higher stability and easier reproduction.  
- Fewer CI/IDE hiccups.  
- Missing out on more advanced updates. However this is a bigger consequence for more advanced developers, and will impact this group to a smaller extent.


## Rollback Plan
If we hit an issue with Java 21, we will create a new ADR to switch to Java 23 (or Java 25 LTS when available), update Maven toolchains, CI image, and README.

## Implementation Notes 
- Pin CI and Che images to Java 21. 

