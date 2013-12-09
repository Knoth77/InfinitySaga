package infinity_gem_saga.file;

import infinity_gem_saga.InfinityGemSaga.InfinityGemSagaPropertyType;
import infinity_gem_saga.data.InfinityGemSagaDataModel;
import infinity_gem_saga.data.InfinityGemSagaLevelRecord;
import infinity_gem_saga.data.InfinityGemSagaRecord;
import infinity_gem_saga.ui.InfinityGemSagaMiniGame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import properties_manager.PropertiesManager;

/**
 * This class provides services for efficiently loading and saving
 * binary files for the Mahjong game application.
 * 
 * @author Richard McKenna
 */
public class InfinityGemSagaFileManager
{
    // WE'LL LET THE GAME KNOW WHEN DATA LOADING IS COMPLETE
    private InfinityGemSagaMiniGame miniGame;
    
    /**
     * Constructor for initializing this file manager, it simply keeps
     * the game for later.
     * 
     * @param initMiniGame The game for which this class loads data.
     */
    public InfinityGemSagaFileManager(InfinityGemSagaMiniGame initMiniGame)
    {
        // KEEP IT FOR LATER
        miniGame = initMiniGame;
    }

    /**
     * This method loads the contents of the levelFile argument so that
     * the player may then play that level. 
     * 
     * @param levelFile Level to load.
     */
    public void loadLevel(String levelFile)
    {
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            File fileToOpen = new File(levelFile);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            
            // FIRST READ THE GRID DIMENSIONS
            int initGridColumns = dis.readInt();
            int initGridRows = dis.readInt();
            int[][] newGrid = new int[initGridColumns][initGridRows];
            
            // AND NOW ALL THE CELL VALUES
            for (int i = 0; i < initGridColumns; i++)
            {                        
                for (int j = 0; j < initGridRows; j++)
                {
                    newGrid[i][j] = dis.readInt();
                }
            }
            
            // EVERYTHING WENT AS PLANNED SO LET'S MAKE IT PERMANENT
            InfinityGemSagaDataModel dataModel = (InfinityGemSagaDataModel)miniGame.getDataModel();
            dataModel.initLevelGrid(newGrid, initGridColumns, initGridRows);
            dataModel.setCurrentLevel(levelFile);
            
        }
        catch(Exception e)
        {
            // LEVEL LOADING ERROR
            miniGame.getErrorHandler().processError(InfinityGemSagaPropertyType.LOAD_LEVEL_ERROR);
        }
    }    
    
    /**
     * This method loads the player record from the records file
     * so that the user may view stats.
     * 
     * @return The fully loaded record from the player record file.
     */
    public InfinityGemSagaRecord loadRecord()
    {
        InfinityGemSagaRecord recordToLoad = new InfinityGemSagaRecord();
        
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(InfinityGemSagaPropertyType.DATA_PATH);
            String recordPath = dataPath + props.getProperty(InfinityGemSagaPropertyType.RECORD_FILE_NAME);
            File fileToOpen = new File(recordPath);

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            // FIRST READ THE NUMBER OF LEVELS
            int numLevels = dis.readInt();

            for (int i = 0; i < numLevels; i++)
            {
                String levelName = dis.readUTF();
                InfinityGemSagaLevelRecord rec = new InfinityGemSagaLevelRecord();
                rec.stars = dis.readInt();
                rec.highScore = dis.readInt();
                rec.completed = dis.readBoolean();
                recordToLoad.addInfinityLevelRecord(levelName, rec);
            }

        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }        
        return recordToLoad;
    }
    
    
    public void saveRecord(){
    // THIS CURRENTLY DOES NOTHING, INSTEAD, IT MUST SAVE ALL THE
        // PLAYER RECORDS IN THE SAME FORMAT IT IS BEING LOADED
       
        // LOAD THE RAW DATA SO WE CAN USE IT
        // OUR LEVEL FILES WILL HAVE THE DIMENSIONS FIRST,
        // FOLLOWED BY THE GRID VALUES
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String dataPath = props.getProperty(InfinityGemSagaPropertyType.DATA_PATH);
            String recordPath = dataPath + props.getProperty(InfinityGemSagaPropertyType.RECORD_FILE_NAME);
            File fileToOpen = new File(recordPath);

            FileOutputStream fis = new FileOutputStream(fileToOpen);
            BufferedOutputStream bis = new BufferedOutputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.write(miniGame.getPlayerRecord().toByteArray());
            bis.close();
          
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }   
        
    }
    
}