package io.github.some_example_name.Messages;
import com.badlogic.gdx.graphics.Color;

public class MessageID {
    public String message;
    public Color colour;
    public float size;
    public float time_left;

    public MessageID(String message, Color colour, float size) {
        this.message = message;
        this.colour = colour;
        this.size = size;
        this.time_left = 0f;
    }
}
