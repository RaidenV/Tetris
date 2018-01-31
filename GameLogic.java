package tetris;

import tetris.Listeners.GameEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import tetris.Pieces.*;
import tetris.Enums.Directions.Dir;

/*========================
 *
 * @author Alex Baum
 * Date: Jan 18, 2018
 * Time: 7:30:00 PM
 *
 *========================*/
public class GameLogic
{

    // Directions for detecting collision;
    enum Direction
    {
        RIGHT, LEFT, DOWN, ROTL, ROTR
    };

    // Board size values;
    private static final int BOARD_SZ_X = 10;
    private static final int BOARD_SZ_Y = 30;
    // Definition of X and Y for clarity;
    private static final int X = 0;
    private static final int Y = 1;
    // Number of unique pieces;
    private static final int UNIQUE_PCS = 7;
    // Number of sections of a piece;
    private static final int PIECE_SZ = 4;
    // Spawn locations for a piece;
    private static final int SPAWN_X = ( BOARD_SZ_X / 2 ) - 1;
    private static final int SPAWN_Y = 0;
    // Score added when a piece advances (moves down) one square;
    private static final int ADVANCE_SCORE = 10;
    // Score added when a row is complete;
    private static final int ROW_SCORE = 200;
    // True if a new piece was just added to the board;
    private static boolean mNewPieceAdded = false;
    // Holds all observers;
    private final List<GameEventListener> mListeners = new ArrayList<>();
    // Random variable;
    private final Random mRan;

    // Current board;
    private final int[][] mBoard;
    // Current score;
    private int mScore;
    // Pieces;
    private Piece mCurPiece;
    private Piece mNextPiece;
    // True if the game is running;
    private boolean mRunning;

    public GameLogic()
    {
        mBoard = new int[BOARD_SZ_X][BOARD_SZ_Y];
        mRan = new Random();
    }

    /*=========================================================================
    Name        addListener
    
    Purpose     Adds a listener for game events.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void addListener( GameEventListener newListener )
    {
        mListeners.add(newListener);
    }

    /*=========================================================================
    Name        getBoard
    
    Purpose     Returns a copy of the current board
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public int[][] getBoard()
    {
        return mBoard;
    }

    /*=========================================================================
    Name        getNextPiece
    
    Purpose     Returns a board of size[szx][szy] where in the piece will be
                placed.
    
    Inputs      szx     -   X dimension of the area upon which the piece will be
                            placed
                szy     -   Y dimension of the area upon which the piece will be
                            placed
    
    Returns     int[][] -   Board of size[szx][szy] with piece placed in the
                            center
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public int[][] getNextPiece( int szx, int szy )
    {
        int[][] pieceBoard = new int[szx][szy];
        int[][] nextPiecePos = mNextPiece.getCurLoc();
        int center = szx / 2 - 1;

        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            int initY = nextPiecePos[i][Y] + 2;
            int initX = nextPiecePos[i][X] - ( SPAWN_X - center );

            pieceBoard[initX][initY] = mNextPiece.getId();
        }

        return pieceBoard;
    }

    /*=========================================================================
    Name        getScore
    
    Purpose     Returns the current score.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public int getScore()
    {
        return mScore;
    }
    
    /*=========================================================================
    Name        isRunning
    
    Purpose     Returns whether the board is running or not
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public boolean isRunning()
    {
        return mRunning;
    }

    /*=========================================================================
    Name        reset
    
    Purpose     Resets the game board.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void reset()
    {
        for ( int i = 0; i < BOARD_SZ_X; ++i )
        {
            for ( int n = 0; n < BOARD_SZ_Y; ++n )
            {
                mBoard[i][n] = 0;
            }
        }

        mScore = 0;
        mRunning = false;
    }

    /*=========================================================================
    Name        update
    
    Purpose     Called to update the game board.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void update()
    {
        if ( !mRunning )
        {
            mCurPiece = getNewPiece();
            mNextPiece = getNewPiece();

            placePiece();

            mRunning = true;
            gameStart();
        }

        else
        {
            boolean canMove = moveDown();
            if ( !canMove && !mNewPieceAdded )
            {
                mCurPiece = mNextPiece;
                checkBoard();
                placePiece();               
                mNextPiece = getNewPiece();
                mNewPieceAdded = true;
            }
            else if ( !canMove )
            {
                gameOver();
            }
            else
            {
                mNewPieceAdded = false;
            }
        }
    }

    /*=========================================================================
    Name        checkBoard
    
    Purpose     Checks the board for completed rows.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private void checkBoard()
    {
        int rowsComplete = 0;
        for ( int i = BOARD_SZ_Y - 1; i >= 0; --i )
        {
            int comp = 0;
            for ( int n = 0; n < BOARD_SZ_X; ++n )
            {
                if ( mBoard[n][i] != 0 )
                {
                    comp++;
                }

            }
            if ( comp == BOARD_SZ_X )
            {
                for ( int m = i; m >= 1; --m )
                {
                    for ( int j = 0; j < BOARD_SZ_X; ++j )
                    {
                        mBoard[j][m] = mBoard[j][m - 1];
                    }
                }
                mScore += ROW_SCORE;
                i = BOARD_SZ_Y;
                rowsComplete += 1;
            }
        }

        if ( rowsComplete != 0 )
        {
            rowComplete(rowsComplete);
        }
    }
    
    /*=========================================================================
    Name        gameStart
    
    Purpose     Game start event
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void gameStart()
    {
        mListeners.forEach((g) ->
        {
            g.gameStart();
        });
    }

    /*=========================================================================
    Name        rowComplete
    
    Purpose     Row complete event
    
    Input       num     -   Number of rows completed
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void rowComplete( int num )
    {
        mListeners.forEach((g) ->
        {
            g.rowComplete(num);
        });
    }

    /*=========================================================================
    Name        gameOver
    
    Purpose     Game over event
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void gameOver()
    {
        mListeners.forEach((g) ->
        {
            g.gameOver();
        });
    }

    /*=========================================================================
    Name        dropPiece
    
    Purpose     Drops a piece
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void dropPiece()
    {
        if ( !mNewPieceAdded )
            while ( moveDown() )
            {
            }
     }

    
    /*
     * The following movement functions were employed to add a layer of 
     * abstraction.  This way, if a move has some alternate effect, that
     * effect can be called outside of the agnostic move function.
     */
    /*=========================================================================
    Name        moveDown
    
    Purpose     Moves a piece down one step
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public boolean moveDown()
    {
        boolean moved = move( Dir.DOWN );
        
        if ( moved )
            mScore += ADVANCE_SCORE;
        
        return moved;
    }

    /*=========================================================================
    Name        moveRight
    
    Purpose     Moves the current piece right.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void moveRight()
    {
        move(Dir.RIGHT);
    }

    /*=========================================================================
    Name        moveLeft
    
    Purpose     Moves the current piece left.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void moveLeft()
    {
        move(Dir.LEFT);
    }

    /*=========================================================================
    Name        rotateRight
    
    Purpose     Rotate a piece 90 degrees to the right.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void rotateRight()
    {
        move(Dir.ROTR);
    }

    /*=========================================================================
    Name        rotateLeft
    
    Purpose     Rotate a piece 90 degrees to the left.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public void rotateLeft()
    {
        move(Dir.ROTL);
    }

    /*=========================================================================
    Name        move
    
    Purpose     Move a piece in the given direction/rotation.
    
    Inputs      d       -   direction to move a piece
    
    Returns     boolean -   True if able to move, false if not
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private boolean move( Dir d )
    {
        if ( mRunning )
        {
            int[][] p = mCurPiece.peek(d);

            // Check for boundary
            if ( !checkBounds(p) )
            {
                return false;
            }

            int[] check = detectCol(d);

            for ( int i = 0; i < check.length; ++i )
            {
                if ( getBoardVal(p, check[i]) != 0 )
                {
                    return false;
                }
            }

            for ( int i = 0; i < PIECE_SZ; ++i )
            {
                clrBoardVal(mCurPiece.getCurLoc(), i);
            }

            mCurPiece.move(d);
            placePiece();
            
            return true;
            
        }
        return false;
    }

    /*=========================================================================
    Name        getBoardX
    
    Purpose     Returns the board x dimension.
    
    Return      int -   X dimension
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public int getBoardX()
    {
        return BOARD_SZ_X;
    }

    /*=========================================================================
    Name        getBoardY
    
    Purpose     Returns the board y dimension.
    
    Return      int -   Y dimension
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    public int getBoardY()
    {
        return BOARD_SZ_Y;
    }

    /*=========================================================================
    Name        placePiece
    
    Purpose     Places the current piece on the board.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private void placePiece()
    {
        int[][] p = mCurPiece.getCurLoc();
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            setBoardVal(p, i);
        }
    }

    /*=========================================================================
    Name        getBoardVal
    
    Purpose     Get the current value at the point specified.
                 
    Inputs      p       -   piece
                idx     -   idx within the piece to get the value of.
    
    Return      int     -   current value of the space on the board
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private int getBoardVal( int[][] p, int idx )
    {
        int i = 0;
        try
        {
            i = mBoard[p[idx][X]][p[idx][Y]];
        }
        catch ( ArrayIndexOutOfBoundsException e )
        {
            System.out.println(mCurPiece.toString());
        }
        return i;
    }

    /*=========================================================================
    Name        setBoardVal
    
    Purpose     Set the current value at the point specified.
                 
    Inputs      p       -   piece
                idx     -   idx within the piece to set the value of
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private void setBoardVal( int[][] p, int idx )
    {
        mBoard[p[idx][X]][p[idx][Y]] = mCurPiece.getId();
    }

    /*=========================================================================
    Name        clrBoardVal
    
    Purpose     Clear the value from the location specified.
                 
    Inputs      p       -   piece
                idx     -   idx within the piece to set the value of
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private void clrBoardVal( int[][] p, int idx )
    {
        mBoard[p[idx][X]][p[idx][Y]] = 0;
    }

    /*=========================================================================
    Name        getNewPiece
    
    Purpose     Randomly get a new piece
    
    Return      Piece
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private Piece getNewPiece()
    {
        int p = mRan.nextInt(UNIQUE_PCS);
        int[] s = new int[2];
        s[0] = SPAWN_X;
        s[1] = SPAWN_Y;

        Piece np;

        switch ( p )
        {
            case 0:
                np = new LPiece(s);
                break;
            case 1:
                np = new LnPiece(s);
                break;
            case 2:
                np = new RPiece(s);
                break;
            case 3:
                np = new SPiece(s);
                break;
            case 4:
                np = new SqPiece(s);
                break;
            case 5:
                np = new TPiece(s);
                break;
            case 6:
                np = new ZPiece(s);
                break;
            default:
                np = new LnPiece(s);
                break;
        }

        return np;
    }

    /*=========================================================================
    Name        checkBounds
    
    Purpose     Checks the location of a piece against the board boundaries.
                 
    Inputs      p       -   locations to check
    
    Return      boolean -   True if able to move, false if not
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private boolean checkBounds( int[][] p )
    {
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            for ( int n = 0; n < 2; ++n )
            {
                int lim = BOARD_SZ_X;
                switch ( n )
                {
                    case 0:
                        break;
                    case 1:
                        lim = BOARD_SZ_Y;
                        break;
                    default:
                        System.out.println("Shouldn't be here");
                        break;
                }
                if ( ( p[i][n] >= lim ) || ( p[i][n] < 0 ) )
                {
                    return false;
                }
            }
        }

        return true;
    }

    /*=========================================================================
    Name        detectCol
    
    Purpose     Returns the indices of a piece for which it is valid to check
                for collision.
    
    Input       d       -       Direction in which collision should be checked.
    
    Return      int[]   -       indices to be checked;
    
    Notes       As pieces cannot conflict with themselves, it's important that
                only the indicies of the piece which cannot possibly conflict
                with itself is checked before moving.
    
                Example using a simple advancement:
    
                    [0][1]
                    [2][3]
                
                If indicies 2 and 3 are checked for collision at their next
                location, the game progresses perfectly, however if indices
                0 and 1 are checked, they will most definitely return a
                collision as 2 and 3 are currently occupying that space.
    
    History     25 Jan 18   AFB     Created
    ==========================================================================*/
    private int[] detectCol( Dir d )
    {
        int[][] curPc = mCurPiece.getCurLoc();
        int[][] newPc;

        switch ( d )
        {
            case DOWN:
                newPc = mCurPiece.peekAdvance();
                break;
            case RIGHT:
                newPc = mCurPiece.peekRight();
                break;
            case LEFT:
                newPc = mCurPiece.peekLeft();
                break;
            case ROTR:
                newPc = mCurPiece.peekRotate(d);
                break;
            case ROTL:
                newPc = mCurPiece.peekRotate(d);
                break;
            default:
                System.out.println(Arrays.toString(
                    Thread.currentThread().getStackTrace()));
                newPc = mCurPiece.peekAdvance();
                break;
        }

        ArrayList<Integer> include = new ArrayList<>();
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            boolean conflict = false;
            for ( int n = 0; n < PIECE_SZ; ++n )
            {
                if ( newPc[i][X] == curPc[n][X] )
                {
                    if ( newPc[i][Y] == curPc[n][Y] )
                    {
                        conflict = true;
                    }
                }
            }

            if ( !conflict )
            {
                include.add(i);
            }
        }

        int[] ret = new int[include.size()];

        for ( int i = 0; i < include.size(); ++i )
        {
            ret[i] = include.get(i);
        }

        return ret;
    }
}
