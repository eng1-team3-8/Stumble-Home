package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;


// handles keycard pickup - needed to unlock the exit
public class keycardEvent extends Sprite {

    public Texture keycardSheet;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> previousAnimation;
    private float stateTime;

    // where the keycard sits on the map
    public float keycardX;
    public float keycardY;
    public final float keycardSize = 1.0f;

    private boolean collected = false;

    public keycardEvent(Sprite sprite) {
        super(sprite);
        initializeAnimations();
    }

    // loads the 3 frames from keyCard.png
    private void initializeAnimations() {
        int frameWidth = 32;
        int frameHeight = 32;

        keycardSheet = this.getTexture();

        // grab frames: top-left, middle-left, top-right
        TextureRegion[] keycardFrames = new TextureRegion[3];
        keycardFrames[0] = new TextureRegion(keycardSheet, 0, 0, frameWidth, frameHeight);
        keycardFrames[1] = new TextureRegion(keycardSheet, 0, 32, frameWidth, frameHeight);
        keycardFrames[2] = new TextureRegion(keycardSheet, 32, 0, frameWidth, frameHeight);

        currentAnimation = new Animation<>(0.3f, keycardFrames);
        stateTime = 0f;
    }

    // updates animation timer each frame
    public void logic() {
        if (!collected) {
            if (currentAnimation != previousAnimation) {
                stateTime = 0f;
                previousAnimation = currentAnimation;
            }
            stateTime += Gdx.graphics.getDeltaTime();
        }
    }

    // draws keycard on screen if not picked up yet
    public void draw(SpriteBatch batch) {
        if (!collected) {
            TextureRegion frameToDraw = currentAnimation.getKeyFrame(stateTime, true);
            batch.draw(frameToDraw, keycardX, keycardY, keycardSize, keycardSize);
        }
    }

    // checks if player touched the keycard
    public boolean checkCollision(float playerCentreX, float playerCentreY) {
        if (collected) {
            return false;
        }

        boolean colliding;
        //calculate distance between player centre and long boi centre using pythagoras
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        colliding = distance < 1;

        if (colliding) {
            collected = true;
            System.out.println("Keycard collected! You can now exit.");
        }

        return colliding;
    }

    private float getCentreX(){
        return keycardX + keycardSize / 2;
    }

    private float getCentreY(){
        return keycardY + keycardSize / 2;
    }

    public boolean isCollected() {
        return collected;
    }
}
