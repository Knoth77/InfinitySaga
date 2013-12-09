package infinity_gem_saga.data;

/**
 * This class stores game results for a given level. Note that this is
 * just a data holding class. It will be manipulated fully by the
 * MahjongSolitaireRecord class, which stores all the records and manages
 * loading and saving.
 * 
 * @author Richard McKenna
 */
public class InfinityGemSagaLevelRecord
{
    public int stars;
    public int highScore;
    public boolean completed;
}
