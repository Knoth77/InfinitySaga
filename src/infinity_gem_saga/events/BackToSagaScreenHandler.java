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
public class BackToSagaScreenHandler implements ActionListener
{

    private InfinityGemSagaMiniGame miniGame;

    public BackToSagaScreenHandler(InfinityGemSagaMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        if (miniGame.getDataModel().inProgress())
        {
            miniGame.getDataModel().endGameAsLoss();
        }
        miniGame.switchToSagaScreen();
    }
}
