package io.github.stumblehome.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.stumblehome.StumbleHome;
import java.util.ArrayList;
import java.util.List;

public class Leaderboard extends MenuScreen {
    private ScrollPane scrollPane;

    public Leaderboard(StumbleHome game) {
        super(game);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Draws leaderboard screen
        String boardData = readLeaderBoard();
        Table leaderboard = leaderboardSetup(boardData);

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
    public void hide() {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'hide'");
    }

    @Override
    public void dispose() {
        stage.dispose();
        skin.dispose();
        scrollPane.clear();
    }

    public Table leaderboardSetup(String boardData) {
        // Adds title to leaderboard
        Table leaderboard = new Table();
        Label tempRow = new Label("Leaderboard (Top 5):", skin);
        tempRow.setFontScale(5f);
        leaderboard.add(tempRow).pad(15);
        leaderboard.row();

        // Adds error message if leaderboard doesn't exist
        if (boardData.equals("Complete Game To Set Score")) {
            tempRow = new Label(boardData, skin);
            tempRow.setFontScale(3f);
            leaderboard.add(tempRow).pad(15);
            leaderboard.row();
        }

        // Otherwise read player scores into leaderboard
        else {
            // Reads in csv line by line
            String[] splitBoard = boardData.split("\n");
            List<String[]> ordered = orderValues(splitBoard);
            // Inserts each score into the leaderboard

            // Displays top 5 scores
            int i = 0;
            while (i < 5 && i < ordered.size()) {
                String[] s = ordered.get(i);
                tempRow = new Label(s[0] + ": " + s[1], skin);
                tempRow.setFontScale(3f);
                leaderboard.add(tempRow).pad(10);
                leaderboard.row();
                i++;
            }
        }
        return leaderboard;
    }

    public List<String[]> orderValues(String[] splitBoard) {
        List<String[]> ordered = new ArrayList<>();

        // Orders csv table rows based on score
        // Uses insertion sort
        for (String s : splitBoard) {
            // Splits rows into values
            String[] tempData = s.split(",");

            if (tempData.length >= 2) {
                if (ordered.size() > 0) {
                    int pos = 0;

                    while (pos != ordered.size()
                            && Integer.valueOf(ordered.get(pos)[1])
                                    > Integer.valueOf(tempData[1])) {
                        pos += 1;
                    }
                    ordered.add(pos, tempData);
                } else {
                    ordered.add(tempData);
                }
            }
        }
        return ordered;
    }

    private String readLeaderBoard() {
        boolean file_exists = Gdx.files.local("leaderBoard.csv").exists();

        if (file_exists == false) {
            return "Complete Game To Set Score";
        } else {
            FileHandle file = Gdx.files.local("leaderBoard.csv");
            return file.readString();
        }
    }
}
