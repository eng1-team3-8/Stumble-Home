package io.github.stumblehome;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

/**
 * Minimal launcher that starts the game using LWJGL3 backend so the produced fat JAR is runnable
 * with `java -jar StumbleHome-<version>.jar`.
 */
public final class LauncherStub {
    public static void main(String[] args) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("StumbleHome");
        config.setWindowedMode(800, 500);
        // If StumbleHome class is in the library, instantiate and run
        new Lwjgl3Application(new StumbleHome(), config);
    }
}
