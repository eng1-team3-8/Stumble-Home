package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Class for food objects Inherits from collectableEntity.
 *
 * @author Lenny
 */
public class Food extends CollectableEntity {
    public static final String ASSET = "Sprites/Chicken.png";


    /**
     * A constructor for entities that don't have any animations
     *
     * @param texture Texture: The texture of the entity
     * @param size float: The size of the entity
     * @param position float[X,Y]: A float array of the coordinates of the entity. Position 0 is X,
     *     and 1 is Y.
     */
    public Food(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public void eatFood(Player player) {
        player.canGetDrunk = false;
        if (player.isDrunk) {
            player.SwapControls();
        }
        player.isDrunk = false;
    }

    public void logic() {}
}
