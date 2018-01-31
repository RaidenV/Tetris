package tetris;

import tetris.Listeners.GameEventListener;
import tetris.Listeners.GameLevelListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
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
 * Date: Jan 26, 2018
 * Time: 7:50:21 PM
 *
 *========================*/
public class GameMusic implements GameEventListener, GameLevelListener
{
    ArrayList<String> mSongs;
    AudioInputStream mAudStream;
    Clip mAudClip;
    Random mRan;
    
    private final String MUSIC_FOLDER_NAME= "music";

    public GameMusic()
    {
        mRan = new Random();
        loadLib();
        init();
    }

    private void init( )
    {       
        try
        {
            File audioFile = new File(getRanFile());
            mAudStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = mAudStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);

            mAudClip = ( Clip ) AudioSystem.getLine(info);
            mAudClip.open(mAudStream);
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
            if ( foldername.endsWith(MUSIC_FOLDER_NAME) )
            {
                if ( file.getName().endsWith(".wav") )
                    filelist.add( file );
            }
            if ( file.isDirectory() )
                searchFolder( file.getAbsolutePath(), filelist );
        }
    }
    
    private void loadLib()
    {
        ArrayList<File> flist = new ArrayList<>();
        searchFolder( System.getProperty( "user.dir" ), flist );
        
        mSongs = new ArrayList<>();
        
        for ( int i = 0; i < flist.size(); ++i )
        {
            mSongs.add( flist.get(i).getAbsolutePath() );
        }   
    }
    
    private String getRanFile()
    {
        int file = mRan.nextInt( mSongs.size() );
        
        return mSongs.get( file );
    }

    public void startBg()
    {
        mAudClip.loop( Clip.LOOP_CONTINUOUSLY );
    }

    public void stopBg()
    {
        mAudClip.stop();
    }
    
    public void resetBg()
    {
        init();
    }

    @Override
    public void rowComplete( int num )
    {
        
    }

    @Override
    public void gameStart()
    {
        init();
        startBg();
    }

    @Override
    public void gameOver()
    {
        stopBg();
    }

    @Override
    public void levelComplete( int lvl )
    {
       stopBg();
    }
}
