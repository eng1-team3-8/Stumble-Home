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

Tests use JUnit 5 and run automatically with `build`, or separately:

```bash
cd StumbleHome
./gradlew test                                                     # Run all tests
./gradlew test --info                                              # Detailed output
./gradlew test --tests "io.github.stumblehome.headless.PlayerTest" # Specific test
./gradlew test jacocoTestReport                                    # With coverage
```

**Reports:**
- Tests: `StumbleHome/lib/build/reports/tests/test/index.html`
- Coverage: `StumbleHome/lib/build/reports/jacoco/test/html/index.html`

### 3 Formatting:

Spotless handles all code formatting. If the build fails due to formatting:

```bash
cd StumbleHome
./gradlew spotlessApply   # Auto-format
./gradlew spotlessCheck   # Informative only
```

### 4 Creating a Release:

Create and push a version tag to trigger an automated release (versioned JAR + macOS app bundle).

Only create releases for **significant** milestones (major features, stable versions), not every commit. Commits to `main`/`development` trigger CI/CD for testing but don't create releases.

**Release types:**
- **`main` branch tags**: Stable production releases (e.g., `StumbleHome-2.0.0.jar`)
- **`development` branch tags**: Pre-releases with `-dev` suffix (e.g., `StumbleHome-2.0.0-dev.jar`)

#### 4.1 Create and push a version tag

Version tags follow semantic versioning (v1.0.0, v2.0.0, v2.1.3): https://semver.org

```bash
# Production release (from main branch)
git tag -a v2.0.0 -m "Release version 2.0.0"
git push origin v2.0.0

# Development pre-release (from development branch)
git checkout development
git tag v2.1.0
git push origin v2.1.0  # Automatically becomes StumbleHome-2.1.0-dev.jar
```

#### 4.2 What happens after pushing a tag

Once the tag is pushed:
1. The GitHub Actions workflow builds the universal JAR with the version from the tag (e.g., `StumbleHome-2.0.0.jar`)
2. Tests run on all platforms (Ubuntu, Windows, macOS) to ensure compatibility
3. The macOS native app bundle is built
4. A GitHub release is automatically created with:
   - `StumbleHome-{version}.jar` (universal JAR for all platforms)
   - `StumbleHome-{version}-macos-app.zip` (native macOS app)

#### 4.3 Deleting a tag (only before release succeeds)

Only delete a tag **before** the release is published or if the workflow fails (wrong version number, build errors, test failures).

Once a release is published, **do NOT** delete the tag. Instead, create a new version (e.g., v2.0.1 to fix v2.0.0).

```bash
git tag -d v2.0.0                    # Delete local tag
git push origin --delete v2.0.0      # Delete remote tag (only if release hasn't completed)
```

### 5 Saving CI/CD Compute:

Each push to `main` or `development` triggers CI/CD (build + tests on 3 platforms). To minimise resource usage:

#### 5.1 Batch your commits

Make multiple commits locally and push once to trigger CI/CD only once (each push = full pipeline on 3 platforms).

```bash
git commit -m "Add feature X part 1"
git commit -m "Add feature X part 2"
git commit -m "Add feature X tests"
git push origin development  # Triggers CI/CD once instead of 3 times
```

#### 5.2 Test locally before pushing

Catch issues locally to avoid failed CI/CD runs:

```bash
cd StumbleHome
./gradlew spotlessCheck build test  # Run all checks locally first
```

#### 5.3 Use feature branches for work-in-progress

Feature branches don't trigger CI/CD until you create a PR:

```bash
git checkout -b feature/new-feature
git push origin feature/new-feature   # No CI/CD triggered
# Create PR when ready → CI/CD runs once
```

### 6 Best practices:

- Always use the Gradle wrapper (`./gradlew`).
- Run `./gradlew spotlessCheck` before pushing.
- Test your changes locally before creating a release tag.
- Use semantic versioning for tags (MAJOR.MINOR.PATCH).
- Create stable production release tags from the `main` branch.
- Create development pre-release tags from the `development` branch.
- Batch commits together to reduce CI/CD runs.
- Use feature branches for work-in-progress to avoid triggering unnecessary builds.
