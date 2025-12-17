package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Twig extends CollectableEntity {

    public Twig(Texture texture, Sprite sprite, float size, int frame_width, int frame_height, int num_animations){
        super(texture, sprite, size, frame_width, frame_height, num_animations);
    }



}
