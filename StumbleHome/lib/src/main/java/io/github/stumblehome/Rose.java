package io.github.stumblehome;

import com.badlogic.gdx.graphics.Texture;

public class Rose extends CollectableEntity{

    public boolean isSquished = false;

    public Rose(Texture texture, float size, float[] position){
        super(texture, size, position);
    }
}
