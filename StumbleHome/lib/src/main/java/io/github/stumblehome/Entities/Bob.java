package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * class for bob item
 * when collided with triggers the boss fight
 */
public class Bob extends CollectableEntity {
    public static final String ASSET = "Sprites/B-bThing.png";

    /**
     * calls super constructor
     */
    public Bob(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public void logic() {}
}
