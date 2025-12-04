9This project has been reorganized so that `triangle/lib` is the primary Java source module.

Structure now:
- gradlew, gradlew.bat, gradle/wrapper/* : Gradle wrapper in project root.
- settings.gradle : Includes `:triangle:lib` as the single included module.
- build.gradle : Minimal root build file with shared properties.
- triangle/lib/ : The Java library module containing `src/main/java` and its `build.gradle`.
- assets/ : Shared runtime assets used by the game; `generateAssetList` task will populate `assets/assets.txt`.

How to build:

- Run the wrapper from the project root:

  ./gradlew :triangle:lib:build

Notes:
- The previous multi-module configs (core, lwjgl3) were removed to simplify the project per request.
- If you want to re-add platforms (lwjgl3, core), reintegrate their build files and update `settings.gradle` accordingly.

