package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * This is an abstract class for creating entities. It inherits from the Sprite class, and calls the
 * superclass in order to create a sprite
 *
 * @author Lenny, Isaac
 */
public abstract class Entity extends Sprite {

    // Size of the entity frames when rendered
    public final float frame_size;
    // The sprite batch
    public SpriteBatch batch;
    // Currently active animation being played.
    protected Animation<TextureRegion> current_animation;
    // Previous animation, used to reset timing when animation changes.
    protected Animation<TextureRegion> previous_animation;
    // Time elapsed in the current animation.
    protected float state_time;
    // The current X position of the entity
    protected float x_pos;
    // The current Y position of the entity
    protected float y_pos;

    /**
     * The base constructor of the entity
     *
     * @param texture Texture: The texture of the entity
     * @param size float: The size of the entity
     * @param position float[X,Y]: An array of the coordinates of the entity. Array position 0 is X,
     *     position 1 is Y.
     */
    public Entity(Texture texture, float size, float[] position) {
        super(texture); // Creates the sprite with the texture
        this.frame_size = size;
        this.setSize(size, size);
        this.setPosition(
                position[0], position[1]); // Sets the position of the sprite using the array
        this.x_pos = position[0];
        this.y_pos = position[1];
    }

    /**
     * Updates the animation state timer each frame. Resets the timer when switching between different
     * animations.
     */
    public abstract void logic();

    /** A dispose function, to clean up the code, when entities are destroyed */
    public void dispose() {
        this.getTexture().dispose();
    }
}
