# CONTRIBUTING GUIDE

## Quick summary

- Use the Gradle wrapper in the `StumbleHome` subdirectory to build the project.
- If the build fails because of formatting, run Spotless to apply the project style automatically.

---

### 1 Build the project:

From the repository root:

```bash
cd StumbleHome
./gradlew build
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

### 2 Running tests:

The project uses JUnit 5 for testing. Tests are automatically run as part of the `build` task, but you can also run them separately.

#### 2.1 Run all tests

```bash
cd StumbleHome
./gradlew test
```

#### 2.2 Run tests with detailed output

```bash
cd StumbleHome
./gradlew test --info
```

#### 2.3 View test results

After running tests, you can view the HTML test report at:
```
StumbleHome/lib/build/reports/tests/test/index.html
```

#### 2.4 Run specific test classes

```bash
cd StumbleHome
./gradlew test --tests "io.github.stumblehome.headless.PlayerTest"
```

#### 2.5 Generate code coverage reports

Code coverage is tracked using JaCoCo. Coverage reports are automatically generated when you run tests.

```bash
cd StumbleHome
./gradlew test jacocoTestReport
```

Coverage reports are available at:
```
StumbleHome/lib/build/reports/jacoco/test/html/index.html`
```

### 3 If the build fails because of formatting:

Spotless is configured as the single source of formatting/linting.
If the build fails with formatting errors, apply the formatting locally and re-run the build.

- To auto-format:

```bash
cd StumbleHome
./gradlew spotlessApply
```

- To check formatting without changing files:

```bash
cd StumbleHome
./gradlew spotlessCheck
```

After running `spotlessApply`, re-run the build command.

### 3 Best practices:

- Always use the Gradle wrapper (`./gradlew`).
- Run `./gradlew spotlessCheck` before pushing.
