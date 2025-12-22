package io.github.stumblehome.Messages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import io.github.stumblehome.StumbleHome;
import java.util.HashMap;
import java.util.Map;

/**
 * A handler class for the messages that the game will show, one should be initialised per game
 *
 * @author Isaac M
 */
public class MessageHandler {
    private StumbleHome game;
    private Map<Messages, MessageID> messages;

    /**
     * instantiates a message handler for a game
     *
     * @param game that the message handler is used in
     */
    public MessageHandler(StumbleHome game) {
        this.game = game;
        messages = new HashMap<Messages, MessageID>();
        for (Messages mssg : Messages.values()) {
            messages.put(mssg, null);
        }
    }

    /**
     * adds a message to the map of stored messages, must correspond to an entry in the Messages.java
     * enum
     *
     * @param mssg the enum entry that the message corresponds to
     * @param message_text the text that the message should show
     * @param colour the color that the text should appear as
     * @param size the size of the text
     * @return true if mssg is valid and the message was added, false if it was not
     */
    public boolean addMessage(Messages mssg, String message_text, Color colour, float size) {
        if (messages.containsKey(mssg)) {
            messages.put(mssg, new MessageID(message_text, colour, size));
            return true;
        }
        return false;
    }

    /**
     * must be called every frame that you want messages to be running for for every message in
     * messages checks if it has time left being shown, if so then it draws it and reduces its time by
     * delta
     */
    public void updateMessages() {
        for (Messages msg : Messages.values()) {
            if (!(messages.get(msg).time_left <= 0)) {
                drawCenteredText(messages.get(msg));
                shift_time(msg, -Gdx.graphics.getDeltaTime());
            }
        }
    }

    /**
     * draws a message if a boolean value is set, must be run every frame
     *
     * @param show_msg the boolena value, if true the message is drawn, if false it is not and this
     *     method does nothing
     * @param message the key of the message to be shown
     */
    public void updateMessage(boolean show_msg, Messages message) {
        if (show_msg) {
            drawCenteredText(messages.get(message));
        }
    }

    /**
     * allows the time a message is shown for to be set to a value
     *
     * @param msg key of the message being referenced
     * @param new_time the new time that the message will be shown for, overrides previous time
     */
    public void set_time(Messages msg, float new_time) {
        messages.get(msg).time_left = new_time;
    }

    /**
     * allows the remaining time a message is shown for to be increased/decreased
     *
     * @param msg key of the message being referenced
     * @param time_difference the amount by which the time remaining is changed by, the new time is
     *     the old time + the time_difference
     */
    public void shift_time(Messages msg, float time_difference) {
        messages.get(msg).time_left += time_difference;
    }

    /**
     * Draws text centered on the screen with a given color and scale.
     *
     * @param mssg_id the key of the message being drawn
     */
    private void drawCenteredText(MessageID mssg_id) {
        game.batch.setProjectionMatrix(
                game.camera
                        .projection
                        .cpy()
                        .setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

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
