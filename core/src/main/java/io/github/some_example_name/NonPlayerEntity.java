package io.github.some_example_name;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;

public abstract class NonPlayerEntity extends Entity {

    public NonPlayerEntity(Sprite sprite, float size, int frame_width, int frame_height, int num_animations){
        super(sprite, size);

        initialiseAnimations(frame_width, frame_height, num_animations);
    }

    private void initialiseAnimations(int frame_width, int frame_height, int num_animations) {
        TextureRegion[] texture_frames = new TextureRegion[num_animations];
        for (int frame = 0; frame < num_animations; frame++) {
            for (int side_frame = 0; side_frame < 2; side_frame++) {
                texture_frames[frame] = new TextureRegion(getTexture(), frame_width * side_frame,
                    frame_height * (int) (frame / 2), frame_width, frame_height);
                frame++;
            }
        }
        current_animation = new Animation<>(0.3f, texture_frames);
        state_time = 0f;
    }


    protected float getCentreX(){
        return x_pos + frame_size / 2;
    }

    protected float getCentreY(){
        return y_pos + frame_size / 2;
    }


}
