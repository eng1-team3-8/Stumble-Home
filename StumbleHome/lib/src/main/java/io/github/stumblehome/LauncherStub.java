package io.github.stumblehome;

/**
 * Small launcher stub that provides a clear message when running the library JAR directly.
 *
 * The library module doesn't include a platform backend (for example LWJGL3),
 * so this stub intentionally explains how to run the game via a platform launcher.
 */
public final class LauncherStub {
    public static void main(String[] args) {
        System.out.println("This JAR is the StumbleHome game library.");
        System.out.println("It is not a runnable desktop application by itself.");
        System.out.println("Create a platform launcher (e.g., an LWJGL3 module) that depends on this library and runs the game.");
        System.out.println("Alternatively, add a launcher with a Main-Class that creates a backend and starts the game.");
    }
}
