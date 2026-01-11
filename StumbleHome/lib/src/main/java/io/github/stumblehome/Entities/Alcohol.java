package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;

/**
 * Class for alcoholic drinks. Inherits from collectable entity
 * When collected makes player drunk
 *
 * @author Lenny
 */
public class Alcohol extends CollectableEntity {
    public static final String ASSET_TSING = "Sprites/Tsingtao.png";
    public static final String ASSET_SMIRN = "Sprites/Smirnoff.png";

    /**
     * calls super constructor
     */
    public Alcohol(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public void logic() {}
}
