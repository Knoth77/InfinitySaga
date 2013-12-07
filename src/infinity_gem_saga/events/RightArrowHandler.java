/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package infinity_gem_saga.events;

import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import static infinity_gem_saga.InfinityGemSagaConstants.*;
import infinity_gem_saga.timer.InfinityGemSagaTimer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author Milk
 */
public class RightArrowHandler implements ActionListener {

    private InfinityGemSagaMiniGame game;
    private InfinityGemSagaTimer time;

    public RightArrowHandler(InfinityGemSagaMiniGame initGame) {
        game = initGame;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        switch (game.currentImage) {
            case BACKGROUND_TYPE:
                game.currentImage = BAXTER_TYPE;
                break;
            case BAXTER_TYPE:
                game.currentImage = HELICARRIER_TYPE;
                break;
            case HELICARRIER_TYPE:
                game.currentImage = ATLANTIS_TYPE;
                break;
            case ATLANTIS_TYPE:
                game.currentImage = GENOSHA_TYPE;
                break;
        }
        
        if (game.getGUIDecor().get(BACKGROUND_TYPE).getX() > -5120) 
        {
            time = new InfinityGemSagaTimer(game, true);
            game.removeButtons();
        }

    }
}
