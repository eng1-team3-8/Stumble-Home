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
    private boolean isColliding = false;

    // Boolean value to see if the entity has been collected
    public boolean isCollected = false;

    // Boolean value to see if the event caused by the collision has been triggered yet
    public boolean isTriggered = false;

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
     * A constructor for entities that don't have any animations
     *
     * @param texture Texture: The texture of the entity
     * @param size float: The size of the entity
     * @param position float[X,Y]: A float array of the coordinates of the entity. Position 0 is X,
     *     and 1 is Y.
     */
    public CollectableEntity(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    /**
     * This method checks if the player is within a certain distance of the centre of the entity. If
     * so, it updates the isColliding parameter. It works by using pythagoras' theorem to calculate
     * the straight line distance between the player and the entity
     *
     * @param playerX float: the x coordinate of the player
     * @param playerY float: the y coordinate of the player
     */
    public boolean checkColliding(float playerX, float playerY) {
        // Checks if the entity has already been collected
        if (this.isCollected) {
            return false;
        }

        // Calculates the distance between the player and the entity using pythagoras
        double distance =
                sqrt(pow(playerX - this.getCentreX(), 2) + pow(playerY - this.getCentreY(), 2));

        // Sets the colliding variable to true if the player is within a certain distance
        this.isColliding = distance < 1f;

        if (this.isColliding) {
            this.isCollected = true;
        }

        return this.isColliding;
    }

    /**
     * Draws the entity, if it hasn't been collected yet
     *
     * @param batch SpriteBatch: The spritebatch that will be drawn on
     */
    @Override
    public void draw(Batch batch) {
        if (!this.isCollected) {
            super.draw(batch);
        }
    }
}
