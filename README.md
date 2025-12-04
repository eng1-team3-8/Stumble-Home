# Stumble-Home

A [libGDX](https://libgdx.com/) project generated with [gdx-liftoff](https://github.com/libgdx/gdx-liftoff).

This project was generated with a template including simple application launchers and an empty `ApplicationListener` implementation.

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


## Platforms

- `core`: Main module with the application logic shared by all platforms.

## Gradle

This project now contains a minimal Gradle setup inside the `StumbleHome/` folder.
Use the wrapper located under `StumbleHome/` for building and maintenance.

Change into the `StumbleHome/` folder and run commands there, for example:

```bash
cd StumbleHome
./gradlew clean build
```

Common useful commands (run inside `StumbleHome/`):

- Full clean + build (recommended before releasing or running CI):

    ```bash
    ./gradlew clean build
    ```

- Build only the library module (faster):

    ```bash
    ./gradlew :lib:build
    ```

- Produce the library JAR (output in `lib/build/libs`):

    ```bash
    ./gradlew :lib:jar
    ```

- Regenerate the assets list (writes `../assets/assets.txt` from module):

    ```bash
    ./gradlew :lib:generateAssetList
    ```

- Clean build artifacts:

    ```bash
    ./gradlew clean
    ```

- Run unit tests (if any):

    ```bash
    ./gradlew test
    ```

- Show runtime dependencies for the library (handy for debugging):

    ```bash
    ./gradlew :lib:dependencies --configuration runtimeClasspath
    ```

- Useful flags for debugging builds:

    ```bash
    ./gradlew <task> --stacktrace --info
    ./gradlew <task> --scan
    ```

Notes on running the game

- The repository no longer contains a platform/executable subproject (for example `lwjgl3`). The `StumbleHome` module builds the game library (JAR). To run the game locally you must either:
   - Re-add or restore a platform module (e.g., a desktop `lwjgl3` module) and use its `:run` task; or
   - Create a small launcher project that depends on `StumbleHome/lib` and provides a backend (LWJGL3, Android, etc.).

- If you still have an executable JAR / platform, the runnable JAR will typically be in that platform's `build/libs` folder.

CI / packaging

- The repository's GitHub Actions workflow is configured to build the project and upload the library JAR from `StumbleHome/lib/build/libs`.

Tips

- If you see unexpected compile errors after renames, try a clean build (`./gradlew clean build`) and ensure your IDE project files are refreshed (e.g., `./gradlew idea` or reimport the Gradle project in the IDE).
- Use `./gradlew :StumbleHome:lib:generateAssetList` before building if you changed files in `assets/` so the list stays up to date.

If you'd like, I can add a small `lwjgl3` launcher module as a lightweight example project that depends on `StumbleHome` and provides a runnable desktop launcher. That would let you run `./gradlew :lwjgl3:run` again.
