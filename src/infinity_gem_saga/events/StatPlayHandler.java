/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity_gem_saga.events;

import infinity_gem_saga.data.InfinityGemSagaDataModel;
import infinity_gem_saga.file.InfinityGemSagaFileManager;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static infinity_gem_saga.InfinityGemSagaConstants.*;

/**
 *
 * @author Milk
 */
public class StatPlayHandler implements ActionListener
{

    private InfinityGemSagaMiniGame game;
    private String levelFile;

    public StatPlayHandler(InfinityGemSagaMiniGame initGame)
    {
        game = initGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        
        game.statScreenOff();

        if (game.isCurrentScreenState(SAGA_SCREEN_STATE))
            game.switchToGameScreen();

    }
}