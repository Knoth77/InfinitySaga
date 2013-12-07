package infinity_gem_saga.data;

import infinity_gem_saga.InfinityGemSaga.InfinityGemSagaPropertyType;
import static infinity_gem_saga.InfinityGemSagaConstants.*;
import infinity_gem_saga.ui.InfinityGemSagaGem;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import infinity_gem_saga.ui.InfinityGemSagaPanel;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Random;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;

/**
 * This class manages the game data for Mahjong Solitaire.
 *
 * @author Richard McKenna
 */
public class InfinityGemSagaDataModel extends MiniGameDataModel
{
    // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES

    private MiniGame miniGame;
    // THE LEVEL GRID REFERS TO THE LAYOUT FOR A GIVEN LEVEL, MEANING
    // HOW MANY TILES FIT INTO EACH CELL WHEN FIRST STARTING A LEVEL
    private int[][] levelGrid;
    // LEVEL GRID DIMENSIONS
    private int gridColumns;
    private int gridRows;
    // THIS STORES THE TILES ON THE GRID DURING THE GAME
    private InfinityGemSagaGem[][] gemGrid;
    // THESE ARE THE TILES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
    private ArrayList<InfinityGemSagaGem> initialGems;
    private ArrayList<InfinityGemSagaGem> movingGems;
    // THIS IS A SELECTED TILE, MEANING THE FIRST OF A PAIR THE PLAYER
    // IS TRYING TO MATCH. THERE CAN ONLY BE ONE OF THESE AT ANY TIME
    private InfinityGemSagaGem selectedTile;
    // THE INITIAL LOCATION OF TILES BEFORE BEING PLACED IN THE GRID
    private int unassignedTilesX;
    private int unassignedTilesY;
    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;
    // THE REFERENCE TO THE FILE BEING PLAYED
    private String currentLevel;
    public int stars;
    public int score;
    public int highScore;
    public int moves;
    public int highStar;

    /**
     * Constructor for initializing this data model, it will create the data
     * structures for storing tiles, but not the tile grid itself, that is
     * dependent of file loading, and so should be subsequently initialized.
     *
     * @param initMiniGame The Mahjong game UI.
     */
    public InfinityGemSagaDataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        initialGems = new ArrayList();
        movingGems = new ArrayList();
    }

    /**
     * This method loads the tiles, creating an individual sprite for each. Note
     * that tiles may be of various types, which is important during the tile
     * matching tests.
     */
    public void initTiles()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
        int spriteTypeID = 0;
        SpriteType sT;
        String type = "";
        int indexer = (gridColumns * gridRows) / 6;
        // FIRST THE TYPE A TILES, OF WHICH THERE IS ONLY ONE OF EACH
        // THIS IS ANALOGOUS TO THE SEASON TILES IN FLAVORLESS MAHJONG
        ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_TILES);
        for (int i = 0; i < typeATiles.size(); i++)
        {
            String imgFile = imgPath + typeATiles.get(i);
            sT = initTileSpriteType(imgFile, "" + spriteTypeID);
            switch (i)
            {
                case 0:
                    type = SPACE_TYPE;
                    break;
                case 1:
                    type = TIME_TYPE;
                    break;
                case 2:
                    type = SOUL_TYPE;
                    break;
                case 3:
                    type = POWER_TYPE;
                    break;
                case 4:
                    type = REALITY_TYPE;
                    break;
                case 5:
                    type = MIND_TYPE;
                    break;
                default:
                    type = GEM_TYPE;
                    break;
            }
            for (int j = 0; j < 81; j++)
            {
                initTile(sT, type);
            }
            spriteTypeID++;
        }
    }

    /**
     * Helper method for loading the tiles, it constructs the prescribed tile
     * type using the provided sprite type.
     *
     * @param sT The sprite type to use to represent this tile during rendering.
     *
     * @param tileType The type of tile. Note that there are 3 broad categories.
     */
    private void initTile(SpriteType sT, String tileType)
    {
        // CONSTRUCT THE TILE
        InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, unassignedTilesX, unassignedTilesY, 0, 0, INVISIBLE_STATE, tileType);

        // AND ADD IT TO THE STACK
        initialGems.add(newTile);
    }

    /**
     * Called after a level has been selected, it initializes the grid so that
     * it is the proper dimensions.
     *
     * @param initGrid The grid distribution of tiles, where each cell specifies
     * the number of tiles to be stacked in that cell.
     *
     * @param initGridColumns The columns in the grid for the level selected.
     *
     * @param initGridRows The rows in the grid for the level selected.
     */
    public void initLevelGrid(int[][] initGrid, int initGridColumns, int initGridRows)
    {
        // KEEP ALL THE GRID INFO
        levelGrid = initGrid;
        gridColumns = initGridColumns;
        gridRows = initGridRows;

        // AND BUILD THE TILE GRID FOR STORING THE TILES
        // SINCE WE NOW KNOW ITS DIMENSIONS
        gemGrid = new InfinityGemSagaGem[gridColumns][gridRows];
        SpriteType sT = new SpriteType("");
        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                gemGrid[i][j] = new InfinityGemSagaGem(sT, 0, 0, 0, 0, "", "");
            }
        }

        // MAKE ALL THE TILES VISIBLE
        enableTiles(true);
    }

    /**
     * This helper method initializes a sprite type for a tile or set of similar
     * tiles to be created.
     */
    private SpriteType initTileSpriteType(String imgFile, String spriteTypeID)
    {
        // WE'LL MAKE A NEW SPRITE TYPE FOR EACH GROUP OF SIMILAR LOOKING TILES
        SpriteType sT = new SpriteType(spriteTypeID);
        addSpriteType(sT);

        // LOAD THE ART
        BufferedImage img = miniGame.loadImageWithColorKey(imgFile, COLOR_KEY);
        Image tempImage = img.getScaledInstance(TILE_IMAGE_WIDTH, TILE_IMAGE_HEIGHT, BufferedImage.SCALE_SMOOTH);
        img = new BufferedImage(TILE_IMAGE_WIDTH, TILE_IMAGE_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        img.getGraphics().drawImage(tempImage, 0, 0, null);

        // WE'LL USE THE SAME IMAGE FOR ALL STATES
        sT.addState(INVISIBLE_STATE, img);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(SELECTED_STATE, img);
        sT.addState(INCORRECTLY_SELECTED_STATE, img);

        return sT;
    }

    // ACCESSOR METHODS
    /**
     * Accessor method for getting the level currently being played.
     *
     * @return The level name used currently for the game screen.
     */
    public String getCurrentLevel()
    {
        return currentLevel;
    }

    /**
     * Accessor method for getting the number of tile columns in the game grid.
     *
     * @return The number of columns (left to right) in the grid for the level
     * currently loaded.
     */
    public int getGridColumns()
    {
        return gridColumns;
    }

    /**
     * Accessor method for getting the number of tile rows in the game grid.
     *
     * @return The number of rows (top to bottom) in the grid for the level
     * currently loaded.
     */
    public int getGridRows()
    {
        return gridRows;
    }

    /**
     * Accessor method for getting the tile grid, which has all the tiles the
     * user may select from.
     *
     * @return The main 2D grid of tiles the user selects tiles from.
     */
    public InfinityGemSagaGem[][] getTileGrid()
    {
        return this.gemGrid;
    }

    public ArrayList<InfinityGemSagaGem> getMovingGrid()
    {
        return initialGems;
    }

    /**
     * Accessor method for getting the moving tiles.
     *
     * @return The moving tiles, which are the tiles currently being animated as
     * they move around the game.
     */
    public Iterator<InfinityGemSagaGem> getMovingTiles()
    {
        return movingGems.iterator();
    }

    /**
     * Mutator method for setting the currently loaded level.
     *
     * @param initCurrentLevel The level name currently being used to play the
     * game.
     */
    public void setCurrentLevel(String initCurrentLevel)
    {
        currentLevel = initCurrentLevel;
    }

    public InfinityGemSagaGem Gauntlet(InfinityGemSagaGem gem, int bot)
    {
        int spriteTypeID = 0;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
        SpriteType sT;
        String type = "";
        type = gemGrid[gem.getGridColumn()][bot].getTileType();
        String addon = GEM_GAUNTLET;
        int arrayPos = 0;
        switch (type)
        {
            case SPACE_TYPE:
                arrayPos = 0;
                break;
            case TIME_TYPE:
                arrayPos = 1;
                break;
            case SOUL_TYPE:
                arrayPos = 2;
                break;
            case POWER_TYPE:
                arrayPos = 3;
                break;
            case REALITY_TYPE:
                arrayPos = 4;
                break;
            case MIND_TYPE:
                arrayPos = 5;
                break;
        }

        String typeATiles = props.getProperty(InfinityGemSagaPropertyType.GAME_GAUNTLET_TILES);
        String imgFile = imgPath + typeATiles;
        sT = initTileSpriteType(imgFile, "" + spriteTypeID);
        InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, getColumnPos((int) gem.getTargetX(), (int) gem.getTargetY()),
                getRowPos((int) gem.getTargetX(), (int) gem.getTargetY()), gem.getVx(), gem.getVy(), INVISIBLE_STATE, type);
        newTile.setTarget(gem.getTargetX(), gemGrid[gem.getGridColumn()][bot].getTargetY());
        newTile.setGridCell(gem.getGridColumn(), bot);
        newTile.setAddon(addon);
        movingGems.add(newTile);
        newTile.startMovingToTarget(MAX_TILE_VELOCITY);
        gemGrid[gem.getGridColumn()][bot] = newTile;
        gemGrid[gem.getGridColumn()][gem.getGridRow()].setState(VISIBLE_STATE);
        return newTile;
    }

    public InfinityGemSagaGem wrappedGem(InfinityGemSagaGem gem, int bot)
    {
        int spriteTypeID = 0;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
        SpriteType sT;
        String type = "";
        type = gemGrid[gem.getGridColumn()][bot].getTileType();
        String addon = GEM_WRAPPED;
        int arrayPos = 0;
        switch (type)
        {
            case SPACE_TYPE:
                arrayPos = 0;
                break;
            case TIME_TYPE:
                arrayPos = 1;
                break;
            case SOUL_TYPE:
                arrayPos = 2;
                break;
            case POWER_TYPE:
                arrayPos = 3;
                break;
            case REALITY_TYPE:
                arrayPos = 4;
                break;
            case MIND_TYPE:
                arrayPos = 5;
                break;
        }

        ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_WRAPPED_TILES);
        String imgFile = imgPath + typeATiles.get(arrayPos);
        sT = initTileSpriteType(imgFile, "" + spriteTypeID);
        InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, getColumnPos((int) gem.getTargetX(), (int) gem.getTargetY()),
                getRowPos((int) gem.getTargetX(), (int) gem.getTargetY()), gem.getVx(), gem.getVy(), INVISIBLE_STATE, type);
        newTile.setTarget(gem.getTargetX(), gemGrid[gem.getGridColumn()][bot].getTargetY());
        newTile.setGridCell(gem.getGridColumn(), bot);
        newTile.setAddon(addon);
        movingGems.add(newTile);
        newTile.startMovingToTarget(MAX_TILE_VELOCITY);
        gemGrid[gem.getGridColumn()][bot] = newTile;
        gemGrid[gem.getGridColumn()][gem.getGridRow()].setState(VISIBLE_STATE);
        return newTile;
    }

    public InfinityGemSagaGem stripedHorizontal(InfinityGemSagaGem gem)
    {
        int spriteTypeID = 0;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
        SpriteType sT;
        String type = "";
        type = gemGrid[gem.getGridColumn()][gem.getGridRow()].getTileType();
        String addon = GEM_HORIZONTAL_STRIPE;
        int arrayPos = 0;
        switch (type)
        {
            case SPACE_TYPE:
                arrayPos = 0;
                break;
            case TIME_TYPE:
                arrayPos = 1;
                break;
            case SOUL_TYPE:
                arrayPos = 2;
                break;
            case POWER_TYPE:
                arrayPos = 3;
                break;
            case REALITY_TYPE:
                arrayPos = 4;
                break;
            case MIND_TYPE:
                arrayPos = 5;
                break;
        }

        ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_HORIZONTAL_TILES);
        String imgFile = imgPath + typeATiles.get(arrayPos);
        sT = initTileSpriteType(imgFile, "" + spriteTypeID);
        InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, getColumnPos((int) gem.getTargetX(), (int) gem.getTargetY()),
                getRowPos((int) gem.getTargetX(), (int) gem.getTargetY()), gem.getVx(), gem.getVy(), INVISIBLE_STATE, type);
        newTile.setTarget(gem.getTargetX(), gem.getTargetY());
        newTile.setGridCell(gem.getGridColumn(), gem.getGridRow());
        newTile.setAddon(addon);
        movingGems.add(newTile);
        newTile.startMovingToTarget(MAX_TILE_VELOCITY);
        gemGrid[gem.getGridColumn()][gem.getGridRow()] = newTile;
        gemGrid[gem.getGridColumn()][gem.getGridRow()].setState(VISIBLE_STATE);
        return newTile;
    }

    public InfinityGemSagaGem stripedVertical(InfinityGemSagaGem gem, int bot)
    {
        int spriteTypeID = 0;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
        SpriteType sT;
        String type = "";
        type = gemGrid[gem.getGridColumn()][bot].getTileType();
        String addon = GEM_VERTICAL_STRIPE;
        int arrayPos = 0;
        switch (type)
        {
            case SPACE_TYPE:
                arrayPos = 0;
                break;
            case TIME_TYPE:
                arrayPos = 1;
                break;
            case SOUL_TYPE:
                arrayPos = 2;
                break;
            case POWER_TYPE:
                arrayPos = 3;
                break;
            case REALITY_TYPE:
                arrayPos = 4;
                break;
            case MIND_TYPE:
                arrayPos = 5;
                break;
        }

        ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_VERTICAL_TILES);
        String imgFile = imgPath + typeATiles.get(arrayPos);
        sT = initTileSpriteType(imgFile, "" + spriteTypeID);

        InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, gem.getGridRow(),
                unassignedTilesY, gem.getGridRow(), 0, VISIBLE_STATE, type);
        newTile.setTarget(gem.getTargetX(), gemGrid[gem.getGridColumn()][bot].getTargetY());
        newTile.setGridCell(gem.getGridColumn(), bot);
        newTile.setState(VISIBLE_STATE);
        newTile.setAddon(addon);
        movingGems.add(newTile);
        newTile.startMovingToTarget(MAX_TILE_VELOCITY);
        gemGrid[gem.getGridColumn()][bot] = newTile;

        return newTile;
    }

    public void cheatOne()
    {
        InfinityGemSagaGem[][] temp = new InfinityGemSagaGem[gemGrid.length][];
        for (int p = 0; p < gemGrid.length; p++)
        {
            temp[p] = gemGrid[p].clone();
        }

        float[] tempPosX = new float[9];
        float[] tempPosY = new float[9];


        for (int k = 0; k < 9; k++)
        {
            tempPosX[k] = temp[k][0].getTargetX();

        }

        for (int z = 0; z < 9; z++)
        {
            tempPosY[z] = temp[0][z].getTargetY();
        }

        for (int i = 0; i < 6; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                if (gemGrid[i][j].match(gemGrid[(i + 1)][j]))
                {
                    int spriteTypeID = 0;
                    PropertiesManager props = PropertiesManager.getPropertiesManager();
                    String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
                    SpriteType sT;
                    String type = "";
                    type = gemGrid[i][j].getTileType();
                    int arrayPos = 0;
                    if (type.equals(SPACE_TYPE))
                        arrayPos = 0;
                    else if (type.equals(TIME_TYPE))
                        arrayPos = 1;
                    else if (type.equals(SOUL_TYPE))
                        arrayPos = 2;
                    else if (type.equals(POWER_TYPE))
                        arrayPos = 3;
                    else if (type.equals(REALITY_TYPE))
                        arrayPos = 4;
                    else if (type.equals(MIND_TYPE))
                        arrayPos = 5;

                    ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_TILES);
                    String imgFile = imgPath + typeATiles.get(arrayPos);
                    sT = initTileSpriteType(imgFile, "" + spriteTypeID);

                    InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, tempPosX[(i + 3)], unassignedTilesY,
                            (i + 3), 0, VISIBLE_STATE, type);
                    newTile.setTarget(tempPosX[(i + 3)], tempPosY[j]);
                    newTile.setGridCell((i + 3), j);
                    newTile.setState(VISIBLE_STATE);
                    movingGems.add(newTile);
                    newTile.startMovingToTarget(MAX_TILE_VELOCITY);
                    gemGrid[(i + 3)][j] = newTile;
                }
            }
        }
    }

    public void cheatTwo()
    {
        InfinityGemSagaGem[][] temp = new InfinityGemSagaGem[gemGrid.length][];
        for (int p = 0; p < gemGrid.length; p++)
        {
            temp[p] = gemGrid[p].clone();
        }

        float[] tempPosX = new float[9];
        float[] tempPosY = new float[9];


        for (int k = 0; k < gridColumns; k++)
        {
            tempPosX[k] = temp[k][0].getTargetX();

        }

        for (int z = 0; z < gridRows; z++)
        {
            tempPosY[z] = temp[0][z].getTargetY();
        }

        for (int i = 0; i < gridColumns - 2; i++)
        {
            for (int j = 0; j < gridRows - 4; j++)
            {
                if (gemGrid[i][j].match(gemGrid[i][(j + 1)])
                        && gemGrid[i][j].match(gemGrid[i][(j + 3)]))
                {
                    int spriteTypeID = 0;
                    PropertiesManager props = PropertiesManager.getPropertiesManager();
                    String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
                    SpriteType sT;
                    String type = "";
                    type = gemGrid[i][j].getTileType();
                    int arrayPos = 0;
                    if (type.equals(SPACE_TYPE))
                        arrayPos = 0;
                    else if (type.equals(TIME_TYPE))
                        arrayPos = 1;
                    else if (type.equals(SOUL_TYPE))
                        arrayPos = 2;
                    else if (type.equals(POWER_TYPE))
                        arrayPos = 3;
                    else if (type.equals(REALITY_TYPE))
                        arrayPos = 4;
                    else if (type.equals(MIND_TYPE))
                        arrayPos = 5;

                    ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_TILES);
                    String imgFile = imgPath + typeATiles.get(arrayPos);
                    sT = initTileSpriteType(imgFile, "" + spriteTypeID);

                    InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, tempPosX[(i + 1)], unassignedTilesY,
                            (i + 1), 0, VISIBLE_STATE, type);
                    newTile.setTarget(tempPosX[(i + 1)], tempPosY[(j + 2)]);
                    newTile.setGridCell((i + 1), (j + 2));
                    newTile.setState(VISIBLE_STATE);
                    movingGems.add(newTile);
                    newTile.startMovingToTarget(MAX_TILE_VELOCITY);
                    gemGrid[(i + 1)][(j + 2)] = newTile;
                }
            }
        }

        for (int i = 1; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows - 4; j++)
            {
                if (gemGrid[i][j].match(gemGrid[i][(j + 1)])
                        && gemGrid[i][j].match(gemGrid[i][(j + 3)]))
                {
                    int spriteTypeID = 0;
                    PropertiesManager props = PropertiesManager.getPropertiesManager();
                    String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
                    SpriteType sT;
                    String type = "";
                    type = gemGrid[i][j].getTileType();
                    int arrayPos = 0;
                    if (type.equals(SPACE_TYPE))
                        arrayPos = 0;
                    else if (type.equals(TIME_TYPE))
                        arrayPos = 1;
                    else if (type.equals(SOUL_TYPE))
                        arrayPos = 2;
                    else if (type.equals(POWER_TYPE))
                        arrayPos = 3;
                    else if (type.equals(REALITY_TYPE))
                        arrayPos = 4;
                    else if (type.equals(MIND_TYPE))
                        arrayPos = 5;

                    ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_TILES);
                    String imgFile = imgPath + typeATiles.get(arrayPos);
                    sT = initTileSpriteType(imgFile, "" + spriteTypeID);

                    InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, tempPosX[(i - 1)], unassignedTilesY,
                            (i - 1), 0, VISIBLE_STATE, type);
                    newTile.setTarget(tempPosX[(i - 1)], tempPosY[(j + 2)]);
                    newTile.setGridCell((i - 1), (j + 2));
                    newTile.setState(VISIBLE_STATE);
                    movingGems.add(newTile);
                    newTile.startMovingToTarget(MAX_TILE_VELOCITY);
                    gemGrid[(i - 1)][(j + 2)] = newTile;
                }
            }
        }
    }

    public void cheatThree()
    {
        InfinityGemSagaGem[][] temp = new InfinityGemSagaGem[gemGrid.length][];
        for (int p = 0; p < gemGrid.length; p++)
        {
            temp[p] = gemGrid[p].clone();
        }

        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);

        ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_TILES);
        String imgFile = imgPath + typeATiles.get(3);
        SpriteType sT = initTileSpriteType(imgFile, "" + 50);

        int i = 3;
        for (int j = 0; j < 5; j++)
        {
            gemGrid[i][j] = new InfinityGemSagaGem(sT, gemGrid[i][j].getX(), gemGrid[i][j].getY(),
                    gemGrid[i][j].getVx(), gemGrid[i][j].getVy(), VISIBLE_STATE, POWER_TYPE);
            gemGrid[i][j].setGridCell(i, j);
            gemGrid[i][j].setTarget(temp[i][j].getTargetX(), temp[i][j].getTargetY());
            if (j == 1)
                i--;
            else if (j == 2)
                i++;
        }
    }

    /**
     * This method creates and returns a textual description of the timeInMillis
     * argument as a time duration in the format of (H:MM:SS).
     *
     * @param timeInMillis The time to be represented textually.
     *
     * @return A textual representation of timeInMillis.
     */
    public String timeToText(long timeInMillis)
    {
        // FIRST CALCULATE THE NUMBER OF HOURS,
        // SECONDS, AND MINUTES
        long hours = timeInMillis / MILLIS_IN_AN_HOUR;
        timeInMillis -= hours * MILLIS_IN_AN_HOUR;
        long minutes = timeInMillis / MILLIS_IN_A_MINUTE;
        timeInMillis -= minutes * MILLIS_IN_A_MINUTE;
        long seconds = timeInMillis / MILLIS_IN_A_SECOND;

        // THEN ADD THE TIME OF GAME SUMMARIZED IN PARENTHESES
        String minutesText = "" + minutes;
        if (minutes < 10)
        {
            minutesText = "0" + minutesText;
        }
        String secondsText = "" + seconds;
        if (seconds < 10)
        {
            secondsText = "0" + secondsText;
        }
        return hours + ":" + minutesText + ":" + secondsText;
    }

    /**
     * This method builds and returns a textual representation of the game time.
     * Note that the game may still be in progress.
     *
     * @return The duration of the current game represented textually.
     */
    public String gameTimeToText()
    {
        // CALCULATE GAME TIME USING HOURS : MINUTES : SECONDS
        if ((startTime == null) || (endTime == null))
        {
            return "";
        }
        long timeInMillis = endTime.getTimeInMillis() - startTime.getTimeInMillis();
        return timeToText(timeInMillis);
    }

    /**
     * This method can be used to make all of the tiles either visible (true) or
     * invisible (false). This should be used when switching between the splash
     * and game screens.
     *
     * @param enable Specifies whether the tiles should be made visible or not.
     */
    public void enableTiles(boolean enable)
    {

        for (InfinityGemSagaGem tile : initialGems)
        {
            // AND SET THEM PROPERLY
            if (enable)
            {
                tile.setState(VISIBLE_STATE);
            }
            else
            {
                tile.setState(INVISIBLE_STATE);
            }
        }
    }

    public void dynamicMoves()
    {
        InfinityGemSagaGem[][] temp = new InfinityGemSagaGem[gemGrid.length][];
        for (int i = 0; i < gemGrid.length; i++)
            temp[i] = gemGrid[i].clone();

        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                move(temp[i][j]);
            }
        }
    }

    /**
     * This method examines the current game grid and finds and returns a valid
     * move that is available.
     *
     * @return A move that can be made, or null if none exist.
     */
    public void cleanBoard()
    {

        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                //if (gemGrid[i][j].getBoardNum() != 0)
                move(gemGrid[i][j]);
            }
        }
    }

    public boolean move(InfinityGemSagaGem gem)
    {
        boolean up;
        boolean down;
        boolean left;
        boolean right;

        boolean flag = false;

        int col = gem.getGridColumn();
        int row = gem.getGridRow();

        //These two are for the 5 gem conditional
        //since there is one one way to get a 5 gem
        //I only need to look for it with the initial gem in the middle

        //This one is 5 Gem conditional horizontal
        if ((col - 2) >= 0 && (col + 2) <= gridColumns - 3)
        {
            if ((gemGrid[col][row]).match(gemGrid[(col - 1)][row])
                    && (gemGrid[col][row]).match(gemGrid[(col - 2)][row])
                    && (gemGrid[col][row]).match(gemGrid[(col + 1)][row])
                    && (gemGrid[col][row]).match(gemGrid[(col + 2)][row]))
            {
                flag = true;
                Gauntlet(gem, row);
                dropDownHorizontal(gemGrid[(col - 1)][row]);
                dropDownHorizontal(gemGrid[(col - 2)][row]);
                dropDownHorizontal(gemGrid[(col + 1)][row]);
                dropDownHorizontal(gemGrid[(col + 2)][row]);
                score += 40;
                return flag;
            }
        }

        //5 Gem conditional for the vertical initial gem in middle
        if ((row - 2) >= 0 && (row + 2) <= gridRows - 3)
        {
            if ((gemGrid[col][row]).match(gemGrid[col][(row - 1)])
                    && (gemGrid[col][row]).match(gemGrid[col][(row - 2)])
                    && (gemGrid[col][row]).match(gemGrid[col][(row + 1)])
                    && (gemGrid[col][row]).match(gemGrid[col][(row + 2)]))
            {
                flag = true;
                dropDownVertical(gemGrid[col][(row + 2)], (row + 1));
                dropDownVertical(gemGrid[col][(row + 1)], (row + 1));
                Gauntlet(gem, (row + 2));
                dropDownVertical(gemGrid[col][(row - 1)], (row + 1));
                dropDownVertical(gemGrid[col][(row - 2)], (row + 1));
                score += 40;
                return flag;
            }
        }

        /*
         * L Shaped combos
         */
        if (row < gridRows - 2 && col < gridColumns - 2)
        {
            if (gem.match(gemGrid[col][row + 1]) && gem.match(gemGrid[col][row + 2])
                    && gem.match(gemGrid[col + 1][row]) && gem.match(gemGrid[col + 2][row]))
            {
                flag = true;
                dropDownVertical(gemGrid[col][(row + 2)], (row + 1));
                dropDownVertical(gemGrid[col][(row + 1)], (row + 1));
                wrappedGem(gem, (row + 2));
                dropDownHorizontal(gemGrid[(col + 1)][row + 1]);
                dropDownHorizontal(gemGrid[(col + 2)][row + 2]);
                score += 40;
                return flag;
            }
        }
        if (row <= gridRows - 3 && col >= 2)
        {
            if (gem.match(gemGrid[col][row + 1]) && gem.match(gemGrid[col][row + 2])
                    && gem.match(gemGrid[col - 1][row]) && gem.match(gemGrid[col - 2][row]))
            {
                flag = true;
                dropDownVertical(gemGrid[col - 1][(row + 2)], (row + 1));
                dropDownVertical(gemGrid[col - 2][(row + 1)], (row + 1));
                wrappedGem(gem, (row + 2));
                dropDownHorizontal(gemGrid[col - 1][row]);
                dropDownHorizontal(gemGrid[col - 2][row]);
                score += 40;
                return flag;
            }
        }
        if (row >= 2 && col >= 2)
        {
            if (gem.match(gemGrid[col][row - 1]) && gem.match(gemGrid[col][row - 2])
                    && gem.match(gemGrid[col - 1][row]) && gem.match(gemGrid[col - 2][row]))
            {
                flag = true;
                wrappedGem(gem, row);
                dropDownVertical(gemGrid[col][row - 1], row - 1);
                dropDownVertical(gemGrid[col][row - 2], row - 1);
                dropDownHorizontal(gemGrid[col - 1][row]);
                dropDownHorizontal(gemGrid[col - 2][row]);

                score += 40;
                return flag;
            }
        }
        if (row >= 2 && col <= gridColumns - 2)
        {
            if (gem.match(gemGrid[col][row - 1]) && gem.match(gemGrid[col][row - 2])
                    && gem.match(gemGrid[col + 1][row]) && gem.match(gemGrid[col + 2][row]))
            {
                flag = true;
                wrappedGem(gem, row);
                dropDownVertical(gemGrid[col][row - 1], row - 1);
                dropDownVertical(gemGrid[col][row - 2], row - 1);
                dropDownHorizontal(gemGrid[col + 1][row]);
                dropDownHorizontal(gemGrid[col + 2][row]);
                score += 40;
                return flag;
            }
        }

        /*
         * T Shaped combo
         */
        if ((col >= 1 && col <= gridColumns - 2) && row <= gridRows - 3)
        {
            if (gem.match(gemGrid[col][row + 1]) && gem.match(gemGrid[col][row + 2])
                    && gem.match(gemGrid[col + 1][row]) && gem.match(gemGrid[col - 1][row]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col][row + 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 1]);
                    return flag;
                }
                else if (!(gemGrid[col][row + 2].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 2]);
                }
                else if (!(gemGrid[col - 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col - 1][row]);
                }
                else if (!(gemGrid[col + 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col + 1][row]);
                }

                dropDownVertical(gemGrid[col][row + 2], (row + 1));
                dropDownVertical(gemGrid[col][row + 1], (row + 1));
                wrappedGem(gem, (row + 2));
                dropDownHorizontal(gemGrid[col + 1][row]);
                dropDownHorizontal(gemGrid[col - 1][row]);
                score += 40;
                return flag;

            }
        }

        if ((col >= 1 && col <= gridColumns - 1) && row >= gridRows - 2)
        {
            if (gem.match(gemGrid[col][row - 1]) && gem.match(gemGrid[col][row - 2])
                    && gem.match(gemGrid[col - 1][row]) && gem.match(gemGrid[col + 1][row]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col][row - 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 1]);
                }
                else if (!(gemGrid[col][row - 2].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 2]);
                }
                else if (!(gemGrid[col - 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col - 1][row]);
                }
                else if (!(gemGrid[col + 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col + 1][row]);
                }


                wrappedGem(gem, row);
                dropDownVertical(gemGrid[col][row - 1], row - 1);
                dropDownVertical(gemGrid[col][row - 2], row - 1);
                dropDownHorizontal(gemGrid[col + 1][row]);
                dropDownHorizontal(gemGrid[col - 1][row]);
                score += 40;
                return flag;

            }
        }

        /* 4 gem conditionals can only be done by placing a gem in between
         * 2 and 1 of it's own gem so the only way it could be inserted
         * is as the 2nd gem or 3rd gem, never the first or last gem.
         */

        //4 Gem conditional horizontal with the initial gem being
        //the second from the top
        if ((col - 1) >= 0 && (col + 2) <= gridColumns - 3)
        {
            if ((gemGrid[col][row]).match(gemGrid[(col - 1)][row])
                    && (gemGrid[col][row]).match(gemGrid[(col + 1)][row])
                    && (gemGrid[col][row]).match(gemGrid[(col + 2)][row]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col - 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col - 1][row]);
                    return flag;
                }
                else if (!(gemGrid[col + 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col + 1][row]);
                }
                else if (!(gemGrid[col + 2][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col + 2][row]);
                }

                dropDownHorizontal(gemGrid[(col + 2)][row]);
                dropDownHorizontal(gemGrid[(col + 1)][row]);
                stripedHorizontal(gemGrid[col][row]);
                dropDownHorizontal(gemGrid[(col - 1)][row]);
                score += 30;
                return flag;
            }
        }

        //4 Gem Conditional horizontal with the initial gem being the one
        //second from the bottom
        if ((col + 1) <= gridColumns - 2 && (col - 2) >= 0)
        {
            if ((gemGrid[col][row]).match(gemGrid[(col + 1)][row])
                    && (gemGrid[col][row]).match(gemGrid[(col - 1)][row])
                    && (gemGrid[col][row]).match(gemGrid[(col - 2)][row]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col + 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col + 1][row]);
                }
                else if (!(gemGrid[col - 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col - 1][row]);
                }
                else if (!(gemGrid[col - 2][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col - 2][row]);
                }

                dropDownHorizontal(gemGrid[(col - 2)][row]);
                dropDownHorizontal(gemGrid[(col - 1)][row]);
                stripedHorizontal(gem);
                dropDownHorizontal(gemGrid[(col + 1)][row]);
                score += 30;
                return flag;
            }
        }

        //4 gem conditional vertical with the initial gem being
        //second from the left
        if ((row - 1) >= 0 && (row + 2) <= gridRows - 3)
        {
            if ((gemGrid[col][row]).match(gemGrid[col][(row - 1)])
                    && (gemGrid[col][row]).match(gemGrid[col][(row + 1)])
                    && (gemGrid[col][row]).match(gemGrid[col][(row + 2)]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                    return flag;
                }
                else if (!(gemGrid[col][row - 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 1]);
                }
                else if (!(gemGrid[col][row + 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row + 1]);
                }
                else if (!(gemGrid[col][row + 2].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row + 2]);
                }

                dropDownVertical(gemGrid[col][(row + 2)], (row + 1));
                dropDownVertical(gemGrid[col][(row + 1)], (row + 1));
                stripedVertical(gemGrid[col][row], (row + 2));
                dropDownVertical(gemGrid[col][(row - 1)], (row + 1));

                score += 30;
                return flag;
            }
        }

        //4 gem conditional vertical with the initial gem being
        //second from the right
        if ((row + 1) < gridRows && (row - 2) >= 0)
        {
            if ((gemGrid[col][row]).match(gemGrid[col][(row + 1)])
                    && (gemGrid[col][row]).match(gemGrid[col][(row - 1)])
                    && (gemGrid[col][row]).match(gemGrid[col][(row - 2)]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col][row + 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row + 1]);
                }
                else if (!(gemGrid[col][row - 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 1]);
                }
                else if (!(gemGrid[col][row - 2].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 2]);
                }

                dropDownVertical(gemGrid[col][(row + 1)], (row));
                stripedVertical(gemGrid[col][row], (row + 1));
                dropDownVertical(gemGrid[col][(row - 1)], (row));
                dropDownVertical(gemGrid[col][(row - 2)], (row));
                score += 30;
                return flag;
            }
        }

        //reseting booleans
        up = true;
        down = true;
        left = true;
        right = true;

        // Making sure it cant go out of bounds
        if (col < 2)
            left = false;
        else if (col > gridColumns - 3)
            right = false;
        if (row < 2)
            up = false;
        else if (row > gridRows - 3)
            down = false;

        /**
         * 3 Combo moves
         */
        if (left == true)
        {
            if ((gemGrid[col][row]).match(gemGrid[(col - 1)][row])
                    && (gemGrid[col][row]).match(gemGrid[(col - 2)][row]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col - 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col - 1][row]);
                }
                else if (!(gemGrid[col - 2][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col - 2][row]);
                }

                dropBoardThree(gemGrid[col][row], gemGrid[col - 1][row], gemGrid[col - 2][row]);
                score += 20;
                return flag;
            }
        }
        if (right == true)
        {
            if (gemGrid[col][row].match(gemGrid[col + 1][row])
                    && gemGrid[col][row].match(gemGrid[col + 2][row]))
            {
                flag = true;

                if (!(gemGrid[col + 2][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col + 2][row]);
                }
                else if (!(gemGrid[col + 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col + 1][row]);
                }
                else if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }

                dropBoardThree(gemGrid[col + 2][row], gemGrid[col + 1][row], gemGrid[col][row]);
                score += 20;
                return flag;
            }
        }
        if (up == true)
        {
            if (gemGrid[col][row].match(gemGrid[col][row - 1])
                    && gemGrid[col][row].match(gemGrid[col][row - 2]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col][row - 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 1]);
                }
                else if (!(gemGrid[col][row - 2].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 2]);
                }

                dropBoardThree(gemGrid[col][row], gemGrid[col][row - 1], gemGrid[col][row - 2]);
                score += 20;
                return flag;
            }
        }
        if (down == true)
        {
            if (gemGrid[col][row].match(gemGrid[col][row + 1])
                    && gemGrid[col][row].match(gemGrid[col][row + 2]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col][row + 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row + 1]);
                    return flag;
                }
                else if (!(gemGrid[col][row + 2].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row + 2]);
                }
                dropBoardThree(gemGrid[col][row], gemGrid[col][row + 1], gemGrid[col][row + 2]);
                score += 20;
                return flag;
            }
        }

        up = true;
        down = true;
        left = true;
        right = true;
        if (col < 1)
            left = false;
        else if (col > gridColumns - 2)
            right = false;
        if (row < 1)
            up = false;
        else if (row > gridRows - 2)
            down = false;

        if (up == true && down == true)
        {
            if (gemGrid[col][row].match(gemGrid[col][row + 1])
                    && gemGrid[col][row].match(gemGrid[col][row - 1]))
            {
                flag = true;

                if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col][row + 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row + 1]);
                }
                else if (!(gemGrid[col][row - 1].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row - 1]);
                }

                dropBoardThree(gemGrid[col][row], gemGrid[col][row + 1], gemGrid[col][row - 1]);
                score += 20;
                return flag;
            }
        }
        if (left == true && right == true)
        {
            if (gemGrid[col][row].match(gemGrid[col + 1][row])
                    && gemGrid[col][row].match(gemGrid[col - 1][row]))
            {
                flag = true;

                if (!(gemGrid[col + 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col + 1][row]);
                }
                else if (!(gemGrid[col][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col][row]);
                }
                else if (!(gemGrid[col - 1][row].getAddon().equals(GEM_NONE_TYPE)))
                {
                    dropDownAddon(gemGrid[col - 1][row]);
                }

                dropBoardThree(gemGrid[col + 1][row], gemGrid[col][row], gemGrid[col - 1][row]);
                score += 20;
                return flag;
            }
        }
        return flag;
    }

    public void dropDownAddon(InfinityGemSagaGem gem)
    {
        String addon = gem.getAddon();

        switch (addon)
        {
            case GEM_HORIZONTAL_STRIPE:
            {
                for (int i = 0; i < getGridRows(); i++)
                    dropDownHorizontal(gemGrid[i][gem.getGridRow()]);
                break;
            }
            case GEM_VERTICAL_STRIPE:
            {
                for (int i = getGridRows() - 1; i >= 0; i--)
                    dropDownVertical(gemGrid[gem.getGridColumn()][i], getGridRows() - 1);
                break;
            }
            case GEM_WRAPPED:
            {
                int col = gem.getGridColumn();
                int row = gem.getGridRow();


                if (col < 1 && row < 1)
                {
                    for (int i = row; i < row + 1; i++)
                    {
                        for (int j = col; j < col + 1; j++)
                        {
                            dropDownHorizontal(gemGrid[j][i]);
                        }
                    }
                }
                else if (col > gridColumns - 2 && row > gridRows - 2)
                {
                    for (int i = row - 1; i <= row; i++)
                    {
                        for (int j = col - 1; j <= col; j++)
                        {
                            dropDownHorizontal(gemGrid[j][i]);
                        }
                    }
                }
                else if (col < 1)
                {
                    for (int i = row - 1; i <= row + 1; i++)
                    {
                        for (int j = col; j <= col + 1; j++)
                        {
                            dropDownHorizontal(gemGrid[j][i]);
                        }
                    }
                }
                else if (row < 1)
                {
                    for (int i = row; i <= row + 1; i++)
                    {
                        for (int j = col - 1; j <= col + 1; j++)
                        {
                            dropDownHorizontal(gemGrid[j][i]);
                        }
                    }
                }
                else if (col > gridColumns - 1)
                {
                    for (int i = row - 1; i <= row + 1; i++)
                    {
                        for (int j = col - 1; j <= col; j++)
                        {
                            dropDownHorizontal(gemGrid[j][i]);
                        }
                    }
                }
                else if (row > gridRows - 1)
                {
                    for (int i = row - 1; i <= row; i++)
                    {
                        for (int j = col - 1; j <= col + 1; j++)
                        {
                            dropDownHorizontal(gemGrid[j][i]);
                        }
                    }
                }
                else
                {
                    for (int i = row - 1; i < row + 2; i++)
                    {
                        for (int j = col - 1; j < col + 2; j++)
                        {
                            dropDownHorizontal(gemGrid[j][i]);
                        }
                    }
                }
                break;
            }
            case GEM_GAUNTLET:
            {


                for (int i = 0; i < gridColumns; i++)
                {
                    for (int j = 0; j < gridRows; j++)
                    {
                        if (gem.match(gemGrid[i][j]))
                        {
                            dropDownHorizontal(gemGrid[i][j]);
                        }
                    }
                }
                gem.setAddon(GEM_NONE_TYPE);
                dropDownHorizontal(gem);
            }
            break;

        }

    }

    public void dropDownVertical(InfinityGemSagaGem gem, int length)
    {
        InfinityGemSagaGem[][] temp = new InfinityGemSagaGem[gemGrid.length][];
        for (int i = 0; i < gemGrid.length; i++)
        {
            temp[i] = gemGrid[i].clone();
        }

        float[][] tempPosX = new float[gridColumns][gridRows];
        float[][] tempPosY = new float[gridColumns][gridRows];


        for (int k = 0; k < gridColumns; k++)
        {
            for (int p = 0; p < gridRows; p++)
            {
                tempPosY[k][p] = temp[k][p].getTargetY();
                tempPosX[k][p] = temp[k][p].getTargetX();
            }
        }

        float gemTargetX = 0;
        float gemTargetY = 0;
        int gemX = gem.getGridColumn();
        int gemY = gem.getGridRow();
        int i = gemX;
        for (int j = gemY; j >= 0; j--)
        {
            if (gem.getBoardNum() == 0)
            {
            }
            else if (j == 0)
            {
                int spriteTypeID = 0;
                PropertiesManager props = PropertiesManager.getPropertiesManager();
                String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
                SpriteType sT;
                String type = "";
                ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_TILES);
                Random generator = new Random();
                int z = generator.nextInt(6);
                String imgFile = imgPath + typeATiles.get(z);
                sT = initTileSpriteType(imgFile, "" + spriteTypeID);
                switch (z)
                {
                    case 0:
                        type = SPACE_TYPE;
                        break;
                    case 1:
                        type = TIME_TYPE;
                        break;
                    case 2:
                        type = SOUL_TYPE;
                        break;
                    case 3:
                        type = POWER_TYPE;
                        break;
                    case 4:
                        type = REALITY_TYPE;
                        break;
                    case 5:
                        type = MIND_TYPE;
                        break;
                    default:
                        type = GEM_TYPE;
                        break;
                }

                InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, tempPosX[i][0], unassignedTilesY,
                        i, 0, VISIBLE_STATE, type);
                newTile.setTarget(tempPosX[i][0], tempPosY[i][0]);
                newTile.setGridCell(i, 0);
                newTile.setState(VISIBLE_STATE);
                movingGems.add(newTile);
                newTile.startMovingToTarget(STANDARD_TILE_VELOCITY);
                gemGrid[i][0] = newTile;

            }
            else
            {

                gemTargetX = tempPosX[i][j];
                gemTargetY = tempPosY[i][j];
                InfinityGemSagaGem tile2 = temp[i][j - 1];
                tile2.setTarget(gemTargetX, gemTargetY);
                tile2.setGridCell(i, j);
                movingGems.add(tile2);
                tile2.startMovingToTarget(STANDARD_TILE_VELOCITY);
                gemGrid[i][j] = tile2;
            }
        }

    }

    public void dropDownHorizontal(InfinityGemSagaGem gem)
    {

        InfinityGemSagaGem[][] temp = new InfinityGemSagaGem[gemGrid.length][];
        for (int i = 0; i < gemGrid.length; i++)
        {
            temp[i] = gemGrid[i].clone();
        }

        float[][] tempPosX = new float[gridColumns][gridRows];
        float[][] tempPosY = new float[gridColumns][gridRows];


        for (int k = 0; k < gridColumns; k++)
        {
            for (int p = 0; p < gridRows; p++)
            {
                tempPosY[k][p] = temp[k][p].getTargetY();
                tempPosX[k][p] = temp[k][p].getTargetX();
            }
        }

        float gemTargetX = 0;
        float gemTargetY = 0;
        int gemX = gem.getGridColumn();
        int gemY = gem.getGridRow();
        int i = gemX;
        for (int j = gemY; j >= 0; j--)
        {
            if (gem.getBoardNum() == 0)
            {
                return;
            }
            else if (j == 0)
            {

                int spriteTypeID = 0;
                PropertiesManager props = PropertiesManager.getPropertiesManager();
                String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
                SpriteType sT;
                String type = "";
                ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_TILES);
                Random generator = new Random();
                int z = generator.nextInt(6);
                String imgFile = imgPath + typeATiles.get(z);
                sT = initTileSpriteType(imgFile, "" + spriteTypeID);
                switch (z)
                {
                    case 0:
                        type = SPACE_TYPE;
                        break;
                    case 1:
                        type = TIME_TYPE;
                        break;
                    case 2:
                        type = SOUL_TYPE;
                        break;
                    case 3:
                        type = POWER_TYPE;
                        break;
                    case 4:
                        type = REALITY_TYPE;
                        break;
                    case 5:
                        type = MIND_TYPE;
                        break;
                    default:
                        type = GEM_TYPE;
                        break;
                }

                InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, tempPosX[i][0], unassignedTilesY,
                        i, 0, VISIBLE_STATE, type);
                newTile.setTarget(tempPosX[i][0], tempPosY[i][0]);
                newTile.setGridCell(i, 0);
                newTile.setState(VISIBLE_STATE);
                movingGems.add(newTile);
                newTile.startMovingToTarget(STANDARD_TILE_VELOCITY);
                gemGrid[i][0] = newTile;

            }
            else
            {

                gemTargetX = tempPosX[i][j];
                gemTargetY = tempPosY[i][j];
                int index = j;
                while (j >= 1 && temp[i][j - 1].getBoardNum() == 0)
                {
                    j--;
                }
                if (j == 0)
                {
                    int spriteTypeID = 0;
                    PropertiesManager props = PropertiesManager.getPropertiesManager();
                    String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
                    SpriteType sT;
                    String type = "";
                    ArrayList<String> typeATiles = props.getPropertyOptionsList(InfinityGemSagaPropertyType.GAME_TILES);
                    Random generator = new Random();
                    int z = generator.nextInt(6);
                    String imgFile = imgPath + typeATiles.get(z);
                    sT = initTileSpriteType(imgFile, "" + spriteTypeID);
                    switch (z)
                    {
                        case 0:
                            type = SPACE_TYPE;
                            break;
                        case 1:
                            type = TIME_TYPE;
                            break;
                        case 2:
                            type = SOUL_TYPE;
                            break;
                        case 3:
                            type = POWER_TYPE;
                            break;
                        case 4:
                            type = REALITY_TYPE;
                            break;
                        case 5:
                            type = MIND_TYPE;
                            break;
                        default:
                            type = GEM_TYPE;
                            break;
                    }

                    InfinityGemSagaGem newTile = new InfinityGemSagaGem(sT, tempPosX[i][index], tempPosY[i][index],
                            i, index, VISIBLE_STATE, type);
                    newTile.setTarget(tempPosX[i][index], tempPosY[i][index]);
                    newTile.setGridCell(i, index);
                    newTile.setState(VISIBLE_STATE);
                    movingGems.add(newTile);
                    newTile.startMovingToTarget(STANDARD_TILE_VELOCITY);
                    gemGrid[i][index] = newTile;
                }
                else
                {
                    InfinityGemSagaGem tile2 = temp[i][j - 1];
                    tile2.setState(VISIBLE_STATE);
                    tile2.setTarget(gemTargetX, gemTargetY);
                    tile2.setGridCell(i, j);
                    movingGems.add(tile2);
                    tile2.startMovingToTarget(STANDARD_TILE_VELOCITY);
                    gemGrid[i][j] = tile2;
                }
            }
        }

    }

    public void dropBoardThree(InfinityGemSagaGem one, InfinityGemSagaGem two, InfinityGemSagaGem three)
    {
        if (one.getGridRow() == two.getGridRow())
        {
            dropDownHorizontal(one);
            dropDownHorizontal(two);
            dropDownHorizontal(three);
        }
        else
        {
            if (one.getGridRow() < two.getGridRow() && one.getGridRow() < three.getGridRow())
            {
                dropDownVertical(one, one.getGridRow());
                if (two.getGridRow() < three.getGridRow())
                    dropDownVertical(two, one.getGridRow());
                else
                    dropDownVertical(three, one.getGridRow());
            }
            else if (two.getGridRow() < three.getGridRow())
            {
                dropDownVertical(two, two.getGridRow());
                if (one.getGridColumn() < three.getGridRow())
                {
                    dropDownVertical(one, two.getGridRow());
                    dropDownVertical(three, two.getGridRow());
                }
                else
                {
                    dropDownVertical(three, two.getGridRow());
                    dropDownVertical(one, two.getGridRow());
                }
            }
            else
            {
                dropDownVertical(three, three.getGridRow());
                if (one.getGridRow() < two.getGridRow())
                {
                    dropDownVertical(one, three.getGridRow());
                    dropDownVertical(two, three.getGridRow());
                }
                else
                {
                    dropDownVertical(two, three.getGridRow());
                    dropDownVertical(one, three.getGridRow());
                }
            }


        }
    }

    /**
     * This method updates all the necessary state information to process the
     * move argument.
     *
     * @param move The move to make. Note that a move specifies the cell
     * locations for a match.
     */
    public void processMove(InfinityGemSagaMove move)
    {

        InfinityGemSagaGem[][] temp = gemGrid.clone();
        for (int i = 0; i < gridColumns; i++)
        {
            temp[i] = gemGrid[i].clone();
        }


        gemGrid[move.col1][move.row1] = gemGrid[move.col2][move.row2];
        gemGrid[move.col2][move.row2] = temp[move.col1][move.row1];

        InfinityGemSagaGem tile1 = gemGrid[move.col1][move.row1];
        InfinityGemSagaGem tile2 = gemGrid[move.col2][move.row2];

        float tempX = tile1.getX();
        float tempY = tile1.getY();
        int posX = tile1.getGridColumn();
        int posY = tile1.getGridRow();

        tile1.setTarget(tile2.getX(), tile2.getY());
        tile1.startMovingToTarget(STANDARD_TILE_VELOCITY);


        tile2.setTarget(tempX, tempY);
        tile2.startMovingToTarget(STANDARD_TILE_VELOCITY);


        movingGems.add(tile1);
        movingGems.add(tile2);


        tile1.setGridCell(tile2.getGridColumn(), tile2.getGridRow());
        tile2.setGridCell(posX, posY);

        boolean flag = move(tile1);
        boolean flagTwo = move(tile2);

        if (!flag && !flagTwo)
        {
            while (!movingGems.isEmpty())
            {
                updateAll(miniGame);
            }

            temp = gemGrid.clone();
            for (int i = 0; i < gridColumns; i++)
            {
                temp[i] = gemGrid[i].clone();
            }


            gemGrid[move.col1][move.row1] = gemGrid[move.col2][move.row2];
            gemGrid[move.col2][move.row2] = temp[move.col1][move.row1];

            tile1 = gemGrid[move.col1][move.row1];
            tile2 = gemGrid[move.col2][move.row2];

            tempX = tile1.getX();
            tempY = tile1.getY();
            posX = tile1.getGridColumn();
            posY = tile1.getGridRow();

            tile1.setTarget(tile2.getX(), tile2.getY());
            tile1.startMovingToTarget(STANDARD_TILE_VELOCITY);

            tile2.setTarget(tempX, tempY);
            tile2.startMovingToTarget(STANDARD_TILE_VELOCITY);


            movingGems.add(tile1);
            movingGems.add(tile2);


            tile1.setGridCell(tile2.getGridColumn(), tile2.getGridRow());
            tile2.setGridCell(posX, posY);

            moves++;
        }

        selectedTile.setState(VISIBLE_STATE);

        selectedTile = null;
        moves--;

        if (score > 4000)
            endGameAsWin();


        if (moves == 50)
            endGameAsLoss();

        dynamicMoves();

    }

    /**
     * This method attempts to select the selectTile argument. Note that this
     * may be the move or second selected tile. If a tile is already selected,
     * it will attempt to process a match/move.
     *
     * @param selectTile The tile to select.
     */
    public void selectTile(InfinityGemSagaGem selectTile)
    {


        // IF IT'S ALREADY THE SELECTED TILE, DESELECT IT
        if (selectTile == selectedTile)
        {
            selectedTile = null;
            selectTile.setState(VISIBLE_STATE);
        }
        else if (selectedTile == null)
        {
            selectedTile = selectTile;
            selectedTile.setState(SELECTED_STATE);
        }
        else if (!(selectedTile.getAddon().equals(GEM_NONE_TYPE)) && !(selectTile.getAddon().equals(GEM_NONE_TYPE)))
        {
            dropDownAddon(selectTile);
            dropDownAddon(selectedTile);
        }
        else if (selectedTile.getAddon().equals(GEM_GAUNTLET))
        {
            selectedTile.setTileType(selectTile.getTileType());
            dropDownAddon(selectedTile);
        }
        else
        {
            int selectedCol = selectedTile.getGridColumn();
            int selectedRow = selectedTile.getGridRow();
            int selectCol = selectTile.getGridColumn();
            int selectRow = selectTile.getGridRow();

            if (selectedRow == selectRow && selectedCol == (selectCol - 1))
            {
                InfinityGemSagaMove mov = new InfinityGemSagaMove();
                mov.col1 = selectedTile.getGridColumn();
                mov.col2 = selectTile.getGridColumn();
                mov.row1 = selectedTile.getGridRow();
                mov.row2 = selectTile.getGridRow();
                processMove(mov);
            }
            else if (selectedRow == (selectRow + 1) && selectedCol == selectCol)
            {
                InfinityGemSagaMove mov = new InfinityGemSagaMove();
                mov.col1 = selectedTile.getGridColumn();
                mov.col2 = selectTile.getGridColumn();
                mov.row1 = selectedTile.getGridRow();
                mov.row2 = selectTile.getGridRow();
                processMove(mov);
            }
            else if (selectedRow == selectRow && selectedCol == (selectCol + 1))
            {
                InfinityGemSagaMove mov = new InfinityGemSagaMove();
                mov.col1 = selectedTile.getGridColumn();
                mov.col2 = selectTile.getGridColumn();
                mov.row1 = selectedTile.getGridRow();
                mov.row2 = selectTile.getGridRow();
                processMove(mov);
            }
            else if (selectedRow == (selectRow - 1) && selectedCol == selectCol)
            {
                InfinityGemSagaMove mov = new InfinityGemSagaMove();
                mov.col1 = selectedTile.getGridColumn();
                mov.col2 = selectTile.getGridColumn();
                mov.row1 = selectedTile.getGridRow();
                mov.row2 = selectTile.getGridRow();
                processMove(mov);
            }
            else
            {
                selectedTile.setState(VISIBLE_STATE);
                selectedTile = selectTile;
                selectedTile.setState(SELECTED_STATE);
            }
        }
    }

    public int getRowPos(int x, int y)
    {

        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                if ((int) gemGrid[i][j].getX() + 50 >= x && (int) gemGrid[i][j].getX() <= x
                        && (int) gemGrid[i][j].getY() + 55 >= y && (int) gemGrid[i][j].getY() <= y)
                {
                    return i;
                }

            }
        }
        return -1;
    }

    public int getColumnPos(int x, int y)
    {

        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                if ((int) gemGrid[i][j].getX() + 50 >= x && (int) gemGrid[i][j].getX() <= x
                        && (int) gemGrid[i][j].getY() + 55 >= y && (int) gemGrid[i][j].getY() <= y)
                {
                    return j;
                }

            }
        }
        return -1;
    }

    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on grid cells.
     *
     * @param game The Mahjong game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y)
    {
        // FIGURE OUT THE CELL IN THE GRID
        int col = getRowPos(x, y);
        int row = getColumnPos(x, y);

        if (col == -1 || row == -1)
        {
        }
        else
        {
            InfinityGemSagaGem testTile = gemGrid[col][row];
            selectTile(testTile);
        }
    }

    public void checkDragPressOnSprites(int x, int y)
    {
        // FIGURE OUT THE CELL IN THE GRID
        int col = getRowPos(x, y);
        int row = getColumnPos(x, y);

        if (col == -1 || row == -1)
        {
        }
        else
        {
            InfinityGemSagaGem testTile = gemGrid[col][row];
            selectTile(testTile);
        }
    }

    /**
     * Called when the game is won, it will record the ending game time, update
     * the player record, display the win dialog, and play the win animation.
     */
    @Override
    public void endGameAsWin()
    {
        // UPDATE THE GAME STATE USING THE INHERITED FUNCTIONALITY
        super.endGameAsWin();


        // RECORD IT AS A WIN
        if (score > highScore)
            highScore = score;

        if (moves > 25)
            stars = 5;
        else if (moves > 20)
            stars = 4;
        else if (moves > 15)
            stars = 3;
        else if (moves > 10)
            stars = 2;
        else
            stars = 1;

        if (stars > highStar)
            highStar = stars;

        ((InfinityGemSagaMiniGame) miniGame).displayStatScreen();
    }

    @Override
    public void endGameAsLoss()
    {
        super.endGameAsLoss();

        //long gameTime = endTime.getTimeInMillis() - startTime.getTimeInMillis();

        // RECORD IT AS A LOSE
        ((InfinityGemSagaMiniGame) miniGame).getPlayerRecord().addLoss(currentLevel);
        ((InfinityGemSagaMiniGame) miniGame).savePlayerRecord();

        // DISPLAY THE LOSE DIALOG

        if (score > highScore)
            highScore = score;


    }

    /**
     * Called when a game is started, the game grid is reset.
     *
     * @param game
     */
    @Override
    public void reset(MiniGame game)
    {
        if (initialGems.size() < 150)
            initTiles();

        for (InfinityGemSagaGem tile : initialGems)
        {
            tile.setX(0);
            tile.setY(0);
            tile.setState(VISIBLE_STATE);
        }

        // RANDOMLY ORDER THEM
        Collections.shuffle(initialGems);

        // START THE CLOCK
        startTime = new GregorianCalendar();

        moves = 30;

        score = 0;
        // NOW LET'S REMOVE THEM FROM THE STACK
        // AND PUT THE TILES IN THE GRID
        int x = 400;
        int y = 100;

        for (int i = 0; i < gridColumns; i++)
        {
            for (int j = 0; j < gridRows; j++)
            {
                // TAKE THE TILE OUT OF THE STACK
                InfinityGemSagaGem tile = initialGems.remove(initialGems.size() - 1);
                // PUT IT IN THE GRID
                int bNum = levelGrid[i][j];
                if (bNum == 0)
                {
                    gemGrid[i][j].setBoardNum(bNum);
                }
                else
                {
                    gemGrid[i][j] = tile;
                    gemGrid[i][j].setBoardNum(bNum);
                    tile.setGridCell(i, j);
                    tile.setTarget(x, y);
                    tile.startMovingToTarget(MAX_TILE_VELOCITY);
                    movingGems.add(tile);

                }
                y += 55;

            }
            x += 50;
            y = 100;
        }
        // AND START ALL UPDATES
        cleanBoard();
        beginGame();
    }

    /**
     * Called each frame, this method updates all the game objects.
     *
     * @param game The Mahjong game to be updated.
     */
    @Override
    public void updateAll(MiniGame game)
    {
        // MAKE SURE THIS THREAD HAS EXCLUSIVE ACCESS TO THE DATA
        try
        {
            game.beginUsingData();


            // WE ONLY NEED TO UPDATE AND MOVE THE MOVING TILES
            for (int i = 0; i < movingGems.size(); i++)
            {
                // GET THE NEXT TILE
                InfinityGemSagaGem tile = movingGems.get(i);

                // THIS WILL UPDATE IT'S POSITION USING ITS VELOCITY
                tile.update(game);
                // IF IT'S REACHED ITS DESTINATION, REMOVE IT
                // FROM THE LIST OF MOVING TILES
                if (!tile.isMovingToTarget())
                {
                    movingGems.remove(tile);
                }
            }

            // IF THE GAME IS STILL ON, THE TIMER SHOULD CONTINUE
            if (inProgress())
            {
                // KEEP THE GAME TIMER GOING IF THE GAME STILL IS
                endTime = new GregorianCalendar();
            }

        }
        finally
        {
            // MAKE SURE WE RELEASE THE LOCK WHETHER THERE IS
            // AN EXCEPTION THROWN OR NOT
            game.endUsingData();
        }
    }

    /**
     * This method is for updating any debug text to present to the screen. In a
     * graphical application like this it's sometimes useful to display data in
     * the GUI.
     *
     * @param game The Mahjong game about which to display info.
     */
    @Override
    public void updateDebugText(MiniGame game)
    {
    }
}