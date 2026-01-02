package io.github.stumblehome.Screens;

import io.github.stumblehome.StumbleHome;

public abstract class GameFinishScreen extends MenuScreen {
    // The remaining time (in seconds) when the player finished the game.
    final float remainingTime;

    // The player's final score.
    final int score;

    public GameFinishScreen(StumbleHome game, float remainingTime, int score, String background) {
        super(game, background);
        this.remainingTime = remainingTime;
        this.score = score;
    }
}
