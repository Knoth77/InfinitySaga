package infinity_gem_saga.events;

import static infinity_gem_saga.InfinityGemSagaConstants.ATLANTIS_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.BACKGROUND_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.BAXTER_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.GENOSHA_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.HELICARRIER_TYPE;
import infinity_gem_saga.timer.InfinityGemSagaTimer;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Mike Knoth
 */
public class LeftArrowHandler implements ActionListener {
    
    private InfinityGemSagaMiniGame game;
    private InfinityGemSagaTimer time;
    

    public LeftArrowHandler(InfinityGemSagaMiniGame initGame)
    {
        game = initGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae)
    {
        switch (game.currentImage) {
            case GENOSHA_TYPE:
                game.currentImage = ATLANTIS_TYPE;
                break;
            case ATLANTIS_TYPE:
                game.currentImage = HELICARRIER_TYPE;
                break;
            case HELICARRIER_TYPE:
                game.currentImage = BAXTER_TYPE;
                break;
            case BAXTER_TYPE:
                game.currentImage = BACKGROUND_TYPE;
                break;
        }
        
        
        if(game.getGUIDecor().get(BACKGROUND_TYPE).getX() < 0)
        {
            time = new InfinityGemSagaTimer(game, false);
            game.removeButtons();
        }
    }
}
