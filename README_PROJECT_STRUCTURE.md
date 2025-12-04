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

How to build (minimal):

```bash
# from repository root
cd StumbleHome
./gradlew clean build
```

The produced library JAR will be at `StumbleHome/lib/build/libs/StumbleHome-<version>.jar`.

Notes (Option B - library-only):
- This repository intentionally provides only the library module (no platform). To run the game create an external small Gradle project that depends on the produced JAR and adds the platform backend (LWJGL3). See `README.md` for an example `build.gradle` and `Launcher` class.
