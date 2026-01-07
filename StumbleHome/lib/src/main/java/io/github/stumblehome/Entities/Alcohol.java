package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Class for alcoholic drinks. Inherits from collectable entity
 *
 * @author Lenny
 */
public class Alcohol extends CollectableEntity {
    /**
     * A constructor for entities that don't have any animations
     *
     * @param texture Texture: The texture of the entity
     * @param size float: The size of the entity
     * @param position float[X,Y]: A float array of the coordinates of the entity. Position 0 is X,
     *     and 1 is Y.
     */
    public Alcohol(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public void makeDrunk(Player player) {
        player.isDrunk = true;
        if (!player.canGetDrunk) {
            player.SwapControls();
        }
    }

    public void logic() {}
}
