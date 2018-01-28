/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.io.File;
import java.io.IOException;
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
public class GameEffects implements GameEventListener
{

    AudioInputStream mAudStream;
    Clip mRowComplete;
    Clip mTwoRowComplete;
    Clip mThreeRowComplete;
    Clip mFourRowComplete;
    Clip mGameOver;

    public GameEffects()
    {
    }

    public void setRowEffect( String filename )
    {
        mRowComplete = addClip( filename );
    }
    
    public void setTwoRowEffect( String filename )
    {
        mTwoRowComplete = addClip( filename );
    }
    
    public void setThreeRowEffect( String filename )
    {
        mThreeRowComplete = addClip( filename );
    }
    
    public void setFourRowEffect( String filename )
    {
        mFourRowComplete = addClip( filename );
    }

    public void setGameOverEffect( String filename )
    {
        mGameOver = addClip( filename );
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
                playSound( mRowComplete );
                break;
            case 2:
                playSound( mTwoRowComplete );
                break;
            case 3:
                playSound( mThreeRowComplete );
                break;
            case 4:
                playSound( mFourRowComplete );
                break;
            default:
                playSound( mRowComplete );
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
        playSound( mGameOver );
    }
}
