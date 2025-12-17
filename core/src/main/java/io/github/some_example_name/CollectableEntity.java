package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * This is a class that creates entities that can be collected
 * @author Lenny, Isaac
 */
public abstract class CollectableEntity extends NonPlayerEntity{

    // Boolean value to see if the entity is colliding with the player
    boolean isColliding = false;

    // Boolean value to see if the entity has been collected
    boolean isCollected = false;

    public CollectableEntity(Texture texture, Sprite sprite, float size, int frame_width, int frame_height, int num_animations){
        super(sprite, size, frame_width, frame_height, num_animations);
    }

    /**
     * This method checks if the player is within a certain distance of the centre of the entity.
     * If so, it updates the isColliding parameter.
     * It works by using pythagoras' theorem to calculate the straight line distance between the player and the entity
     *
     * @param playerX
     * @param playerY
     */
    public void checkColliding(float playerX, float playerY){
        // Checks if the entity has already been collected
        if (this.isCollected){
            return;
        }

        // Calculates the distance between the player and the entity using pythagoras
        double distance = sqrt(pow(playerX - this.getCentreX(), 2) + pow(playerY - this.getCentreY(), 2));

        // Sets the colliding variable to true if the player is within a certain distance
        this.isColliding = distance < 1f;
    }


    @Override
    public void logic() {

    }

}
