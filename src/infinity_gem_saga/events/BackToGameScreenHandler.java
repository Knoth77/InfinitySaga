/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity_gem_saga.events;

import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Milk
 */
public class BackToGameScreenHandler implements ActionListener
{
    private InfinityGemSagaMiniGame miniGame;

    public BackToGameScreenHandler(InfinityGemSagaMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
       miniGame.switchToSagaScreen();
    }
}
