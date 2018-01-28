/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris.Pieces;

/*========================
 *
 * @author Alex Baum
 * Date: Jan 23, 2018
 * Time: 7:25:32 PM
 *

S Shaped Piece

      [2][3]
   [0][1]

 *========================*/
public class SPiece extends Piece
{
    private final int mId = 4;
    
    public SPiece( int[] start )
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
        final int initX = mStartLoc[X];
        final int initY = mStartLoc[Y];
        
        mCurLoc[0][X] = initX - 1;
        mCurLoc[0][Y] = initY + 1;
        mCurLoc[1][X] = initX;
        mCurLoc[1][Y] = initY + 1;
        mCurLoc[2][X] = initX;
        mCurLoc[2][Y] = initY;
        mCurLoc[3][X] = initX + 1;
        mCurLoc[3][Y] = initY;
        
    }
    
    @Override
    public String toString()
    {
        String str = "S-Piece\n";
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
