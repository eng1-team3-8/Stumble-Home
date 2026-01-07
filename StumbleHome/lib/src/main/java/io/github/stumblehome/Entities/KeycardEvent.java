package io.github.stumblehome.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a collectible keycard item required to unlock the exit. The keycard displays an
 * animated sprite until collected by the player.
 */
public class KeycardEvent extends CollectableEntity {
    public static final String ASSET = "keyCard.png";

    /**
     * calling constructor of superclass
     */
    public KeycardEvent(Texture texture, float size, float[] position) {
        super(texture, size, position, 32, 32, 3);
    }

    /**
     * Updates the keycard's animation state each frame. Only processes animation while the keycard
     * hasn't been collected.
     */
    public void logic() {
        if (!isCollected) {
            if (current_animation != previous_animation) {
                state_time = 0f;
                previous_animation = current_animation;
            }
            state_time += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Renders the keycard to the screen if it hasn't been collected yet.
     *
     * @param batch the sprite batch used for rendering
     */
    @Override
    public void draw(Batch batch) {
        if (!isCollected) {
            TextureRegion frameToDraw = current_animation.getKeyFrame(state_time, true);
            batch.draw(frameToDraw, getX(), getY(), frame_size, frame_size);
        }
    }
}
