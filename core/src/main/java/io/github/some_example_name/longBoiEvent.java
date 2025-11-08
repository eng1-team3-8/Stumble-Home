package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class longBoiEvent extends Sprite {
    // long boi animation sheet
    public Texture longSheet;

    private TextureRegion idleFrame;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> previousAnimation;
    private float stateTime;

    // position
    public float longX;
    public float longY;
    public final float longSize = 2f;

    public boolean near = false;

    // walking loop variables
    private int xDirection = 70;
    private int yDirection = 220;
    private int xDirection2 = 250;
    private int yDirection2 = 70;
    private int xDirection3 = 70;
    private int yDirection3 = 150;
    private int xDirection4 = 60;
    private int yDirection4 = 120;
    private int xLoopCounter = 0;
    private int yLoopCounter = 0;
    private int xLoopCounter2 = 0;
    private int yLoopCounter2 = 0;
    private int xLoopCounter3 = 0;
    private int yLoopCounter3 = 0;
    private int xLoopCounter4 = 0;
    private int yLoopCounter4 = 0;

    public boolean doneWalk = false;

    public boolean collided = false;

    // distance to be near long boi for him to start walking
    public float radius = 6;

    public longBoiEvent(Sprite sprite) {
        super(sprite);

        // initialize animations
        initializeAnimations();
    }

    private void initializeAnimations() {
        int frameWidth = 32;
        int frameHeight = 32;

        longSheet = this.getTexture();

        // set idle frame
        idleFrame = new TextureRegion(longSheet, 0, 32, frameWidth, frameHeight);

        // walking frames
        TextureRegion[] longFrames = new TextureRegion[2];
        longFrames[0] = new TextureRegion(longSheet, 0, 0, frameWidth, frameHeight);
        longFrames[1] = new TextureRegion(longSheet, 32, 0, frameWidth, frameHeight);
        Animation<TextureRegion> animation = new Animation<>(0.3f, longFrames);

        stateTime = 0f;

        currentAnimation = animation;
    }

    public void logic() {
        if (near) {
            // Reset animation time if animation changed
            if (currentAnimation != previousAnimation) {
                stateTime = 0f;
                previousAnimation = currentAnimation;
            }

            // Update animation time
            stateTime += Gdx.graphics.getDeltaTime();
        }
    }

    public boolean checkNear(float playerCentreX, float playerCentreY) {
        if (near) {
            return false;
        }

        boolean nearing;
        //calculate distance between player centre and long boi centre using pythagoras
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        nearing = distance < radius;

        if (nearing) {
            near = true;
            System.out.println("near long boi.");
        }
        return nearing;
    }

    private float getCentreX(){
        return longX + longSize / 2;
    }

    private float getCentreY(){
        return longY + longSize / 2;
    }

    public void draw(SpriteBatch batch) {
        TextureRegion frameToDraw;
        if (!near) {
            frameToDraw = idleFrame;
        }
        else {
            frameToDraw = currentAnimation.getKeyFrame(stateTime, true);
        }

        // draw long boi
        batch.draw(frameToDraw, longX, longY, longSize, longSize);
    }

    public boolean checkCollision(float playerCentreX, float playerCentreY) {
        if (collided) {
            return false;
        }

        boolean colliding;
        //calculate distance between player centre and long boi centre using pythagoras
        double distance = sqrt(pow(playerCentreX - getCentreX(), 2) + pow(playerCentreY - getCentreY(), 2));

        // set nearing to true if player within (value of radius) of long boi
        colliding = distance < 1;

        if (colliding) {
            collided = true;
            System.out.println("Collided with long boi.");
        }
        return colliding;
    }

    public void walkPath(){
        float delta = Gdx.graphics.getDeltaTime();
        float speed = 3f;
        if (xLoopCounter < xDirection) {
            longX += delta*speed;
            xLoopCounter++;
        } else if (yLoopCounter < yDirection) {
            longY += delta*speed;
            yLoopCounter++;
        } else if (xLoopCounter2 < xDirection2) {
            longX -= delta*speed;
            xLoopCounter2++;
        } else if (yLoopCounter2 < yDirection2) {
            longY-= delta*speed;
            yLoopCounter2++;
        } else if (xLoopCounter3 < xDirection3) {
            longX -= delta*speed;
            xLoopCounter3++;
        } else if (yLoopCounter3 < yDirection3) {
            longY += delta*speed;
            yLoopCounter3++;
        } else if (xLoopCounter4 < xDirection4) {
            longX += delta*speed;
            xLoopCounter4++;
        } else if (yLoopCounter4 < yDirection4) {
            longY += delta * speed;
            yLoopCounter4++;
        } else {
            doneWalk = true;
        }
    }
}
