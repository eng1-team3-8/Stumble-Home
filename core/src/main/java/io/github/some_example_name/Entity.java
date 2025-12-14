package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


/**
 * This is an abstract class for creating entities.
 * It inherits from the Sprite class, and calls the superclass in order to create a sprite
 * @author Lenny, Isaac
 */
public abstract class Entity extends Sprite{

    // Currently active animation being played.
    protected Animation<TextureRegion> current_animation;

    // Previous animation, used to reset timing when animation changes.
    protected Animation<TextureRegion> previous_animation;

    // Time elapsed in the current animation.
    protected float state_time;

    // The current X position of the entity
    public float x_pos;

    // The current Y position of the entity
    public float y_pos;

    // Size of the entity frames when rendered
    public final float frame_size;

    public Entity(Sprite sprite, float size){
        super(sprite);
        frame_size = size;
    }

    /**
     * Updates the animation state timer each frame.
     * Resets the timer when switching between different animations.
     */
    public abstract void logic();
    
    /**
     * Renders the player character using the appropriate animation frame or standing pose.
     *
     * @param batch the sprite batch to draw with
     */
    public abstract void draw(SpriteBatch batch);
}
