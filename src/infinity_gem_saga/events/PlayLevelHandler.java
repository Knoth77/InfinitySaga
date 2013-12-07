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
public class PlayLevelHandler implements ActionListener
{
    
    private InfinityGemSagaMiniGame game;
    private String levelFile;
    
    public PlayLevelHandler(InfinityGemSagaMiniGame initGame, String initLevelFile)
    {
        game = initGame;
        levelFile = initLevelFile;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        
        InfinityGemSagaDataModel data = (InfinityGemSagaDataModel) game.getDataModel();

        // UPDATE THE DATA
        InfinityGemSagaFileManager fileManager = game.getFileManager();
        fileManager.loadLevel(levelFile);
        if (game.isCurrentScreenState(SAGA_SCREEN_STATE))
            game.switchToGameScreen();
        
    }
}