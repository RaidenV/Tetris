/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris.Pieces;

import tetris.Enums.Directions.Dir;
import tetris.Pieces.Piece;

/*========================
 *
 * @author Alex Baum
 * Date: Jan 21, 2018
 * Time: 6:52:34 PM
 *
 
Square Piece

    [0][1]
    [2][3]

 *========================*/
public class SqPiece extends Piece
{
    
    private final int mId = 5;
    
    public SqPiece( int[] start )
    {
       super( start );
       configStart();    
    }
    
    @Override
    public int getId()
    {
        return mId;
    }

    @Override
    public void configStart()
    {
        final int initX = mStartLoc[0];
        final int initY = mStartLoc[1];
        mCurLoc[0][X] = initX;
        mCurLoc[0][Y] = initY;
        mCurLoc[1][X] = initX + 1;
        mCurLoc[1][Y] = initY;
        mCurLoc[2][X] = initX;
        mCurLoc[2][Y] = initY + 1;
        mCurLoc[3][X] = initX + 1;
        mCurLoc[3][Y] = initY + 1;
    }

    // The square piece does not rotate.
    // Simply override and return the current location.
    @Override
    public int[][] rotate( Dir d )
    {
        return mCurLoc;
    }
    
    @Override
    public int[][] peekRotate( Dir d )
    {
        return mCurLoc;
    }
    
    @Override
    public String toString()
    {
        String str = "Square\n";
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            str += "[" + i + "]:";
            for ( int n = 0; n < 2; ++n )
            {
                if ( n == 0 )
                {
                    str += " X " + mCurLoc[i][n];
                }
                
                else
                {
                    str += " Y " + mCurLoc[i][n] + "\n";
                }
            }
        }
        str += "\n";
        return str;
    }

}
