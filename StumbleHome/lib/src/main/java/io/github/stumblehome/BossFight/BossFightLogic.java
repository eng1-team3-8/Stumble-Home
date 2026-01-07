package io.github.stumblehome.BossFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.stumblehome.Entities.BossFightEntity;

public class BossFightLogic {

    private boolean isStopped = false;
    private boolean[] cableStatus = {true, true, true, true};
    private final Texture cutCable;

    public BossFightLogic(Texture cutCable) {
        this.cutCable = cutCable;
    }

    private void checkIfStopped(BossFightEntity[] cables, Scissors scissors) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && !isStopped) {
            isStopped = true;
            this.checkOverlap(cables, scissors);
        }

        // Below is code for testing
        //        else if  (Gdx.input.isKeyJustPressed(Input.Keys.SPACE) && isStopped) {
        //          isStopped = false;
        //        }
    }

    public void moveScissors(
            Viewport viewport, SpriteBatch batch, Scissors scissors, BossFightEntity[] cables) {
        this.checkIfStopped(cables, scissors);
        if (!isStopped) {
            scissors.move(viewport);
        }

        scissors.draw(batch);
    }

    public void drawCables(BossFightEntity[] cables, SpriteBatch batch) {
        for (int i = 0; i < cables.length; i++) {
            cables[i].draw(batch);
        }
    }

    public void checkOverlap(BossFightEntity[] cables, Scissors scissors) {
        System.out.println("Overlap is being checked");
        for (int i = 0; i < cables.length; i++) {
            System.out.println("Cable: " + i);
            if (scissors.overlaps(cables[i])) {
                System.out.println("Overlaps!");
                if (this.cableStatus[i]) {
                    this.cableStatus[i] = false;
                    updateSprite(cables[i]);
                }
            }
        }
    }

    private void updateSprite(BossFightEntity cable) {
        cable.setTexture(this.cutCable);
    }
}
