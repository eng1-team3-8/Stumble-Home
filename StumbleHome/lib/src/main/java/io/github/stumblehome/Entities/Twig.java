package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * The class for the twig event
 *
 * @author Lenny
 */
public class Twig extends CollectableEntity {

    /**
     * A constructor for entities that don't have any animations
     *
     * @param texture Texture: The texture of the entity
     * @param size float: The size of the entity
     * @param position float[X,Y]: A float array of the coordinates of the entity. Position 0 is X,
     *     and 1 is Y.
     */
    public Twig(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public void logic() {}
}
