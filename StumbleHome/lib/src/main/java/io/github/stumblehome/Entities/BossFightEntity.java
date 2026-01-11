package io.github.stumblehome.Entities;

import static java.lang.Math.*;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.FitViewport;

/**
 * This is an entity class for sprites that are in the boss fight
 * used in boss fight
 *
 * @author Lenny
 */
public class BossFightEntity extends NonPlayerEntity {
    public static final String ASSET_CABLES = "Sprites/BossFight/Cable.png";

    /**
     * calls super constructor
     */
    public BossFightEntity(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    /**
     * Object will never collide with player
     */
    @Override
    public boolean checkColliding(float playerCentreX, float playerCentreY) {
        return false;
    }

    @Override
    public void logic() {}

    /**
     * This method checks if the x-axis of the two object overlap
     *
     * @param playerCentreX the x axis of the player to check against
     * @param viewport the viewport that is being used
     * @return true if the objects overlap, false if they don't
     */
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
