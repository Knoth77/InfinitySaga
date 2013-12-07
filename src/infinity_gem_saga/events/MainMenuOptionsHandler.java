package infinity_gem_saga.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import static infinity_gem_saga.InfinityGemSagaConstants.*;

/**
 * This event handler responds to when the user selects a Mahjong level to play
 * on the splash screen.
 *
 * @author Richard McKenna
 */
public class MainMenuOptionsHandler implements ActionListener {
    // HERE'S THE GAME WE'LL UPDATE

    private InfinityGemSagaMiniGame game;
    // HERE'S THE LEVEL TO LOAD
    private String levelFile;

    /**
     * This constructor just stores the game and the level to load for later.
     *
     * @param initGame The game to update.
     *
     * @param initLevelFile The level to load when the user requests it.
     */
    public MainMenuOptionsHandler(InfinityGemSagaMiniGame initGame) {
        game = initGame;
    }

    /**
     * Here is the event response. This code is executed when the user clicks on
     * a button for selecting a level which is how the user starts a game. Note
     * that the game data is already locked for this thread before it is called,
     * and that it will be unlocked after it returns.
     *
     * @param ae the event object for the button press
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        // WE ONLY LET THIS HAPPEN IF THE SPLASH SCREEN IS VISIBLE
        if (game.isCurrentScreenState(SPLASH_SCREEN_STATE)) {
            // GO TO THE GAME
            game.switchToSagaScreen();
        }
    }
}