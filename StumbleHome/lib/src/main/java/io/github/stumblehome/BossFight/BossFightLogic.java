package io.github.stumblehome.BossFight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class BossFightLogic {

    private boolean isStopped = false;

    private void checkIfStopped() {
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            isStopped = true;
        }
    }

    public void moveScissors(Viewport viewport, SpriteBatch batch, Scissors scissors) {
        this.checkIfStopped();
        if (!isStopped) {
            scissors.move(viewport);
        }

        scissors.draw(batch);
    }
}
