package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stumblehome.StumbleHome;
import java.util.Map;

/**
 * Class to display achievements
 *
 * @author Henry
 */
public class Achievements extends MenuScreen {
    private ScrollPane scrollPane;

    // Map containing achievements completed
    private Map<String, Integer> achievementData;

    // Contains all achievements in the game
    private String[][] achievements;
    // Used to select mode (last player's achievement status or list)
    private boolean playerAchievements;

    /**
     * constructor for screen when entering from win screen
     * @param game the game that is being referenced
     * @param achievementData the achievement data for the game just won
     */
    public Achievements(StumbleHome game, Map<String, Integer> achievementData) {
        super(game);
        this.achievementData = achievementData;
        playerAchievements = true;
    }

    /**
     * constructor for screen when entering from main menu screen
     * @param game the game that is being referenced
     * no achievements are listed as completed
     */
    public Achievements(StumbleHome game) {
        super(game);
        playerAchievements = false;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Draws achievement screen screen
        Table leaderboard = achievementSetup();

        this.scrollPane = new ScrollPane(leaderboard, skin);
        scrollPane.setFillParent(true);
        stage.addActor(scrollPane);
    }

    /**
     * Called once per frame to render the menu screen Edited to capture input for scrollable table
     *
     * @param delta the time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        Gdx.input.setInputProcessor(stage);
        scrollPane.setScrollbarsVisible(true);
        ScreenUtils.clear(Color.BLACK);

        game.batch.setProjectionMatrix(
                game.camera
                        .projection
                        .cpy()
                        .setToOrtho2D(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        game.batch.begin();

        if (background != null) {
            game.batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        game.batch.end();
        stage.act(delta);
        stage.draw();

        // Escape button click
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            this.dispose();
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        scrollPane.clear();
    }

    /**
     * Sets up achievement table for display
     * Can display player achievements if object instantiated with achievementData
     * @return formatted table with achievements
     */
    public Table achievementSetup() {
        // Adds all achievements & Hints
        achievements =
                new String[][] {
                    {"Glad that wasn't Vodka!", "(AKA: Collect Water Bottle)"},
                    {"Swipe the card!", "(AKA: Collect Keycard)"},
                    {"Broken Ankle", "(AKA: Trip On The Stick)"},
                    {"Here's Johnny!", "(AKA: Collect Chainsaw)"},
                    {"BEER ME!", "(AKA: Get drunk from Beer)"},
                    {"Down the vodka", "(AKA: Get drunk from Vodka)"},
                    {"Lava Chicken... TASTY AS HELL", "(AKA: Collect Chicken)"},
                    {"War of the Roses...", "(AKA: Crush Red Rose)"},
                    {"Traitor!!!", "(AKA: Crush White Rose)"},
                    {"Switched sides have you??", "(AKA: Crush Both Roses)"},
                    {"Someone didn't like SYS1...", "(AKA: Collect Bob)"},
                    {"Raised from the dead... RUN!", "(AKA: Encounter LongBoi)"},
                    {"Feed the Bird", "(AKA: Collect Bird Food)"},
                    {"Mix & Blackout", "(AKA: Drink Beer & Vodka Consecutively)"}
                };

        // Adds title to leaderboard
        Table leaderboard = new Table();
        Label tempRow = new Label("Achievements:", skin);
        tempRow.setFontScale(5f);
        leaderboard.add(tempRow).pad(15);
        leaderboard.row();

        // Adds ESC Hint
        tempRow = new Label("Press ESC to exit", skin);
        tempRow.setFontScale(1f);
        leaderboard.add(tempRow).pad(5);
        leaderboard.row();

        // Displays correct type of achievement board
        if (playerAchievements) {
            // Adds achievement & collected status
            for (String[] achievement : achievements) {
                if (achievementData.containsKey(achievement[0])) {
                    tempRow = new Label((achievement[0] + ": Achieved"), skin);
                } else {
                    tempRow = new Label((achievement[0] + ": X"), skin);
                }

                tempRow.setFontScale(3f);
                leaderboard.add(tempRow).padTop(15);
                leaderboard.row();

                // Adds points gained
                if (achievementData.containsKey(achievement[0])) {
                    tempRow =
                            new Label(
                                    achievement[1]
                                            + " +"
                                            + achievementData.get(achievement[0]).toString()
                                            + " points",
                                    skin);
                }
                // Adds points available message
                else {
                    tempRow = new Label(achievement[1] + " Collect For Points", skin);
                }
                tempRow.setFontScale(1f);
                leaderboard.add(tempRow).pad(5);
                leaderboard.row();
            }
        } else {
            // Adds all achievements
            for (String[] achievement : achievements) {
                tempRow = new Label(achievement[0], skin);
                tempRow.setFontScale(3f);
                leaderboard.add(tempRow).padTop(15);
                leaderboard.row();

                // Adds points available message
                tempRow = new Label(achievement[1] + " Collect For Points", skin);
                tempRow.setFontScale(1f);
                leaderboard.add(tempRow).pad(5);
                leaderboard.row();
            }
        }
        return leaderboard;
    }
}
