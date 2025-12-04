9This project has been reorganized so that `StumbleHome/lib` is the primary Java source module.

Structure now:
- gradlew, gradlew.bat, gradle/wrapper/* : Gradle wrapper in project root.
- settings.gradle : Includes `:StumbleHome:lib` as the included module for the game logic.
- build.gradle : Minimal root build file with shared properties.
- StumbleHome/lib/ : The Java library module containing `src/main/java` and its `build.gradle`.
- assets/ : Shared runtime assets used by the game; `generateAssetList` task will populate `assets/assets.txt`.

How to build:

- Run the wrapper from the project root:

  ./gradlew :StumbleHome:lib:build

Notes:
- Platform-specific launchers and modules (like `lwjgl3`) were removed from the Gradle multi-project configuration; the game library is built via the `StumbleHome` module.
  Re-add platform modules and update `settings.gradle` only when you need executable platforms.
