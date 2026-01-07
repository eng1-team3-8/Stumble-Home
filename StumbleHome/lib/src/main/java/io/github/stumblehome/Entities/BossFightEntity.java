package io.github.stumblehome.Entities;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import com.badlogic.gdx.graphics.Texture;

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
    public void logic() {}

    public boolean overlaps(BossFightEntity other) {

        double distance =
                sqrt(
                        pow(other.getCentreX() - this.getCentreX(), 2)
                                + pow(other.getCentreY() - this.getCentreY(), 2));

        return distance < 1f;
    }

    public void updatePosition() {
        this.setPosition(this.x_pos, this.y_pos);
    }
}
