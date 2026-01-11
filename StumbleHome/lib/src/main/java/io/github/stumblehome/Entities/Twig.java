package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * The class for the twig event
 *
 * @author Lenny
 */
public class Twig extends CollectableEntity {
    public static final String ASSET = "Sprites/Stick.png";

    /**
     * calls the super constructor
     */
    public Twig(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public void logic() {}
}
