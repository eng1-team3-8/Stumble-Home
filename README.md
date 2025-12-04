# Stumble-Home

Repository for developing video game. Cohort 3, Team 8.

-----------------------------------------------------------------------
Note you must use **Adoptium's Temurin® Java 17**:

- This is the version the modules technical requirements state we must use, check **JAVASETUP.md** for setup
  instructions
- To test you are using the right version, run the file: **CheckJDKVersion.java**
    - Expect results like this:
        - Java Version: 17.0.16
        - Java Vendor: Eclipse Adoptium

-----------------------------------------------------------------------
Commit Standards:

- **Type: Message**
    - Type:
        - **feat** - changes that add a feature or modify one
        - **fix**  - changes that fix bugs or issues
        - **doc**  - changes to documentation
        - **conf**  - changes to file structure
        - **test** - changes to code tests
        - **dep** - changes to dependencies

    - Message: short explaining change
- Example:
    - *fix: data validation bug corrected*

-----------------------------------------------------------------------

Names:

- Henry G
- Lenny S
- Isaac M
- Andri K
- Rishi T
- Ida K
- Viktor B

-----------------------------------------------------------------------

## Build & Run

This repository provides a minimal runnable distribution: the `lib` module is built into a single runnable jar that includes the LWJGL3 backend and native desktop libraries.

```bash
# from repository root
cd StumbleHome
./gradlew clean :lib:jar

# then run the produced jar
java -jar lib/build/libs/StumbleHome-1.0.0.jar
```

Notes:
- The produced JAR is at `StumbleHome/lib/build/libs/StumbleHome-<version>.jar` where `<version>` is set in `StumbleHome/gradle.properties` (`projectVersion`).
