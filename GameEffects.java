package tetris;

import tetris.Listeners.GameEventListener;
import tetris.Listeners.GameLevelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/*========================
 *
 * @author Alex Baum
 * Date: Jan 27, 2018
 * Time: 4:37:38 PM
 *
 *========================*/
public class GameEffects implements GameEventListener, GameLevelListener
{

    // I may be too tired to figure out a pleasant way of extracting the desired
    // files with assurance that their name is correct.
    // Thinking back on it, I always loved when games made it easy for me to
    // change the music by just overwriting a file.
    private final String SCORE = "score.wav";
    private final String TWO_ROW_FNAME = "tworow.wav";
    private final String THREE_ROW_FNAME = "threerow.wav";
    private final String FOUR_ROW_FNAME = "fourrow.wav";
    private final String GAMEOVER = "gameover.wav";
    private final String LVL_COMP = "lvlcomplete.wav";

    AudioInputStream mAudStream;
    Clip mRowComplete;
    Clip mTwoRowComplete;
    Clip mThreeRowComplete;
    Clip mFourRowComplete;
    Clip mLvlComplete;
    Clip mGameOver;

    public GameEffects()
    {
        loadEffects();
    }

    
    // Recursively search all subfolders for appropriate files
    private void searchFolder( String foldername, ArrayList<File> filelist )
    {
        File folder = new File( foldername );
        if ( folder.listFiles().length == 0 )
            return;
        
        File[] files = folder.listFiles();
        for( File file : files )
        {
            if ( file.getName().endsWith(".wav") )
                filelist.add( file );
            if ( file.isDirectory() )
                searchFolder( file.getAbsolutePath(), filelist );
        }
    }
    public final void loadEffects()
    {

       ArrayList<File> fnames = new ArrayList<>();
       searchFolder( System.getProperty( "user.dir" ), fnames );
       
        for ( File file : fnames )
        {
            switch ( file.getName() )
            {
                case SCORE:
                    setRowEffect(file.getAbsolutePath());
                    break;
                case TWO_ROW_FNAME:
                    setTwoRowEffect(file.getAbsolutePath());
                    break;
                case THREE_ROW_FNAME:
                    setThreeRowEffect(file.getAbsolutePath());
                    break;
                case FOUR_ROW_FNAME:
                    setFourRowEffect(file.getAbsolutePath());
                    break;
                case GAMEOVER:
                    setGameOverEffect(file.getAbsolutePath());
                    break;
                case LVL_COMP:
                    setLvlComplete(file.getAbsolutePath());
                    break;
                default:
                    break;
            }
        }

    }

    public void setRowEffect( String filename )
    {
        mRowComplete = addClip(filename);
    }

    public void setTwoRowEffect( String filename )
    {
        mTwoRowComplete = addClip(filename);
    }

    public void setThreeRowEffect( String filename )
    {
        mThreeRowComplete = addClip(filename);
    }

    public void setFourRowEffect( String filename )
    {
        mFourRowComplete = addClip(filename);
    }

    public void setGameOverEffect( String filename )
    {
        mGameOver = addClip(filename);
    }

    public void setLvlComplete( String filename )
    {
        mLvlComplete = addClip(filename);
    }

    private Clip addClip( String fname )
    {
        Clip clip = null;
        try
        {
            File audioFile = new File(fname);
            mAudStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = mAudStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            clip = ( Clip ) AudioSystem.getLine(info);
            clip.open(mAudStream);
        }
        catch ( UnsupportedAudioFileException e )
        {
            System.out.println("Unsupported format");
        }
        catch ( IOException e )
        {
            System.out.println("IOException");
        }
        catch ( LineUnavailableException e )
        {
            System.out.println("LineUnavailableException");
        }

        return clip;
    }

    @Override
    public void rowComplete( int num )
    {
        switch ( num )
        {
            case 1:
                playSound(mRowComplete);
                break;
            case 2:
                playSound(mTwoRowComplete);
                break;
            case 3:
                playSound(mThreeRowComplete);
                break;
            case 4:
                playSound(mFourRowComplete);
                break;
            default:
                playSound(mRowComplete);
                break;
        }
    }

    private void playSound( Clip clip )
    {
        if ( clip != null )
        {
            if ( !clip.isRunning() )
            {
                clip.setMicrosecondPosition(0);
                clip.start();
            }
        }
    }

    @Override
    public void gameOver()
    {
        playSound(mGameOver);
    }

    @Override
    public void gameStart()
    {

    }

    @Override
    public void levelComplete( int lvl )
    {
        playSound(mLvlComplete);
    }
    
}
