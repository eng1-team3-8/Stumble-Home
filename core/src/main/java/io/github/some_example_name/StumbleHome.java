package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StumbleHome extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    public OrthographicCamera camera;

    final float VIEWPORT_HEIGHT = 12;  // Zoomed in closer to see player navigate paths

    public Viewport viewport;


    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();

        // aspect ratio = width/height.
        float aspectRatio = (float) Gdx.graphics.getWidth() / (float) Gdx.graphics.getHeight();

        // create orthographic camera
        camera = new OrthographicCamera();
        viewport = new FitViewport(VIEWPORT_HEIGHT*aspectRatio, VIEWPORT_HEIGHT, camera);
        viewport.apply();
        camera.position.set(viewport.getWorldWidth()/2, VIEWPORT_HEIGHT/2, 0);
        camera.update();

        this.setScreen(new MainMenuScreen(this));
    }

    public void render(){
        super.render();
    }

    public void dispose(){
        batch.dispose();
        if (font != null) {
            font.dispose();
        }
        // need to dispose everything in gameScreen dispose method, as dispose method in
        // screen class is not automatically called
    }
}
