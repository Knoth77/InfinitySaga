package infinity_gem_saga.events;

import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LevelButtonHandler implements ActionListener
{
    private InfinityGemSagaMiniGame miniGame;

    public LevelButtonHandler(InfinityGemSagaMiniGame initMiniGame)
    {
        miniGame = initMiniGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        // DISPLAY THE STATS
        miniGame.displayStatScreen();
    }
}