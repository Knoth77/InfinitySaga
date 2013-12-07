package infinity_gem_saga;

import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import infinity_gem_saga.ui.InfinityGemSagaErrorHandler;
import xml_utilities.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;

/**
 * @author Michael Knoth
 */
public class InfinityGemSaga
{
    static InfinityGemSagaMiniGame miniGame = new InfinityGemSagaMiniGame();
    
    static String PROPERTY_TYPES_LIST = "property_types.txt";
    static String UI_PROPERTIES_FILE_NAME = "properties.xml";
    static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    static String DATA_PATH = "./data/";

    public static void main(String[] args)
    {
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(InfinityGemSagaPropertyType.UI_PROPERTIES_FILE_NAME, UI_PROPERTIES_FILE_NAME);
            props.addProperty(InfinityGemSagaPropertyType.PROPERTIES_SCHEMA_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            props.addProperty(InfinityGemSagaPropertyType.DATA_PATH.toString(), DATA_PATH);
            props.loadProperties(UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            
            String gameFlavorFile = props.getProperty(InfinityGemSagaPropertyType.GAME_FLAVOR_FILE_NAME);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);
                               
            String appTitle = props.getProperty(InfinityGemSagaPropertyType.GAME_TITLE_TEXT);
            int fps = Integer.parseInt(props.getProperty(InfinityGemSagaPropertyType.FPS));
            miniGame.initMiniGame(appTitle, fps);
            miniGame.startGame();
        }
        catch(InvalidXMLFileFormatException ixmlffe)
        {
            // LET THE ERROR HANDLER PROVIDE THE RESPONSE
            InfinityGemSagaErrorHandler errorHandler = miniGame.getErrorHandler();
            errorHandler.processError(InfinityGemSagaPropertyType.INVALID_XML_FILE_ERROR_TEXT);
        }
    }
    
    public enum InfinityGemSagaPropertyType
    {
        UI_PROPERTIES_FILE_NAME,
        PROPERTIES_SCHEMA_FILE_NAME,
        GAME_FLAVOR_FILE_NAME,
        RECORD_FILE_NAME,

        AUDIO_PATH,
        DATA_PATH,
        IMG_PATH,
        
        WINDOW_WIDTH,
        WINDOW_HEIGHT,
        FPS,
        GAME_WIDTH,
        GAME_HEIGHT,
        GAME_LEFT_OFFSET,
        GAME_TOP_OFFSET,
        
        GAME_TITLE_TEXT,
        EXIT_REQUEST_TEXT,
        INVALID_XML_FILE_ERROR_TEXT,
        ERROR_DIALOG_TITLE_TEXT,
        
        AUDIO_FILE_ERROR,
        LOAD_LEVEL_ERROR,
        RECORD_SAVE_ERROR,

        WINDOW_ICON,
        SPLASH_SCREEN_IMAGE_NAME,
        GAME_BACKGROUND_IMAGE_NAME,
        STAT_SCREEN_IMAGE_NAME,
        SAGA_SCREEN_IMAGE_NAME,
        
        
        BAXTER_IMAGE_NAME,
        HELICARRIER_IMAGE_NAME,
        ATLANTIS_IMAGE_NAME,
        GENOSHA_IMAGE_NAME,
        
        UP_BUTTON_IMAGE_NAME,
        DOWN_BUTTON_IMAGE_NAME,
        
        PLAY_OPTION,
        PLAY_IMAGE_BUTTON,
        PLAY_MOUSE_OVER_IMAGE_BUTTON,
        RESET_BUTTON,
        RESET_IMAGE_BUTTON,
        RESET_MOUSE_OVER_IMAGE_BUTTON,
        QUIT_BUTTON,
        QUIT_IMAGE_BUTTON,
        QUIT_MOUSE_OVER_IMAGE_BUTTON,
        MENU_BUTTON,
        MENU_IMAGE_BUTTON,
        MENU_MOUSE_OVER_IMAGE_BUTTON,
        LEVEL_BACK_IMAGE_BUTTON,
        LEVEL_BACK_MOUSE_OVER_IMAGE_BUTTON,
        
        STAT_PLAY,
        STAT_PLAY_IMAGE_BUTTON,
        STAT_PLAY_MOUSE_OVER_IMAGE_BUTTON,
        STAT_QUIT,
        STAT_QUIT_IMAGE_BUTTON,
        STAT_QUIT_MOUSE_OVER_IMAGE_BUTTON,
        
        STATS_DIALOG_IMAGE_NAME,
        WIN_DIALOG_IMAGE_NAME,
        
        GAME_TILES,
        GAME_HORIZONTAL_TILES,
        GAME_VERTICAL_TILES,
        GAME_WRAPPED_TILES,
        GAME_GAUNTLET_TILES,
        
        LEVEL_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        
        SELECT_AUDIO_CUE,
        MATCH_AUDIO_CUE,
        NO_MATCH_AUDIO_CUE,
        BLOCKED_TILE_AUDIO_CUE,
        UNDO_AUDIO_CUE,
        WIN_AUDIO_CUE,
        LOSE_AUDIO_CUE,
        SPLASH_SCREEN_SONG_CUE,
        GAMEPLAY_SONG_CUE
    }
}