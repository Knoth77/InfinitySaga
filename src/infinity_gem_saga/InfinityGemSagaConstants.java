package infinity_gem_saga;

import java.awt.Color;
import java.awt.Font;
/**
 * @author Michael Knoth
 */
public class InfinityGemSagaConstants
{
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    public static final String STATS_DIALOG_TYPE = "STATS_DIALOG_TYPE";
    public static final String WIN_DIALOG_TYPE = "WIN_DIALOG_TYPE";
    
    public static final String SAGA_SCREEN_BUTTON_TYPE = "PLAY_BUTTON_TYPE";
    public static final String SAGA_SCREEN_RESET_BUTTON_TYPE = "RESET_BUTTON_TYPE";
    public static final String QUIT_BUTTON_TYPE = "QUIT_BUTTON_TYPE";

    public static final String UP_BUTTON_TYPE = "UP_BUTTON_TYPE";
    public static final String DOWN_BUTTON_TYPE = "DOWN_BUTTON_TYPE";
    
    public static final String STAT_PLAY_TYPE = "STAT_PLAY_TYPE";
    public static final String STAT_QUIT_TYPE = "STAT_QUIT_TYPE";
    
    public static final String MENU_TYPE = "MENU_TYPE";
    public static final String LEVEL_BACK_TYPE = "LEVEL_BACK_TYPE";
        
    public static final String SPACE_TYPE = "SPACE_TYPE";
    public static final String REALITY_TYPE = "REALITY_TYPE";
    public static final String POWER_TYPE = "POWER_TYPE";
    public static final String MIND_TYPE = "MIND_TYPE";
    public static final String TIME_TYPE = "TIME_TYPE";
    public static final String SOUL_TYPE = "SOUL_TYPE";
    public static final String GEM_TYPE = "GEM_TYPE";
    
    public static final String GEM_NONE_TYPE = "None";
    public static final String GEM_HORIZONTAL_STRIPE = "GEM_HORIZONTAL_STRIPE";
    public static final String GEM_VERTICAL_STRIPE = "GEM_VERTICAL_STRIPE";
    public static final String GEM_WRAPPED = "GEM_WRAPPED";
    public static final String GEM_GAUNTLET = "GEM_GAUNTLET";
    
    
    public static final String TILE_SPRITE_TYPE_PREFIX = "SPACE_";
    
    public static final String BAXTER_TYPE = "BAXTER_TYPE";
    public static final String HELICARRIER_TYPE = "HELICARRIER_TYPE";
    public static final String ATLANTIS_TYPE = "ATLANTIS_TYPE";
    public static final String GENOSHA_TYPE = "GENOSHA_TYPE";
    
    // WE'LL USE THESE STATES TO CONTROL SWITCHING BETWEEN THE TWO
    public static final String SPLASH_SCREEN_STATE = "SPLASH_SCREEN_STATE";
    public static final String SAGA_SCREEN_STATE = "SAGA_SCREEN_STATE";  
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";  

    public static final String LEVEL_SELECT_BUTTON_TYPE = "LEVEL_SELECT_BUTTON_TYPE";
    
    public static final String INVISIBLE_STATE = "INVISIBLE_STATE";
    public static final String VISIBLE_STATE = "VISIBLE_STATE";
    public static final String SELECTED_STATE = "SELECTED_STATE";
    public static final String INCORRECTLY_SELECTED_STATE = "NOT_AVAILABLE_STATE";
    public static final String MOUSE_OVER_STATE = "MOUSE_OVER_STATE";
            

    public static final int OPTION_BUTTON_WIDTH = 200;
    public static final int OPTION_BUTTON_MARGIN = 5;
    public static final int OPTION_BUTTON_Y = 570;

    public static final int TILE_IMAGE_OFFSET = 1;
    public static final int TILE_IMAGE_WIDTH = 55;
    public static final int TILE_IMAGE_HEIGHT = 55;
    public static final int Z_TILE_OFFSET = 5;

    public static final int MAX_TILE_VELOCITY = 75;
    public static final int STANDARD_TILE_VELOCITY = 5;
    
    public static final int UP_X = 1215;
    public static final int UP_Y = 325;
    
    public static final int DOWN_X = 0;
    public static final int DOWN_Y = 325;
       
    public static final long MILLIS_IN_A_SECOND = 1000;
    public static final long MILLIS_IN_A_MINUTE = 1000 * 60;
    public static final long MILLIS_IN_AN_HOUR  = 1000 * 60 * 60;

    public static final Color COLOR_KEY = new Color(255, 174, 201);
    public static final Color DEBUG_TEXT_COLOR = Color.BLACK;
    public static final Color TEXT_DISPLAY_COLOR = new Color (10, 160, 10);
    public static final Color SELECTED_TILE_COLOR = new Color(255,255,0,100);
    public static final Color INCORRECTLY_SELECTED_TILE_COLOR = new Color(255, 50, 50, 100);
    public static final Color STATS_COLOR = new Color(0, 60, 0);

    public static final Font TEXT_DISPLAY_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 48);
    public static final Font DEBUG_TEXT_FONT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font STATS_FONT = new Font(Font.MONOSPACED, Font.BOLD, 24);

    public static final String SUCCESS_AUDIO_TYPE = "SUCCESS_AUDIO_TYPE";
    public static final String FAILURE_AUDIO_TYPE = "FAILURE_AUDIO_TYPE";
    public static final String THEME_SONG_TYPE = "THEME_SONG_TYPE";
    
    // UI CONTROLS POSITIONS IN THE GAME SCREEN
    public static final int CONTROLS_MARGIN = 0;
    public static final int NEW_BUTTON_X = 0;
    public static final int NEW_BUTTON_Y = 0;
    public static final int BACK_BUTTON_X = NEW_BUTTON_X + 130 + CONTROLS_MARGIN;
    public static final int BACK_BUTTON_Y = 0;
    public static final int TILES_BUTTON_X = BACK_BUTTON_X + 130 + CONTROLS_MARGIN;
    public static final int TILES_BUTTON_Y = 0;
    public static final int TILES_TEXT_OFFSET = 60;
    public static final int TILES_OFFSET = 150;
    public static final int TIME_X = TILES_BUTTON_X + 230 + CONTROLS_MARGIN;
    public static final int TIME_Y = 0;
    public static final int TIME_OFFSET = 130;
    public static final int TIME_TEXT_OFFSET = 55;
    public static final int STATS_X = TIME_X + 310 + CONTROLS_MARGIN;
    public static final int STATS_Y = 0;
    public static final int UNDO_X = STATS_X + 160 + CONTROLS_MARGIN;
    public static final int UNDO_Y = 0;
    public static final int TILE_STACK_X = UNDO_X + 130 + CONTROLS_MARGIN;
    public static final int TILE_STACK_Y = 0;
    public static final int TILE_STACK_OFFSET_X = 30;
    public static final int TILE_STACK_OFFSET_Y = 12;
    public static final int TILE_STACK_2_OFFSET_X = 105;
}