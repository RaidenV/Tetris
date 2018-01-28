package tetris;

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
public class GameMusic
{
    ArrayList<String> mSongs;
    AudioInputStream mAudStream;
    Clip mAudClip;
    Random mRan;
    
    private final String mLibFolder= "src/tetris/Resources/music/";

    public GameMusic()
    {
        loadLib();
        init();
    }

    private void init( )
    {
        mRan = new Random();
        
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
    
    private void loadLib()
    {
        File lib = new File( mLibFolder );
        
        File[] files = lib.listFiles();
        
        mSongs = new ArrayList<>();
        
        for ( int i = 0; i < files.length; ++i )
        {
            if ( files[i].isFile() )
            {
                mSongs.add( files[i].getAbsolutePath() );
            }
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
}
