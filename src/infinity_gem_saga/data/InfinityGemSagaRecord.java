package infinity_gem_saga.data;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

/**
 * This class represents the complete playing history for the player since
 * originally starting the application. Note that it stores stats separately for
 * different levels.
 *
 * @author Richard McKenna
 */
public class InfinityGemSagaRecord
{
    // HERE ARE ALL THE RECORDS

    private HashMap<String, InfinityGemSagaLevelRecord> levelRecords;

    /**
     * Default constructor, it simply creates the hash table for storing all the
     * records stored by level.
     */
    public InfinityGemSagaRecord()
    {
        levelRecords = new HashMap();
    }

    // GET METHODS
    // - getGamesPlayed
    // - getWins
    // - getLosses
    // - getFastestTime
    /**
     * This method gets the wins for a given level.
     *
     * @param levelName Level for the request.
     *
     * @return The wins the player has earned for the levelName level.
     */
    public int getHighScore(String levelName)
    {
        InfinityGemSagaLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0        
        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE WINS
        else
            return rec.highScore;
    }

    /**
     * This method gets the losses for a given level.
     *
     * @param levelName Level for the request.
     *
     * @return The losses the player has earned for the levelName level.
     */
    public int getStars(String levelName)
    {
        InfinityGemSagaLevelRecord rec = levelRecords.get(levelName);

        // IF levelName ISN'T IN THE RECORD OBJECT
        // THEN SIMPLY RETURN 0

        if (rec == null)
            return 0;
        // OTHERWISE RETURN THE LOSSES
        else
            return rec.stars;
    }

    public boolean getCompleted(String levelName)
    {
        InfinityGemSagaLevelRecord rec = levelRecords.get(levelName);
        if (rec == null)
            return false;
        else
            return rec.completed;
    }
   
    
    // ADD METHODS
    // -addInfinityLevelRecord
    // -addWin
    // -addLoss
    /**
     * Adds the record for a level
     *
     * @param levelName
     *
     * @param rec
     */
    public void addInfinityLevelRecord(String levelName, InfinityGemSagaLevelRecord rec)
    {
        levelRecords.put(levelName, rec);
    }

    /**
     * This method adds a win to the current player's record according to the
     * level being played.
     *
     * @param levelName The level being played that the player won.
     *
     * @param score The time it took to win the game.
     */
    public void addWin(String levelName, int score, int stars, boolean win)
    {
        // GET THE RECORD FOR levelName
        InfinityGemSagaLevelRecord rec = levelRecords.get(levelName);

        // IF THE PLAYER HAS NEVER PLAYED A GAME ON levelName
        if (rec == null)
        {
            // MAKE A NEW RECORD FOR THIS LEVEL, SINCE THIS IS
            // THE FIRST TIME WE'VE PLAYED IT
            rec = new InfinityGemSagaLevelRecord();
            rec.stars = stars;
            rec.highScore = score;
            rec.completed = win;
            levelRecords.put(levelName, rec);
        }
        else
        {
            // WE'VE PLAYED THIS LEVEL BEFORE, SO SIMPLY
            // UPDATE THE STATS
            if (stars > rec.stars)
                rec.stars = stars;
            if (score > rec.highScore)
                rec.highScore = score;
            
             rec.completed = win;
        }
    }

    /**
     * This method adds a loss to the current player's record according to the
     * level being played.
     *
     * @param levelName The level being played that the player lost.
     */
    public void addLoss(String levelName, int score, int stars)
    {
        // GET THE RECORD FOR levelName
        InfinityGemSagaLevelRecord rec = levelRecords.get(levelName);

        // IF THE PLAYER HAS NEVER PLAYED A GAME ON levelName
        if (rec == null)
        {
            // MAKE A NEW RECORD FOR THIS LEVEL, SINCE THIS IS
            // THE FIRST TIME WE'VE PLAYED IT
            rec = new InfinityGemSagaLevelRecord();
            rec.stars = stars;
            rec.highScore = score;
            rec.completed = false;
            levelRecords.put(levelName, rec);
        }
        else
        {
            // WE'VE PLAYED THIS LEVEL BEFORE, SO SIMPLY
            // UPDATE THE STATS
            if (stars > rec.stars)
                rec.stars = stars;
            if (score > rec.highScore)
                rec.highScore = score;
        }
    }

    /**
     * This method constructs and fills in a byte array with all the necessary
     * data stored by this object. We do this because writing a byte array all
     * at once to a file is fast. Certainly much faster than writing to a file
     * across many write operations.
     *
     * @return A byte array filled in with all the data stored in this object,
     * which means all the player records in all the levels.
     *
     * @throws IOException Note that this method uses a stream that writes to an
     * internal byte array, not a file. So this exception should never happen.
     */
    public byte[] toByteArray() throws IOException
    {
        Iterator<String> keysIt = levelRecords.keySet().iterator();
        int numLevels = levelRecords.keySet().size();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(numLevels);
        while (keysIt.hasNext())
        {
            String key = keysIt.next();
            dos.writeUTF(key);
            InfinityGemSagaLevelRecord rec = levelRecords.get(key);
            dos.writeInt(rec.stars);
            dos.writeInt(rec.highScore);
            dos.writeBoolean(rec.completed);
        }
        // AND THEN RETURN IT
        return baos.toByteArray();
    }
}
