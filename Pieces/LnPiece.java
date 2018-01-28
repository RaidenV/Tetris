/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris.Pieces;

/*========================
 *
 * @author Alex Baum
 * Date: Jan 21, 2018
 * Time: 7:00:42 PM
 *

Line Piece

  [0][1][2][3]


 *========================*/
public class LnPiece extends Piece
{
    private final int mId = 2;
    
    public LnPiece( int[] start )
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
        mCurLoc[0][X] = initX - 1;
        mCurLoc[1][X] = initX;
        mCurLoc[2][X] = initX + 1;
        mCurLoc[3][X] = initX + 2;
        
        for ( int i = 0; i < PIECE_SZ; ++i )
        {
            mCurLoc[i][Y] = initY;
        }
    }
    
    @Override
    public String toString()
    {
        String str = "Line Piece\n";
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
