package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * This is an abstract class for creating entities.
 * It inherits from the Sprite class, and calls the superclass in order to create a sprite
 * @author Lenny, Isaac
 */
public abstract class Entity extends Sprite{

    // Sprite batch where the entity should be drawn
    private SpriteBatch batch;

    public Entity (Texture texture){
        super(texture);
    }

}
