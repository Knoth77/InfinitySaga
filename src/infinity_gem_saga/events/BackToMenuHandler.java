/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Michael Knoth
 */
package infinity_gem_saga.events;

import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Milk
 */
public class BackToMenuHandler implements ActionListener
{
    private InfinityGemSagaMiniGame miniGame;

    public BackToMenuHandler(InfinityGemSagaMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
       System.exit(0);
    }
}