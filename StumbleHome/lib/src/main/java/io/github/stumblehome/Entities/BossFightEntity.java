package io.github.stumblehome.Entities;

import static java.lang.Math.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * This is an entity class for sprites that are in the boss fight
 *
 * @author Lenny
 */
public class BossFightEntity extends NonPlayerEntity {

    /**
     * The base constructor of the entity
     *
     * @param texture  Texture: The texture of the entity
     * @param size     float: The size of the entity
     * @param position float[X,Y]: An array of the coordinates of the entity. Array position 0 is X,
     *                 position 1 is Y.
     */
    public BossFightEntity(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    @Override
    public boolean checkColliding(float playerCentreX, float playerCentreY) {
        return false;
    }

    /**
     * This method checks if the x-axis of the two object overlap
     */
    @Override
    public void logic() {}

    public boolean checkOverlap(float playerCentreX, FitViewport viewport) {

        double distance = abs(playerCentreX - this.getX());
        return distance < (viewport.getWorldWidth() / 80) * 3;
    }

    /**
     * Setter for the position
     */
    public void updatePosition() {
        this.setPosition(this.getX(), this.getY());
    }
}
