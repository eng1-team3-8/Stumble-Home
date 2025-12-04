This repository is organized as a super-minimal project. The game library and Gradle wrapper live under the `StumbleHome/` folder.

Structure now:

- .gitignore, LICENCE, README.md (root)
- StumbleHome/
  - settings.gradle
  - gradlew, gradlew.bat
  - gradle/
  - lib/
    - build.gradle
    - src/main/java/

Shared runtime assets are located inside the module at `StumbleHome/lib/src/main/assets/` and are packaged by the `lib` module.

How to build:

1. Change into the `StumbleHome` folder and use the included wrapper:

   ```bash
   cd StumbleHome
   ./gradlew clean build
   ```

2. Build only the library (faster) — run this from `StumbleHome/`:

   ```bash
   cd StumbleHome
   ./gradlew :lib:build
   ```

If you need to re-add platform launchers (desktop/mobile), add platform modules and update `StumbleHome/settings.gradle`.
