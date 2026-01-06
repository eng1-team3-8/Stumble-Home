package io.github.stumblehome.Entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class Chainsaw extends CollectableEntity {
    /**
     * calling constructor of superclass
     */
    public Chainsaw(Texture texture, float size, float[] position) {
        super(texture, size, position);
    }

    public boolean UseChainsaw(Player player, TiledMapTileLayer removable_walls) {
        if (isTriggered || !(isCollected)) {
            return false;
        }
        EntityDirection direction = player.getDirection();
        int[] target_cells = {0, 0};
        switch (direction) {
            case UP:
                target_cells[0] = (int) player.playerX;
                target_cells[1] = (int) player.playerY + 1;
                break;
            case DOWN:
                target_cells[0] = (int) player.playerX;
                target_cells[1] = (int) player.playerY;
                break;
            case RIGHT:
                target_cells[0] = (int) player.playerX + 1;
                target_cells[1] = (int) player.playerY;
                break;
            case LEFT:
                target_cells[0] = (int) player.playerX - 1;
                target_cells[1] = (int) player.playerY;
                break;
        }
        if (removable_walls.getCell(target_cells[0], target_cells[1]) == null) {
            return false;
        }
        Chainsaw.remove_wall_strip(removable_walls, target_cells, direction);
        // isTriggered = true;
        return true;
    }

    public static void remove_wall_strip(
            TiledMapTileLayer removable_walls, int[] start_pos, EntityDirection direction) {
        switch (direction) {
            case UP:
                while (removable_walls.getCell(start_pos[0], start_pos[1]) != null) {
                    removable_walls.setCell(start_pos[0], start_pos[1], null);
                    start_pos[1] += 1;
                    System.out.println(start_pos[1]);
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
