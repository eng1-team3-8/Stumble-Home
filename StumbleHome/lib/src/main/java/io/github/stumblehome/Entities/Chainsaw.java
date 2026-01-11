package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

/**
 * Collectable event item for chainsaw
 * When collected allows the player to cut a row of hedges a single time
 *
 * @author Isaac
 */
public class Chainsaw extends CollectableEntity {
    public static final String ASSET = "Sprites/Chainsaw.png";

    /**
     * calling constructor of superclass
     */
    public Chainsaw(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    /**
     * detects the direction that player is facing and cuts a row of hegdes in that direction
     * until a non hedge is reached. sets is_triggered to true
     * @param player player using the chainsaw
     * @param removable_walls wall layer that should be considered when deciding to remove,
     *      will remove any not null cell
     * @return true if successful and false if not
     */
    public boolean UseChainsaw(Player player, TiledMapTileLayer removable_walls) {
        if (is_triggered || !(is_collected)) {
            return false;
        }
        EntityDirection direction = player.getDirection();
        int[] target_cells = {0, 0};
        switch (direction) {
            case UP:
                target_cells[0] = (int) player.getX();
                target_cells[1] = (int) player.getY() + 1;
                break;
            case DOWN:
                target_cells[0] = (int) player.getX();
                target_cells[1] = (int) player.getY();
                break;
            case RIGHT:
                target_cells[0] = (int) player.getX() + 1;
                target_cells[1] = (int) player.getY();
                break;
            case LEFT:
                target_cells[0] = (int) player.getX() - 1;
                target_cells[1] = (int) player.getY();
                break;
        }
        if (removable_walls.getCell(target_cells[0], target_cells[1]) == null) {
            return false;
        }
        Chainsaw.remove_wall_strip(removable_walls, target_cells, direction);
        is_triggered = true;
        return true;
    }

    /**
     * static function to remove the wall strip in a given direction, starting at a given position
     * @param removable_walls the layer that should be considered and removed from.
     *      all non null cells will be removed
     * @param start_pos the inital position of the line of walls to be removed in format [x, y]
     * @param direction the direction that the line shoudl go in
     */
    public static void remove_wall_strip(
            TiledMapTileLayer removable_walls, int[] start_pos, EntityDirection direction) {
        switch (direction) {
            case UP:
                while (removable_walls.getCell(start_pos[0], start_pos[1]) != null) {
                    removable_walls.setCell(start_pos[0], start_pos[1], null);
                    start_pos[1] += 1;
                }
                break;
            case DOWN:
                while (removable_walls.getCell(start_pos[0], start_pos[1]) != null) {
                    removable_walls.setCell(start_pos[0], start_pos[1], null);
                    start_pos[1] -= 1;
                }
                break;
            case RIGHT:
                while (removable_walls.getCell(start_pos[0], start_pos[1]) != null) {
                    removable_walls.setCell(start_pos[0], start_pos[1], null);
                    start_pos[0] += 1;
                }
                break;
            case LEFT:
                while (removable_walls.getCell(start_pos[0], start_pos[1]) != null) {
                    removable_walls.setCell(start_pos[0], start_pos[1], null);
                    start_pos[0] -= 1;
                }
                break;
        }
    }

    public void logic() {}
}
