package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;

public class Rose extends CollectableEntity {
    public static final String ASSET_YORK = "Sprites/YorkRose.png";
    public static final String ASSET_LANC = "Sprites/LancasterRose.png";

    public boolean isSquished = false;

    public Rose(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public void logic() {}
}
