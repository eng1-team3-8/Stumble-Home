package io.github.stumblehome.Screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.backends.lwjgl3.audio.Mp3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import io.github.stumblehome.StumbleHome;
import io.github.stumblehome.Entities.BossFightEntity;

public class BossScreen implements Screen {

    // The game instance
    private final StumbleHome game;

    // Players name
    private final String playerName;

    // Textures of the boss-fight object
    // Mike
    private Texture Mike;
    // Menu Background
    private Texture MenuBackground;
    // Buttons
    private Texture Button;
    // Switch
    private Texture Switch;
    // Cables
    private Texture BrokenCable;
    // Packets
    private Texture PacketUDP;

    // Music
    private Mp3.Music BossMusic;

    // Sprite batch
    private SpriteBatch batch;

    // Entities
    private BossFightEntity Scissors;
    private BossFightEntity Cable;

    public BossScreen(final StumbleHome game, final String playerName) {
        this.game = game;
        this.playerName = playerName;

        this.batch = new SpriteBatch();

        // Create the BossFightEntities
        this.Scissors =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Scissors.png"), 1f, new float[] {2, 2});

        this.Cable =
                new BossFightEntity(
                        new Texture("Sprites/BossFight/Cable.png"), 1f, new float[] {2, 3});
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        // Make the Screen Black -> paint it Black!
        ScreenUtils.clear(Color.BLACK);
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
