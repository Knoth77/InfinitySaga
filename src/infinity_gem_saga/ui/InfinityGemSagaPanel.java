package infinity_gem_saga.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeMap;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import static infinity_gem_saga.InfinityGemSagaConstants.*;
import infinity_gem_saga.data.InfinityGemSagaDataModel;
import infinity_gem_saga.data.InfinityGemSagaRecord;

/**
 * This class performs all of the rendering for the Mahjong game application.
 *
 * @author Michael Knoth
 */
public class InfinityGemSagaPanel extends JPanel
{
    // THIS IS ACTUALLY OUR Mahjong Solitaire APP, WE NEED THIS
    // BECAUSE IT HAS THE GUI STUFF THAT WE NEED TO RENDER

    private MiniGame game;
    // WE'LL USE THIS TO FORMAT SOME TEXT FOR DISPLAY PURPOSES
    private NumberFormat numberFormatter;
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING UNSELECTED TILES
    private BufferedImage blankTileImage;
    // WE'LL USE THIS AS THE BASE IMAGE FOR RENDERING SELECTED TILES
    private BufferedImage blankTileSelectedImage;
    private InfinityGemSagaDataModel data;

    /**
     * This constructor stores the game and data references, which we'll need
     * for rendering.
     *
     * @param initGame the Mahjong Solitaire game that is using this panel for
     * rendering.
     *
     * @param initData the Mahjong Solitaire game data.
     */
    public InfinityGemSagaPanel(MiniGame initGame, InfinityGemSagaDataModel initData)
    {
        game = initGame;
        data = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
    }

    // MUTATOR METHODS
    // -setBlankTileImage
    // -setBlankTileSelectedImage
    /**
     * This mutator method sets the base image to use for rendering tiles.
     *
     * @param initBlankTileImage The image to use as the base for rendering
     * tiles.
     */
    public void setBlankTileImage(BufferedImage initBlankTileImage)
    {
        blankTileImage = initBlankTileImage;
    }

    /**
     * This mutator method sets the base image to use for rendering selected
     * tiles.
     *
     * @param initBlankTileSelectedImage The image to use as the base for
     * rendering selected tiles.
     */
    public void setBlankTileSelectedImage(BufferedImage initBlankTileSelectedImage)
    {
        blankTileSelectedImage = initBlankTileSelectedImage;
    }

    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     *
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g)
    {
        try
        {
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();

            // CLEAR THE PANEL
            super.paintComponent(g);

            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);

            // AND THE BUTTONS AND DECOR
            renderGUIControls(g);

            if (((InfinityGemSagaMiniGame) game).getCurrentScreenState().equals(GAME_SCREEN_STATE))
                renderTiles(g);


            // AND THE DIALOGS, IF THERE ARE ANY
            renderDialogs(g);

            renderGrid(g);

            if (((InfinityGemSagaMiniGame) game).getCurrentScreenState().equals(SAGA_SCREEN_STATE)
                    || ((InfinityGemSagaMiniGame) game).getCurrentScreenState().equals(GAME_SCREEN_STATE))
                renderStats(g);

        }
        finally
        {
            // RELEASE THE LOCK
            game.endUsingData();
        }
    }

    // RENDERING HELPER METHODS
    // - renderBackground
    // - renderGUIControls
    // - renderTiles
    // - renderDialogs
    // - renderGrid
    // - renderDebuggingText
    /**
     * Renders the background image, which is different depending on the screen.
     *
     * @param g the Graphics context of this panel.
     */
    public void renderBackground(Graphics g)
    {
        // THERE IS ONLY ONE CURRENTLY SET
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
    }

    public void renderTiles(Graphics g)
    {

        InfinityGemSagaGem[][] tileGrid = data.getTileGrid();
        for (int i = 0; i < data.getGridColumns(); i++)
        {
            for (int j = 0; j < data.getGridRows(); j++)
            {

                InfinityGemSagaGem tile = tileGrid[i][j];
                renderTile(g, tile);
            }
        }


        // THEN DRAW ALL THE MOVING TILES
        Iterator<InfinityGemSagaGem> movingTiles = data.getMovingTiles();
        while (movingTiles.hasNext())
        {
            InfinityGemSagaGem tile = movingTiles.next();
            renderTile(g, tile);
        }
    }

    public void renderTile(Graphics g, InfinityGemSagaGem tileToRender)
    {
        // ONLY RENDER VISIBLE TILES
        // if (!tileToRender.getState().equals(INVISIBLE_STATE)) {
        // FIRST DRAW THE BLANK TILE IMAGE
        if (tileToRender.getState().equals(SELECTED_STATE))
            g.drawImage(blankTileSelectedImage, (int) tileToRender.getX(), (int) tileToRender.getY(), null);
        else if (tileToRender.getState().equals(VISIBLE_STATE))
            g.drawImage(blankTileImage, (int) tileToRender.getX(), (int) tileToRender.getY(), null);

        if (tileToRender.getBoardNum() == 2)
        {
            g.drawImage(blankTileSelectedImage, (int) tileToRender.getX(), (int) tileToRender.getY(), null);
        }
        // THEN THE TILE IMAGE
        SpriteType bgST = tileToRender.getSpriteType();
        Image img = bgST.getStateImage(tileToRender.getState());
        g.drawImage(img, (int) tileToRender.getX() + TILE_IMAGE_OFFSET, (int) tileToRender.getY() + TILE_IMAGE_OFFSET, bgST.getWidth(), bgST.getHeight(), null);

        // IF THE TILE IS SELECTED, HIGHLIGHT IT
        if (tileToRender.getState().equals(SELECTED_STATE))
        {
            g.setColor(SELECTED_TILE_COLOR);
            g.fillRoundRect((int) tileToRender.getX(), (int) tileToRender.getY(), bgST.getWidth(), bgST.getHeight(), 5, 5);
        }
        else if (tileToRender.getState().equals(INCORRECTLY_SELECTED_STATE))
        {
            g.setColor(INCORRECTLY_SELECTED_TILE_COLOR);
            g.fillRoundRect((int) tileToRender.getX(), (int) tileToRender.getY(), bgST.getWidth(), bgST.getHeight(), 5, 5);
        }
        if (tileToRender.getBoardNum() == 2)
        {
            g.setColor(JELLY_COLOR);
            g.fillRoundRect((int) tileToRender.getX(), (int) tileToRender.getY(), bgST.getWidth(), bgST.getHeight(), 5, 5);
        }
    }

    public void renderGrid(Graphics g)
    {
        // ONLY RENDER THE GRID IF WE'RE DEBUGGING
        if (data.isDebugTextRenderingActive())
        {
            for (int i = 0; i < data.getGridColumns(); i++)
            {
                for (int j = 0; j < data.getGridRows(); j++)
                {
                    int x = data.getRowPos(i, 0);
                    int y = data.getColumnPos(j, 0);
                    g.drawRect(x, y, TILE_IMAGE_WIDTH, TILE_IMAGE_HEIGHT);
                }
            }
        }
    }

    /**
     * Renders all the GUI decor and buttons.
     *
     * @param g this panel's rendering context.
     */
    public void renderGUIControls(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites)
        {
            renderSprite(g, s);
        }

        // AND NOW RENDER THE BUTTONS
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
        {
            renderSprite(g, s);
        }
    }

    public void renderStats(Graphics g)
    {
        // RENDER THE GAME TIME

        /*
         * if (tileToRender.getState().equals(SELECTED_STATE))
         g.drawImage(blankTileSelectedImage, (int) tileToRender.getX(), (int) tileToRender.getY(), null);
         */
        if (((InfinityGemSagaMiniGame) game).isCurrentScreenState(GAME_SCREEN_STATE)
                && data.inProgress())
        {

            String strNum = "" + data.movesLimit;
            g.setFont(new Font("default", Font.BOLD, 100));
            g.setColor(Color.YELLOW);
            g.drawString(strNum, 100, 100);

            String strScore = "" + data.score;
            g.setFont(new Font("default", Font.BOLD, 100));
            g.setColor(Color.YELLOW);
            g.drawString(strScore, 1000, 100);
        }

        if (((InfinityGemSagaMiniGame) game).getGUIDecor().get(WIN_DIALOG_TYPE).getState().equals(VISIBLE_STATE))
        {
            String strScore = "" + data.score;
            InfinityGemSagaRecord record = ((InfinityGemSagaMiniGame) game).getPlayerRecord();
            int stars = record.getStars(data.getCurrentLevel());

            g.setFont(new Font("default", Font.BOLD, 50));
            g.setColor(Color.RED);
            g.drawString("You WIN!", 525, 240);

            g.setFont(new Font("default", Font.BOLD, 20));
            g.setColor(Color.RED);
            g.drawString("Score : " + strScore, 535, 295);

            if (stars >= 1)
                ((InfinityGemSagaMiniGame) game).getGUIButtons().get(STAR_ONE_TYPE).setState(VISIBLE_STATE);
            if (stars >= 2)
                ((InfinityGemSagaMiniGame) game).getGUIButtons().get(STAR_TWO_TYPE).setState(VISIBLE_STATE);
            if (stars == 3)
                ((InfinityGemSagaMiniGame) game).getGUIButtons().get(STAR_THREE_TYPE).setState(VISIBLE_STATE);

            ((InfinityGemSagaMiniGame) game).getGUIButtons().get(WIN_QUIT_TYPE).setState(VISIBLE_STATE);
            ((InfinityGemSagaMiniGame) game).getGUIButtons().get(WIN_QUIT_TYPE).setEnabled(true);
        }

        if (((InfinityGemSagaMiniGame) game).getGUIDecor().get(LOSE_DIALOG_TYPE).getState().equals(VISIBLE_STATE))
        {
            g.setFont(new Font("default", Font.BOLD, 50));
            g.setColor(Color.RED);
            g.drawString("Game Over", 525, 240);
            
            ((InfinityGemSagaMiniGame) game).getGUIButtons().get(TRY_AGAIN_TYPE).setState(VISIBLE_STATE);
            ((InfinityGemSagaMiniGame) game).getGUIButtons().get(TRY_AGAIN_TYPE).setEnabled(true);
        }
        
        if (((InfinityGemSagaMiniGame) game).isStatScreen)
        {
            InfinityGemSagaRecord record = ((InfinityGemSagaMiniGame) game).getPlayerRecord();
            int stars = record.getStars(data.getCurrentLevel());
            int highScore = record.getHighScore(data.getCurrentLevel());


            if (stars >= 1)
                ((InfinityGemSagaMiniGame) game).getGUIButtons().get(STAR_ONE_TYPE).setState(VISIBLE_STATE);
            if (stars >= 2)
                ((InfinityGemSagaMiniGame) game).getGUIButtons().get(STAR_TWO_TYPE).setState(VISIBLE_STATE);
            if (stars == 3)
                ((InfinityGemSagaMiniGame) game).getGUIButtons().get(STAR_THREE_TYPE).setState(VISIBLE_STATE);

            int target = 0;
            String temp = data.getCurrentLevel();
            temp = temp.substring(25);
            String lvl = "";
            switch (temp)
            {
                case "LevelOne.zom":
                    target = LEVEL_ONE_TARGET;
                    lvl = "Level One";
                    break;
                case "LevelTwo.zom":
                    target = LEVEL_TWO_TARGET;
                    lvl = "Level Two";
                    break;
                case "LevelThree.zom":
                    target = LEVEL_THREE_TARGET;
                    lvl = "Level Three";
                    break;
                case "LevelFour.zom":
                    target = LEVEL_FOUR_TARGET;
                    lvl = "Level Four";
                    break;
                case "LevelFive.zom":
                    target = LEVEL_FIVE_TARGET;
                    lvl = "Level Five";
                    break;
                case "LevelSix.zom":
                    target = LEVEL_SIX_TARGET;
                    lvl = "Level Six";
                    break;
                case "LevelSeven.zom":
                    target = LEVEL_SEVEN_TARGET;
                    lvl = "Level Seven";
                    break;
                case "LevelEight.zom":
                    target = LEVEL_EIGHT_TARGET;
                    lvl = "Level Eight";
                    break;
                case "LevelNine.zom":
                    target = LEVEL_NINE_TARGET;
                    lvl = "Level Nine";
                    break;
                case "LevelTen.zom":
                    target = LEVEL_TEN_TARGET;
                    lvl = "Level Ten";
                    break;

            }

            g.setFont(new Font("default", Font.BOLD, 50));
            g.setColor(Color.RED);
            g.drawString(lvl, 525, 240);

            g.setFont(new Font("default", Font.PLAIN, 20));
            g.setColor(Color.red);
            g.drawString("Target Points: " + target, 550, 300);


            g.setFont(new Font("default", Font.PLAIN, 20));
            g.setColor(Color.red);
            g.drawString("HighScore: " + highScore, 550, 450);
        }
    }

    public void renderDialogs(Graphics g)
    {
        // GET EACH DECOR IMAGE ONE AT A TIME
        Collection<Sprite> dialogSprites = game.getGUIDialogs().values();
        for (Sprite s : dialogSprites)
        {
            // RENDER THE DIALOG, NOTE IT WILL ONLY DO IT IF IT'S VISIBLE
            renderSprite(g, s);
        }
    }

    /**
     * Renders the s Sprite into the Graphics context g. Note that each Sprite
     * knows its own x,y coordinate location.
     *
     * @param g the Graphics context of this panel
     *
     * @param s the Sprite to be rendered
     */
    public void renderSprite(Graphics g, Sprite s)
    {
        // ONLY RENDER THE VISIBLE ONES
        if (!s.getState().equals(INVISIBLE_STATE))
        {
            SpriteType bgST = s.getSpriteType();
            Image img = bgST.getStateImage(s.getState());
            g.drawImage(img, (int) s.getX(), (int) s.getY(), bgST.getWidth(), bgST.getHeight(), null);
        }
    }
}