package infinity_gem_saga.events;

import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Mike Knoth
 */
public class ResetGameHandler implements ActionListener{
    
    private InfinityGemSagaMiniGame game;

    public ResetGameHandler(InfinityGemSagaMiniGame initGame)
    {
        game = initGame;
    }
    
    @Override
    public void actionPerformed(ActionEvent ae)
    {
    }
    
    
    
}
