package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * An entity class for Non-Player Entities. It inherits from entity. Contains two constructor for
 * entities with and without animations
 *
 * @author Isaac, Lenny
 */
public abstract class NonPlayerEntity extends Entity {

    /**
     * A constructor for entities that have animations
     *
     * @param texture Texture: The texture of the entity
     * @param size float: The size of the entity
     * @param position float[X,Y]: A float array of the coordinates of the entity. Position 0 is X,
     *     and 1 is Y.
     * @param frame_width integer: The frame width of the animation
     * @param frame_height integer: The frame height of the animation
     * @param num_animations integer: The number of animations
     */
    public NonPlayerEntity(
            Texture texture,
            float size,
            float[] position,
            int frame_width,
            int frame_height,
            int num_animations) {
        super(texture, size, position);
        initialiseAnimations(frame_width, frame_height, num_animations);
    }

    /**
     * A constructor for entities that don't have any animations
     *
     * @param texture Texture: The texture of the entity
     * @param size float: The size of the entity
     * @param position float[X,Y]: A float array of the coordinates of the entity. Position 0 is X,
     *     and 1 is Y.
     */
    public NonPlayerEntity(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    private void initialiseAnimations(int frame_width, int frame_height, int num_animations) {
        TextureRegion[] texture_frames = new TextureRegion[num_animations];
        for (int frame = 0; frame < num_animations; ) {
            for (int side_frame = 0; side_frame < 2; side_frame++) {
                if (frame < num_animations) {
                    texture_frames[frame] =
                            new TextureRegion(
                                    getTexture(),
                                    frame_width * side_frame,
                                    frame_height * (int) (frame / 2),
                                    frame_width,
                                    frame_height);
                    frame++;
                }
            }
        }
        current_animation = new Animation<>(0.3f, texture_frames);
        state_time = 0f;
    }

    protected float getCentreX() {
        return x_pos + frame_size / 2;
    }

    protected float getCentreY() {
        return y_pos + frame_size / 2;
    }
}
