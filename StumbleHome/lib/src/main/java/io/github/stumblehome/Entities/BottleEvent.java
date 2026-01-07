package io.github.stumblehome.Entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Represents a collectible bottle item that appears in the game world. When collected by the
 * player, this affects their movement controls.
 */
public class BottleEvent extends CollectableEntity {

    /**
     * calling constructor of superclass
     */
    public BottleEvent(Texture texture, float size, float[] position) {
        super(texture, size, position, 32, 32, 6);
    }

    /**
     * Updates the bottle's animation state each frame. Only processes animation if the bottle hasn't
     * been collected yet.
     */
    public void logic() {
        if (!isCollected) {
            // Reset animation time if animation changed
            if (current_animation != previous_animation) {
                state_time = 0f;
                previous_animation = current_animation;
            }

            // Update animation time
            state_time += Gdx.graphics.getDeltaTime();
        }
    }

    /**
     * Renders the bottle to the screen if it hasn't been collected.
     *
     * @param batch the sprite batch used for rendering
     */
    @Override
    public void drawEntity(SpriteBatch batch) {
        if (!isCollected) {
            // Determine which frame to draw
            TextureRegion frameToDraw = current_animation.getKeyFrame(state_time, true);
            // Draw the character with proper aspect ratio
            batch.draw(frameToDraw, x_pos, y_pos, frame_size, frame_size);
        }
    }
}
