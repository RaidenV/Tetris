package tetris;

import java.util.ArrayList;
import java.util.List;
import tetris.Listeners.GameEventListener;
import tetris.Listeners.GameLevelListener;

/*========================
 *
 * @author Alex Baum
 * Date: Jan 29, 2018
 * Time: 6:46:56 PM
 *
 *========================*/
public class GameLevel implements GameEventListener
{

    private final int DEFAULT_LEVEL = 0;
    private final int DEFAULT_DIFF  = 25;
    private final int DEFAULT_TARGET = 1000;
    private final int DIFF_INC = 1;
    private final int TAR_INC = 5000;
    
    private int mTarget;
    private static int mDifficulty;
    private static int mLevelNum;
    private List<GameLevelListener> mListeners = new ArrayList<>();

    public GameLevel()
    {
        gameOver();
    }

    public int getDifficulty()
    {
        return mDifficulty;
    }

    public int getTarget()
    {
        return mTarget;
    }

    public int getCurLevel()
    {
        return mLevelNum;
    }

    public void increaseLevel()
    {
        mLevelNum   += 1;
        mTarget     += TAR_INC;
        mDifficulty -= DIFF_INC;
        if ( mDifficulty < 1 )
            mDifficulty = 1;
    }
    
    public void reset()
    {
        mLevelNum   = DEFAULT_LEVEL;
        mDifficulty = DEFAULT_DIFF;
        mTarget     = DEFAULT_TARGET;
    }

    public void checkLevel( int curScore )
    {
        if ( curScore > mTarget )
        {
            mListeners.forEach((g) ->
            {
                g.levelComplete( mLevelNum );
            });
        }
    }
    
    /*=========================================================================
    Name        addListener
    
    Purpose     Adds a listener for game events.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void addListener( GameLevelListener newListener )
    {
        mListeners.add( newListener );
    }

    @Override
    public void rowComplete( int num )
    {
       
    }

    @Override
    public void gameStart()
    {
        
    }

    @Override
    public void gameOver()
    {
        reset();
    }
}
