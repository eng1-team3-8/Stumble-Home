package io.github.stumblehome.Entities;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * This is a class that creates entities that can be collected
 *
 * @author Lenny, Isaac
 */
public abstract class CollectableEntity extends NonPlayerEntity {

    // Boolean value to see if the entity is colliding with the player
    private boolean is_colliding = false;

    // Boolean value to see if the entity has been collected
    public boolean is_collected = false;

    // Boolean value to see if the event caused by the collision has been triggered yet
    public boolean is_triggered = false;

    /**
     * calls super constructor
     */
    public CollectableEntity(
            Texture texture,
            float size,
            float[] position,
            int frame_width,
            int frame_height,
            int num_animations) {
        super(texture, size, position, frame_width, frame_height, num_animations);
    }

    /**
     * calls super constructor
     */
    public CollectableEntity(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    /**
     * This method checks if the player is within a certain distance of the centre of the entity. If
     * so, it updates the is_colliding parameter. It works by using pythagoras' theorem to calculate
     * the straight line distance between the player and the entity
     *
     * @param playerX float: the x coordinate of the player
     * @param playerY float: the y coordinate of the player
     */
    public boolean checkColliding(float playerX, float playerY) {
        if (this.is_collected) {
            return false;
        }
        // a^2 + b^2 = c^2
        double distance =
                sqrt(pow(playerX - this.getCentreX(), 2) + pow(playerY - this.getCentreY(), 2));
        this.is_colliding = distance < 1f;
        if (this.is_colliding) {
            this.is_collected = true;
        }

        return this.is_colliding;
    }

    /**
     * Draws the entity, if it hasn't been collected yet
     *
     * @param batch SpriteBatch: The spritebatch that will be drawn on
     */
    @Override
    public void draw(Batch batch) {
        if (!this.is_collected) {
            super.draw(batch);
        }
    }
}
