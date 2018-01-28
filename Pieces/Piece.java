/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris.Pieces;

import tetris.Math.Rot2d;
import tetris.Enums.Directions.Dir;

/*========================
 *
 * @author Alex Baum
 * Date: Jan 18, 2018
 * Time: 7:37:34 PM
 *
 *========================*/
public abstract class Piece 
{
    protected static final int PIECE_SZ = 4;
    protected static final int X = 0;
    protected static final int Y = 1;
    protected static final int VERTEX = 2;
    protected final int[] mStartLoc;   
    private final int mId = -1;
    
    
    protected int [][] mCurLoc;
    
    static enum Direction{ RIGHT, LEFT };
    
    public Piece( int[] start)
    {
        mCurLoc = new int[PIECE_SZ][2];
        mStartLoc = start;
    }
    
    public int[][] getCurLoc()
    {
        return mCurLoc;
    }
    
    public int[][] move( Dir d )
    {
        switch ( d )
        {
            case DOWN:
                return advance();
            case LEFT:
                return moveLeft();
            case RIGHT:
                return moveRight();
            case ROTL:
            case ROTR:
                return rotate( d );
            default:
                System.out.println( "You shouldn't be here" );
                return advance();
        }
    }
    
    public int[][] moveRight()
    {
        mCurLoc = peekRight();
        return mCurLoc;
    }
    
    public int[][] peekRight()
    {
        int[][] p = getLocCopy();
        
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            p[i][X]++;
        }
        
        return p;
    }
    
    public int[][] moveLeft()
    {
        mCurLoc = peekLeft();
        
        return mCurLoc;
    }
    
    public int[][] peekLeft()
    {
        int[][] p = getLocCopy();
        
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            p[i][X]--;
        }
        
        return p;
    }
    
    public int[][] advance()
    {
        mCurLoc = peekAdvance();
        return mCurLoc;
    }
    
    public int[][] peekAdvance()
    {

        int[][] p = getLocCopy();
        
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            p[i][Y]++;
        }
        
        return p;
    }
    
    public int[][] rotate( Dir d )
    {
        mCurLoc = peekRotate( d );    
        return mCurLoc;
    }
    
    public int[][] peek( Dir d )
    {
        switch ( d )
        {
            case DOWN:
                return peekAdvance();
            case LEFT:
                return peekLeft();
            case RIGHT:
                return peekRight();
            case ROTL:
            case ROTR:
                return peekRotate( d );
            default:
                System.out.println( "You shouldn't be here" );
                return peekAdvance();
        }
    }
    
    public int[][] peekRotate( Dir d )
    {
        int[][] p = getLocCopy();    
        
        switch ( d )
        {
            case ROTR:
                p = Rot2d.rotate2d( p, VERTEX,  90.0 );
                break;
            case ROTL:
                p = Rot2d.rotate2d( p, VERTEX, -90.0 );
                break;
            default:
                System.out.println( "ENUM FAIL!!!!" );
                break;
        }
               
        return p;
    }
    
    public int getId()
    {
        return mId;
    }
    
    @Override
    public String toString()
    {
        String str = "";
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            str += "Loc[" + i + "]: X " + mCurLoc[i][X]
                                + " Y " + mCurLoc[i][Y] + "\n";
        }
        
        return str;
    }
    
    private int[][] getLocCopy()
    {
        int[][] p = new int[PIECE_SZ][2];
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            for ( int n = 0; n < 2; ++n )
            {
                p[i][n] = mCurLoc[i][n];
            }
        }
        
        return p;
    }
    public abstract void configStart();
}
