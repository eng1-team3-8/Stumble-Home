package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Chainsaw extends CollectableEntity {

    private boolean chainsaw_used;
    /**
     * calling constructor of superclass
     */
    public Chainsaw(Texture texture, float size, float[] position) {
        super(texture, size, position);
        chainsaw_used = false;
    }

    public boolean UseChainsaw(Player player, TiledMapTileLayer removable_walls) {
        if (chainsaw_used == false) {
            return false;
        }
        EntityDirection direction = player.getDirection();
        float[] target_cells = {0, 0};
        switch (direction) {
            case UP:
                target_cells[0] = x_pos;
                target_cells[1] = y_pos + 1;
                break;
            case DOWN:
                target_cells[0] = x_pos;
                target_cells[1] = y_pos - 1;
                break;
            case RIGHT:
                target_cells[0] = x_pos + 1;
                target_cells[1] = y_pos;
                break;
            case LEFT:
                target_cells[0] = x_pos - 1;
                target_cells[1] = y_pos;
                break;
        }
        if (removable_walls.getCell((int) target_cells[0], (int) target_cells[1]) == null) {
            return false;
        }
        removable_walls.setCell((int) target_cells[0], (int) target_cells[1], null);
        return true;
    }
}
