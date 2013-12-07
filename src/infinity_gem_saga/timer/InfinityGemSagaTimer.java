package infinity_gem_saga.timer;

import static infinity_gem_saga.InfinityGemSagaConstants.ATLANTIS_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.BACKGROUND_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.BAXTER_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.GENOSHA_TYPE;
import static infinity_gem_saga.InfinityGemSagaConstants.HELICARRIER_TYPE;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.util.Timer;
import java.util.TimerTask;


public class InfinityGemSagaTimer {

    Timer timer;
    
    public InfinityGemSagaTimer(InfinityGemSagaMiniGame game, boolean leftRight) {
        timer = new Timer();
        timer.schedule(new InfinityGemSagaTask(game, leftRight), 0, 1 * 100);
    }
    
    class InfinityGemSagaTask extends TimerTask {

        InfinityGemSagaMiniGame game;
        int x = 0;
        boolean rightLeft;
        
        public InfinityGemSagaTask(InfinityGemSagaMiniGame g, boolean rL) {
            
            game = g;
            x = 0;
            rightLeft = rL;
        }

        @Override
        public void run() {

            if (x < 10 && rightLeft == true) {
                game.getGUIDecor().get(BACKGROUND_TYPE).setX((int) game.getGUIDecor().get(BACKGROUND_TYPE).getX() - 128);
                game.getGUIDecor().get(BAXTER_TYPE).setX((int) game.getGUIDecor().get(BAXTER_TYPE).getX() - 128);
                game.getGUIDecor().get(HELICARRIER_TYPE).setX((int) game.getGUIDecor().get(HELICARRIER_TYPE).getX() - 128);
                game.getGUIDecor().get(ATLANTIS_TYPE).setX((int) game.getGUIDecor().get(ATLANTIS_TYPE).getX() - 128);
                game.getGUIDecor().get(GENOSHA_TYPE).setX((int) game.getGUIDecor().get(GENOSHA_TYPE).getX() - 128);
                x++;
            }
            else if(x < 10 && rightLeft == false)
            {
                game.getGUIDecor().get(BACKGROUND_TYPE).setX((int) game.getGUIDecor().get(BACKGROUND_TYPE).getX() + 128);
                game.getGUIDecor().get(BAXTER_TYPE).setX((int) game.getGUIDecor().get(BAXTER_TYPE).getX() + 128);
                game.getGUIDecor().get(HELICARRIER_TYPE).setX((int) game.getGUIDecor().get(HELICARRIER_TYPE).getX() + 128);
                game.getGUIDecor().get(ATLANTIS_TYPE).setX((int) game.getGUIDecor().get(ATLANTIS_TYPE).getX() + 128);
                game.getGUIDecor().get(GENOSHA_TYPE).setX((int) game.getGUIDecor().get(GENOSHA_TYPE).getX() + 128);
                x++;
            }
            else
            {
                 timer.cancel();
                 timer.purge();
            }
        }
    }
}