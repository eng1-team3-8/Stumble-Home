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
     * Calling constructor of superclass
     */
    public Food(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    /**
     * sets the player to being unable to get drunk
     * @param player that the food is affecting
     */
    public void eatFood(Player player) {
        player.canGetDrunk = false;
        player.setSober();
    }

    public void logic() {}
}
