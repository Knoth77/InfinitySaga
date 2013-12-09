/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity_gem_saga.events;

import infinity_gem_saga.data.InfinityGemSagaDataModel;
import infinity_gem_saga.data.InfinityGemSagaMove;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author Milk
 */
public class InfinityKeyHandler extends KeyAdapter
{
    // THE MAHJONG GAME ON WHICH WE'LL RESPOND
    private InfinityGemSagaMiniGame game;

    /**
     * This constructor simply inits the object by 
     * keeping the game for later.
     * 
     * @param initGame The Mahjong game that contains
     * the back button.
     */    
    public InfinityKeyHandler(InfinityGemSagaMiniGame initGame)
    {
        game = initGame;
    }
    
    /**
     * This method provides a custom game response to when the user
     * presses a keyboard key.
     * 
     * @param ke Event object containing information about the event,
     * like which key was pressed.
     */
    @Override
    public void keyPressed(KeyEvent ke)
    {
        // CHEAT BY ONE MOVE. NOTE THAT IF WE HOLD THE C
        // KEY DOWN IT WILL CONTINUALLY CHEAT
        if (ke.getKeyCode() == KeyEvent.VK_1)
        {
            InfinityGemSagaDataModel data = (InfinityGemSagaDataModel)game.getDataModel();
            
            // FIND A MOVE IF THERE IS ONE
            data.cheatOne();
        }
        else if (ke.getKeyCode() == KeyEvent.VK_2)
        {
            InfinityGemSagaDataModel data = (InfinityGemSagaDataModel)game.getDataModel();
            
            data.cheatTwo();
        }
        else if (ke.getKeyCode() == KeyEvent.VK_3)
        {
            InfinityGemSagaDataModel data = (InfinityGemSagaDataModel)game.getDataModel();
            
            data.cheatThree();
        }
        else if (ke.getKeyCode() == KeyEvent.VK_4)
        {
            InfinityGemSagaDataModel data = (InfinityGemSagaDataModel)game.getDataModel();
            
            data.cheatFour();
        }
        else if (ke.getKeyCode() == KeyEvent.VK_5)
        {
            InfinityGemSagaDataModel data = (InfinityGemSagaDataModel)game.getDataModel();
            
            data.cheatFive();
        }
        else if(ke.getKeyCode() == KeyEvent.VK_W)
        {
            InfinityGemSagaDataModel data = (InfinityGemSagaDataModel)game.getDataModel();
            data.endGameAsWin();
        }
        else if(ke.getKeyCode() == KeyEvent.VK_L)
        {
            InfinityGemSagaDataModel data = (InfinityGemSagaDataModel)game.getDataModel();
            data.endGameAsLoss();
        }
        
    }
}