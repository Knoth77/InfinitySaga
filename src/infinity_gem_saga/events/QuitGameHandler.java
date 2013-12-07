package infinity_gem_saga.events;

import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Mike Knoth
 */
public class QuitGameHandler implements ActionListener
{
    private InfinityGemSagaMiniGame miniGame;
    
    public QuitGameHandler(InfinityGemSagaMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {

        System.exit(0);
    }
}