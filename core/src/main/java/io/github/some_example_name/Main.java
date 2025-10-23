package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    // declare the variables here
    SpriteBatch spriteBatch;
    FitViewport viewport;
    Texture playerTexture;
    Texture backgroundTexture;
    sprite playerSprite;


    @Override
    public void create() {
        // load the assets here
        backgroundTexture = new Texture("background.png");
        playerTexture = new Texture("player.png");

        playerSprite = new Sprite(playerTexture); // Initialize the sprite based on the texture
        playerSprite.setSize(1, 1);

        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
    }
//
    @Override
    public void render() {

        inpit();
        logic();
        draw();

        privet void input(){
            float speed = .25f;
            float delta = Gdx.graphics.getDeltaTime();

            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                playerSprite.translateX(speed * delta);
                // todo: Do something when the user presses the right arrow
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                playerSprite.translateX(speed * delta);
                // todo: Do something when the user presses the LEFT arrow
            }
        }privet void logic(){
            // Store the worldWidth and worldHeight as local variables for brevity
            float worldWidth = viewport.getWorldWidth();
            float worldHeight = viewport.getWorldHeight();

            // Clamp x to values between 0 and worldWidth
            playerSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth));

        }

    @Override
    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);
        spriteBatch.begin();

        // i think here is the texture for the map
        //everything about rendering should be here
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight);
        playerSprite.draw(spriteBatch);

        spriteBatch.end();
    }


    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}
