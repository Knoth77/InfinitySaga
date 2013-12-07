package infinity_gem_saga.ui;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import mini_game.MiniGame;
import static infinity_gem_saga.InfinityGemSagaConstants.*;
import mini_game.Sprite;
import mini_game.SpriteType;
import properties_manager.PropertiesManager;
import infinity_gem_saga.InfinityGemSaga.InfinityGemSagaPropertyType;
import infinity_gem_saga.data.InfinityGemSagaDataModel;
import infinity_gem_saga.data.InfinityGemSagaRecord;
import infinity_gem_saga.events.BackToGameScreenHandler;
import infinity_gem_saga.events.StatScreenOffHandler;
import infinity_gem_saga.events.BackToMenuHandler;
import infinity_gem_saga.events.GemHandler;
import infinity_gem_saga.events.InfinityKeyHandler;
import infinity_gem_saga.events.LeftArrowHandler;
import infinity_gem_saga.events.LevelButtonHandler;
import infinity_gem_saga.events.RightArrowHandler;
import infinity_gem_saga.events.PlayGameHandler;
import infinity_gem_saga.events.PlayLevelHandler;
import infinity_gem_saga.events.QuitGameHandler;
import infinity_gem_saga.events.ResetGameHandler;
import infinity_gem_saga.file.InfinityGemSagaFileManager;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * This is the actual mini game, as extended from the mini game framework. It
 * manages all the UI elements.
 *
 * @author Michael Knoth
 */
public class InfinityGemSagaMiniGame extends MiniGame
{
    // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION

    private InfinityGemSagaRecord record;
    // HANDLES ERROR CONDITIONS
    private InfinityGemSagaErrorHandler errorHandler;
    private InfinityGemSagaFileManager fileManager;
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    public String currentImage;

    // private MouseListener mouseL;
    // ACCESSOR METHODS
    // - getPlayerRecord
    // - getErrorHandler
    // - getFileManager
    // - isCurrentScreenState
    /**
     * Accessor method for getting the player record object, which summarizes
     * the player's record on all optionsList.
     *
     * @return The player's complete record.
     */
    public InfinityGemSagaRecord getPlayerRecord()
    {
        return record;
    }

    public InfinityGemSagaFileManager getFileManager()
    {
        return fileManager;
    }

    public String getCurrentScreenState()
    {
        return currentScreenState;
    }

    public void savePlayerRecord()
    {
        fileManager.saveRecord();
    }

    /**
     * Accessor method for getting the application's error handler.
     *
     * @return The error handler.
     */
    public InfinityGemSagaErrorHandler getErrorHandler()
    {
        return errorHandler;
    }

    public boolean isCurrentScreenState(String testScreenState)
    {
        return testScreenState.equals(currentScreenState);
    }

    public void switchToSagaScreen()
    {

        PropertiesManager props = PropertiesManager.getPropertiesManager();


        currentImage = BACKGROUND_TYPE;
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SAGA_SCREEN_STATE);


        guiButtons.get(UP_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(true);

        guiButtons.get(SAGA_SCREEN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SAGA_SCREEN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SAGA_SCREEN_RESET_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SAGA_SCREEN_RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(false);


        guiDecor.get(BAXTER_TYPE).setState(VISIBLE_STATE);
        guiDecor.get(HELICARRIER_TYPE).setState(VISIBLE_STATE);
        guiDecor.get(ATLANTIS_TYPE).setState(VISIBLE_STATE);
        guiDecor.get(GENOSHA_TYPE).setState(VISIBLE_STATE);


        guiButtons.get(STAT_PLAY_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(STAT_PLAY_TYPE).setEnabled(false);
        guiButtons.get(STAT_QUIT_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(STAT_QUIT_TYPE).setEnabled(false);

        guiButtons.get(MENU_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(MENU_TYPE).setEnabled(true);

        guiButtons.get(LEVEL_BACK_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_BACK_TYPE).setEnabled(false);

        ArrayList<String> levels = props.getPropertyOptionsList(InfinityGemSagaPropertyType.LEVEL_OPTIONS);
        for (String level : levels)
        {
            guiButtons.get(level).setState(VISIBLE_STATE);
            guiButtons.get(level).setEnabled(true);
        }

        ((InfinityGemSagaDataModel) data).enableTiles(false);

        currentScreenState = SAGA_SCREEN_STATE;
    }

    public void switchToSplashScreen()
    {

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SPLASH_SCREEN_STATE);

        guiButtons.get(UP_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);

        guiDecor.get(BAXTER_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(HELICARRIER_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(ATLANTIS_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(GENOSHA_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SAGA_SCREEN_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SAGA_SCREEN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SAGA_SCREEN_RESET_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(SAGA_SCREEN_RESET_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(true);

        guiDecor.get(STATS_DIALOG_TYPE).setState(INVISIBLE_STATE);

        guiButtons.get(STAT_PLAY_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(STAT_PLAY_TYPE).setEnabled(false);
        guiButtons.get(STAT_QUIT_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(STAT_QUIT_TYPE).setEnabled(false);
        guiButtons.get(MENU_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(MENU_TYPE).setEnabled(false);

        guiButtons.get(LEVEL_BACK_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(LEVEL_BACK_TYPE).setEnabled(false);

        ArrayList<String> levels = props.getPropertyOptionsList(InfinityGemSagaPropertyType.LEVEL_OPTIONS);
        for (String level : levels)
        {
            guiButtons.get(level).setState(INVISIBLE_STATE);
            guiButtons.get(level).setEnabled(false);
        }

        ((InfinityGemSagaDataModel) data).enableTiles(false);

        // AND CHANGE THE SCREENS STATE
        currentScreenState = SPLASH_SCREEN_STATE;
    }

    public void switchToGameScreen()
    {

        PropertiesManager props = PropertiesManager.getPropertiesManager();

        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);

        guiButtons.get(LEVEL_BACK_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(LEVEL_BACK_TYPE).setEnabled(true);

        guiButtons.get(UP_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);


        guiButtons.get(SAGA_SCREEN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SAGA_SCREEN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SAGA_SCREEN_RESET_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(SAGA_SCREEN_RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(false);


        guiDecor.get(BAXTER_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(HELICARRIER_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(ATLANTIS_TYPE).setState(INVISIBLE_STATE);
        guiDecor.get(GENOSHA_TYPE).setState(INVISIBLE_STATE);


        guiButtons.get(STAT_PLAY_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(STAT_PLAY_TYPE).setEnabled(false);
        guiButtons.get(STAT_QUIT_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(STAT_QUIT_TYPE).setEnabled(false);
        guiDecor.get(STATS_DIALOG_TYPE).setState(INVISIBLE_STATE);

        guiButtons.get(MENU_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(MENU_TYPE).setEnabled(false);

        ArrayList<String> levels = props.getPropertyOptionsList(InfinityGemSagaPropertyType.LEVEL_OPTIONS);
        for (String level : levels)
        {
            guiButtons.get(level).setState(INVISIBLE_STATE);
            guiButtons.get(level).setEnabled(false);
        }

        ((InfinityGemSagaDataModel) data).enableTiles(true);
        data.reset(this);

        currentScreenState = GAME_SCREEN_STATE;
    }

    @Override
    public void initData()
    {
        // INIT OUR ERROR HANDLER
        errorHandler = new InfinityGemSagaErrorHandler(window);

        fileManager = new InfinityGemSagaFileManager(this);

        data = new InfinityGemSagaDataModel(this);

        record = fileManager.loadRecord();



        // LOAD THE GAME DIMENSIONS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        int gameWidth = Integer.parseInt(props.getProperty(InfinityGemSagaPropertyType.GAME_WIDTH.toString()));
        int gameHeight = Integer.parseInt(props.getProperty(InfinityGemSagaPropertyType.GAME_HEIGHT.toString()));
        data.setGameDimensions(gameWidth, gameHeight);


        // THIS WILL CHANGE WHEN WE LOAD A LEVEL
        boundaryLeft = Integer.parseInt(props.getProperty(InfinityGemSagaPropertyType.GAME_LEFT_OFFSET.toString()));
        boundaryTop = Integer.parseInt(props.getProperty(InfinityGemSagaPropertyType.GAME_TOP_OFFSET.toString()));
        boundaryRight = gameWidth - boundaryLeft;
        boundaryBottom = gameHeight;
        //
    }

    /**
     * Initializes the game controls, like buttons, used by the game
     * application. Note that this includes the tiles, which serve as buttons of
     * sorts.
     */
    @Override
    public void initGUIControls()
    {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;

        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(InfinityGemSagaPropertyType.IMG_PATH);
        String windowIconFile = props.getProperty(InfinityGemSagaPropertyType.WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING

        canvas = new InfinityGemSagaPanel(this, (InfinityGemSagaDataModel) data);

        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = SPLASH_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(InfinityGemSagaPropertyType.SPLASH_SCREEN_IMAGE_NAME));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(SPLASH_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(InfinityGemSagaPropertyType.GAME_BACKGROUND_IMAGE_NAME));
        sT.addState(GAME_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(InfinityGemSagaPropertyType.SAGA_SCREEN_IMAGE_NAME));
        sT.addState(SAGA_SCREEN_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, SPLASH_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);

        float totalWidth = 3 * (OPTION_BUTTON_WIDTH + OPTION_BUTTON_MARGIN) - OPTION_BUTTON_MARGIN;
        float gameWidth = Integer.parseInt(props.getProperty(InfinityGemSagaPropertyType.GAME_WIDTH));

        x = (gameWidth - totalWidth) / 2.0f;
        y = OPTION_BUTTON_Y;

        String playButton = props.getProperty(InfinityGemSagaPropertyType.PLAY_IMAGE_BUTTON);
        sT = new SpriteType(SAGA_SCREEN_BUTTON_TYPE);
        img = loadImage(imgPath + playButton);
        sT.addState(VISIBLE_STATE, img);
        String playMouseOverButton = props.getProperty(InfinityGemSagaPropertyType.PLAY_MOUSE_OVER_IMAGE_BUTTON);
        img = loadImage(imgPath + playMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, x, y, 0, 0, VISIBLE_STATE);
        guiButtons.put(SAGA_SCREEN_BUTTON_TYPE, s);

        x += OPTION_BUTTON_WIDTH + OPTION_BUTTON_MARGIN;

        String resetButton = props.getProperty(InfinityGemSagaPropertyType.RESET_IMAGE_BUTTON);
        sT = new SpriteType(SAGA_SCREEN_RESET_BUTTON_TYPE);
        img = loadImage(imgPath + resetButton);
        sT.addState(VISIBLE_STATE, img);
        String resetMouseOverButton = props.getProperty(InfinityGemSagaPropertyType.RESET_MOUSE_OVER_IMAGE_BUTTON);
        img = loadImage(imgPath + resetMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, x, y, 0, 0, VISIBLE_STATE);
        guiButtons.put(SAGA_SCREEN_RESET_BUTTON_TYPE, s);

        x += OPTION_BUTTON_WIDTH + OPTION_BUTTON_MARGIN;

        String quitButton = props.getProperty(InfinityGemSagaPropertyType.QUIT_IMAGE_BUTTON);
        sT = new SpriteType(QUIT_BUTTON_TYPE);
        img = loadImage(imgPath + quitButton);
        sT.addState(VISIBLE_STATE, img);
        String quitMouseOverButton = props.getProperty(InfinityGemSagaPropertyType.QUIT_MOUSE_OVER_IMAGE_BUTTON);
        img = loadImage(imgPath + quitMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, x, y, 0, 0, VISIBLE_STATE);
        guiButtons.put(QUIT_BUTTON_TYPE, s);


        String arrowUp = props.getProperty(InfinityGemSagaPropertyType.UP_BUTTON_IMAGE_NAME);
        sT = new SpriteType(UP_BUTTON_TYPE);
        img = loadImage(imgPath + arrowUp);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, UP_X, UP_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(UP_BUTTON_TYPE, s);

        String arrowDown = props.getProperty(InfinityGemSagaPropertyType.DOWN_BUTTON_IMAGE_NAME);
        sT = new SpriteType(DOWN_BUTTON_TYPE);
        img = loadImage(imgPath + arrowDown);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, DOWN_X, DOWN_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(DOWN_BUTTON_TYPE, s);

        /*String wings = props.getProperty(InfinityGemSagaPropertyType.IRONMAN_BUTTON_IMAGE_NAME);
         sT = new SpriteType(IRONMAN_BUTTON_TYPE);
         img = loadImage(imgPath + wings);
         sT.addState(VISIBLE_STATE, img);
         sT.addState(MOUSE_OVER_STATE, img);
         s = new Sprite(sT, 33, 27, 0, 0, INVISIBLE_STATE);
         guiButtons.put(IRONMAN_BUTTON_TYPE, s);*/

        ArrayList<String> levels = props.getPropertyOptionsList(InfinityGemSagaPropertyType.LEVEL_OPTIONS);
        ArrayList<String> levelImageNames = props.getPropertyOptionsList(InfinityGemSagaPropertyType.LEVEL_IMAGE_OPTIONS);

        int levelx = 33;
        int levely = 27;
        
        for (int i = 0; i < levels.size(); i++)
        {
            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
            sT.addState(VISIBLE_STATE, img);
            img = loadImageWithColorKey(imgPath + levelImageNames.get(i), COLOR_KEY);
            sT.addState(MOUSE_OVER_STATE, img);
            s = new Sprite(sT, levelx, levely, 0, 0, INVISIBLE_STATE);
            guiButtons.put(levels.get(i), s);
            levelx+=50;
            levely+=50;
            
        }


        String secondImg = props.getProperty(InfinityGemSagaPropertyType.BAXTER_IMAGE_NAME);
        sT = new SpriteType(BAXTER_TYPE);
        img = loadImage(imgPath + secondImg);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, 1280, 0, 0, 0, INVISIBLE_STATE);
        guiDecor.put(BAXTER_TYPE, s);

        String thirdImg = props.getProperty(InfinityGemSagaPropertyType.HELICARRIER_IMAGE_NAME);
        sT = new SpriteType(HELICARRIER_TYPE);
        img = loadImage(imgPath + thirdImg);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, 2560, 0, 0, 0, INVISIBLE_STATE);
        guiDecor.put(HELICARRIER_TYPE, s);

        String fourthImg = props.getProperty(InfinityGemSagaPropertyType.ATLANTIS_IMAGE_NAME);
        sT = new SpriteType(ATLANTIS_TYPE);
        img = loadImage(imgPath + fourthImg);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, 3840, 0, 0, 0, INVISIBLE_STATE);
        guiDecor.put(ATLANTIS_TYPE, s);

        String fifthImg = props.getProperty(InfinityGemSagaPropertyType.GENOSHA_IMAGE_NAME);
        sT = new SpriteType(GENOSHA_TYPE);
        img = loadImage(imgPath + fifthImg);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, 5120, 0, 0, 0, INVISIBLE_STATE);
        guiDecor.put(GENOSHA_TYPE, s);


        String statPlayButton = props.getProperty(InfinityGemSagaPropertyType.STAT_PLAY_IMAGE_BUTTON);
        sT = new SpriteType(STAT_PLAY_TYPE);
        img = loadImage(imgPath + statPlayButton);
        sT.addState(VISIBLE_STATE, img);
        String statPlayMouseOverButton = props.getProperty(InfinityGemSagaPropertyType.STAT_PLAY_MOUSE_OVER_IMAGE_BUTTON);
        img = loadImage(imgPath + statPlayMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, 450, 450, 0, 0, INVISIBLE_STATE);
        guiButtons.put(STAT_PLAY_TYPE, s);



        String statQuitButton = props.getProperty(InfinityGemSagaPropertyType.STAT_QUIT_IMAGE_BUTTON);
        sT = new SpriteType(STAT_QUIT_TYPE);
        img = loadImage(imgPath + statQuitButton);
        sT.addState(VISIBLE_STATE, img);
        String statQuitMouseOverButton = props.getProperty(InfinityGemSagaPropertyType.STAT_QUIT_MOUSE_OVER_IMAGE_BUTTON);
        img = loadImage(imgPath + statQuitMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, 600, 450, 0, 0, INVISIBLE_STATE);
        guiButtons.put(STAT_QUIT_TYPE, s);




        String statsDialog = props.getProperty(InfinityGemSagaPropertyType.STATS_DIALOG_IMAGE_NAME);
        sT = new SpriteType(STATS_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + statsDialog, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(STATS_DIALOG_TYPE, s);

        String winDialog = props.getProperty(InfinityGemSagaPropertyType.WIN_DIALOG_IMAGE_NAME);
        sT = new SpriteType(WIN_DIALOG_TYPE);
        img = loadImageWithColorKey(imgPath + winDialog, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        x = (data.getGameWidth() / 2) - (img.getWidth(null) / 2);
        y = (data.getGameHeight() / 2) - (img.getHeight(null) / 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE_STATE);
        guiDecor.put(WIN_DIALOG_TYPE, s);

        String menuButton = props.getProperty(InfinityGemSagaPropertyType.MENU_IMAGE_BUTTON);
        sT = new SpriteType(MENU_TYPE);
        img = loadImage(imgPath + menuButton);
        sT.addState(VISIBLE_STATE, img);
        String menuMouseOverButton = props.getProperty(InfinityGemSagaPropertyType.MENU_MOUSE_OVER_IMAGE_BUTTON);
        img = loadImage(imgPath + menuMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, 1000, 600, 0, 0, INVISIBLE_STATE);
        guiButtons.put(MENU_TYPE, s);

        String levelBackButton = props.getProperty(InfinityGemSagaPropertyType.LEVEL_BACK_IMAGE_BUTTON);
        sT = new SpriteType(LEVEL_BACK_TYPE);
        img = loadImage(imgPath + levelBackButton);
        sT.addState(VISIBLE_STATE, img);
        String levelBackMouseOverButton = props.getProperty(InfinityGemSagaPropertyType.LEVEL_BACK_MOUSE_OVER_IMAGE_BUTTON);
        img = loadImage(imgPath + levelBackMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, 1000, 600, 0, 0, INVISIBLE_STATE);
        guiButtons.put(LEVEL_BACK_TYPE, s);



        ((InfinityGemSagaDataModel) data).initTiles();
    }

    /**
     * Initializes the game event handlers for things like game gui buttons.
     */
    @Override
    public void initGUIHandlers()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String dataPath = props.getProperty(InfinityGemSagaPropertyType.DATA_PATH);


        ArrayList<String> levels = props.getPropertyOptionsList(InfinityGemSagaPropertyType.LEVEL_OPTIONS);
        for (String levelFile : levels)
        {
            PlayLevelHandler plh = new PlayLevelHandler(this, dataPath + levelFile);
            guiButtons.get(levelFile).setActionListener(plh);
        }

        PlayGameHandler pgh = new PlayGameHandler(this);
        guiButtons.get(SAGA_SCREEN_BUTTON_TYPE).setActionListener(pgh);


        QuitGameHandler qh = new QuitGameHandler(this);
        guiButtons.get(QUIT_BUTTON_TYPE).setActionListener(qh);

        ResetGameHandler rgh = new ResetGameHandler(this);
        guiButtons.get(SAGA_SCREEN_RESET_BUTTON_TYPE).setActionListener(rgh);

        // NEW GAME EVENT HANDLER
        RightArrowHandler auh = new RightArrowHandler(this);
        guiButtons.get(UP_BUTTON_TYPE).setActionListener(auh);

        LeftArrowHandler adh = new LeftArrowHandler(this);
        guiButtons.get(DOWN_BUTTON_TYPE).setActionListener(adh);


        StatScreenOffHandler ssoh = new StatScreenOffHandler(this);
        guiButtons.get(STAT_QUIT_TYPE).setActionListener(ssoh);

        BackToMenuHandler btmch = new BackToMenuHandler(this);
        guiButtons.get(MENU_TYPE).setActionListener(btmch);

        //PlayLevelHandler plh = new PlayLevelHandler(this);
        //guiButtons.get(STAT_PLAY_TYPE).setActionListener(plh);

        BackToGameScreenHandler btgsch = new BackToGameScreenHandler(this);
        guiButtons.get(LEVEL_BACK_TYPE).setActionListener(btgsch);

        InfinityKeyHandler ikh = new InfinityKeyHandler(this);
        this.setKeyListener(ikh);

    }

    public void removeButtons()
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        if (currentImage.equals(BACKGROUND_TYPE))
        {
            ArrayList<String> levels = props.getPropertyOptionsList(InfinityGemSagaPropertyType.LEVEL_OPTIONS);
            for (String level : levels)
            {
                guiButtons.get(level).setState(VISIBLE_STATE);
                guiButtons.get(level).setEnabled(true);
            }
        }
        else
        {
            ArrayList<String> levels = props.getPropertyOptionsList(InfinityGemSagaPropertyType.LEVEL_OPTIONS);
            for (String level : levels)
            {
                guiButtons.get(level).setState(INVISIBLE_STATE);
                guiButtons.get(level).setEnabled(false);
            }
        }
    }

    /**
     * Invoked when a new game is started, it resets all relevant game data and
     * gui control states.
     */
    @Override
    public void reset()
    {
        data.reset(this);
    }

    /**
     * Updates the state of all gui controls according to the current game
     * conditions.
     */
    @Override
    public void updateGUI()
    {
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();

            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(VISIBLE_STATE))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(MOUSE_OVER_STATE);
                }
            } // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(MOUSE_OVER_STATE))
            {
                if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(VISIBLE_STATE);
                }
            }
        }
    }

    public void displayStatScreen()
    {
        guiButtons.get(STAT_PLAY_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(STAT_QUIT_TYPE).setState(VISIBLE_STATE);
        guiButtons.get(STAT_PLAY_TYPE).setEnabled(true);
        guiButtons.get(STAT_QUIT_TYPE).setEnabled(true);
        guiDecor.get(STATS_DIALOG_TYPE).setState(VISIBLE_STATE);

    }

    public void statScreenOff()
    {
        guiButtons.get(STAT_PLAY_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(STAT_QUIT_TYPE).setState(INVISIBLE_STATE);
        guiButtons.get(STAT_PLAY_TYPE).setEnabled(false);
        guiButtons.get(STAT_QUIT_TYPE).setEnabled(false);
        guiDecor.get(STATS_DIALOG_TYPE).setState(INVISIBLE_STATE);
    }

    /* public boolean isMoving()
     {
     System.out.println(guiDecor.get(BACKGROUND_TYPE).getX());
        
     if((int)(guiDecor.get(BACKGROUND_TYPE).getX()) != 0 || (int)(guiDecor.get(BACKGROUND_TYPE).getX()) != -1280 || 
     (int)(guiDecor.get(BACKGROUND_TYPE).getX()) != -2560 || (int)(guiDecor.get(BACKGROUND_TYPE).getX()) != -3840)
     { 
     System.out.println(guiDecor.get(BACKGROUND_TYPE).getX());
     return true;
     }    
     return false;
     }*/
    @Override
    public void initAudioContent()
    {
    }
}