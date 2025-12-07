# CONTRIBUTION GUIDE

## Quick summary

- Use the Gradle wrapper in the `StumbleHome` subdirectory to build the project.
- If the build fails because of formatting, run Spotless to apply the project style automatically.

---

### 1 Build the project:

From the repository root:

```bash
cd StumbleHome
./gradlew --no-daemon --stacktrace build
```

This runs the normal Gradle lifecycle: compilation, tests, checks and packaging.

#### 1.1 Running without tests

If you want a fresh packaged JAR without running tests or checks, use the clean+jar flow. This rebuilds artifacts from
scratch and produces the runnable JAR under `lib/build/libs/`.

```bash
cd StumbleHome
./gradlew clean jar

# then run the produced jar
java -jar lib/build/libs/StumbleHome-1.0.0.jar
```

### 2 If the build fails because of formatting:

Spotless is configured as the single source of formatting/linting.
If the build fails with formatting errors, apply the formatting locally and re-run the build.

- To auto-format (apply fixes):

```bash
cd StumbleHome
./gradlew spotlessApply
```

- To check formatting without changing files (CI-style):

```bash
cd StumbleHome
./gradlew spotlessCheck
```

After running `spotlessApply`, re-run the build command above.

### 3 Best practices:

- Always use the Gradle wrapper (`./gradlew`) — it ensures a consistent Gradle version across contributors and CI.
- Use Java 11+ when running Gradle locally (CI uses Java 17) to avoid incompatibilities with some plugin versions.
- Run `./gradlew spotlessCheck` before pushing.
