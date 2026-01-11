package io.github.stumblehome.BossFight;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.stumblehome.Entities.BossFightEntity;

public class Scissors extends BossFightEntity {
    public static final String ASSET = "Sprites/BossFight/Scissors.png";
    private boolean moveRight = true;

    /**
     * The base constructor of the entity
     *
     * @param texture  Texture: The texture of the entity
     * @param size     float: The size of the entity
     * @param position float[X,Y]: An array of the coordinates of the entity. Array position 0 is X,
     *                 position 1 is Y.
     */
    public Scissors(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public void move(Viewport viewport) {
        if (this.getX() > (viewport.getWorldWidth() / 800) * 633) {
            this.moveRight = false;
        } else if (this.getX() < (viewport.getWorldWidth() / 160) * 21) {
            this.moveRight = true;
        }

        if (moveRight) {
            this.setX(this.getX() + (viewport.getWorldWidth() / 800));
        } else {
            this.setX(this.getX() - (viewport.getWorldWidth() / 800));
        }

        this.updatePosition();
    }

    //    public void setX(float x) {
    //        this.s
    //        this.updatePosition();
    //    }
}
