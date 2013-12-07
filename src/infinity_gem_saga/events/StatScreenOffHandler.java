/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author Michael Knoth
 */
package infinity_gem_saga.events;

import static infinity_gem_saga.InfinityGemSagaConstants.STATS_DIALOG_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.STAT_PLAY_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.STAT_QUIT_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.VISIBLE_STATE;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StatScreenOffHandler implements ActionListener
{
    private InfinityGemSagaMiniGame miniGame;

    public StatScreenOffHandler(InfinityGemSagaMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
       miniGame.statScreenOff();
    }
}