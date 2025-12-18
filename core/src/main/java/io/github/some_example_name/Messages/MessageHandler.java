package io.github.some_example_name.Messages;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

import io.github.some_example_name.StumbleHome;

public class MessageHandler {
    private StumbleHome game;
    private Map<Messages, MessageID> messages;

    public MessageHandler(StumbleHome game) {
        this.game = game;
        messages = new HashMap<Messages, MessageID>();
        for (Messages mssg : Messages.values()) {
            messages.put(mssg, null);
        }
    }

    public boolean addMessage(Messages mssg, String message_text, Color colour, float size) {
        if (messages.containsKey(mssg)) {
            messages.put(mssg, new MessageID(message_text, colour, size));
            return true;
        }
        return false;
    }

    public void updateMessages() {
        for (Messages msg : Messages.values()) {
            if (!(messages.get(msg).time_left <= 0)) {
                drawCenteredText(messages.get(msg));
                shift_time(msg, -Gdx.graphics.getDeltaTime());
            }
        }
    }

    public void updateMessage(boolean show_msg, Messages message) {
        if (show_msg) {
            drawCenteredText(messages.get(message));
        }
    }

    public void set_time(Messages msg, float new_time) {
        messages.get(msg).time_left = new_time;
    }
    public void shift_time(Messages msg, float time_difference) {
        messages.get(msg).time_left += time_difference;
    }
    /**
     * Draws text centered on the screen with a given color and scale.
     *
     * @param text  the text to display.
     * @param color the color of the text.
     * @param scale the scaling factor of the font size.
     */
    private void drawCenteredText(MessageID mssg_id) {
        game.batch.setProjectionMatrix(
            game.camera.projection.cpy().setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight())
        );

        game.batch.begin();
        game.font.getData().setScale(mssg_id.size);
        game.font.setColor(mssg_id.colour);

        GlyphLayout layout = new GlyphLayout(game.font, mssg_id.message);
        float x = (Gdx.graphics.getWidth() - layout.width) / 2f;
        float y = (Gdx.graphics.getHeight() + layout.height) / 2f;
        game.font.draw(game.batch, layout, x, y);

        game.font.getData().setScale(1f);
        game.batch.end();
    }

}
