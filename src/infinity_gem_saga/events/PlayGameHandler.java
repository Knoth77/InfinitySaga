package infinity_gem_saga.events;

import infinity_gem_saga.data.InfinityGemSagaDataModel;
import infinity_gem_saga.file.InfinityGemSagaFileManager;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Mike Knoth
 */
public class PlayGameHandler implements ActionListener
{

    private InfinityGemSagaMiniGame game;

    public PlayGameHandler(InfinityGemSagaMiniGame initGame)
    {
        game = initGame;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        
            // GO TO THE GAME
            game.switchToSagaScreen();
    }
}
