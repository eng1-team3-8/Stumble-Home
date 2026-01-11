package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * class for bird seed collectable item
 * when collected increases longbois speed
 *
 * @author Henry
 */
public class BirdSeed extends CollectableEntity {
    public static final String ASSET = "Sprites/BirdSeed.png";

    /**
     * calls super constructor
     */
    public BirdSeed(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    @Override
    public void logic() {}
}
