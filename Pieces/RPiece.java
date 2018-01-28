/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tetris.Pieces;

import tetris.Math.Rot2d;


/*========================
 *
 * @author Alex Baum
 * Date: Jan 21, 2018
 * Time: 7:39:23 PM
 *

Lowercase 'R' Piece

  [0]
  [1][2][3]

 *========================*/
public class RPiece extends Piece
{
    private final int mId = 3;
    
    public RPiece( int[] start )
    {
        super(start);
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
        mCurLoc[1][X] = initX - 1;
        mCurLoc[2][X] = initX;
        mCurLoc[3][X] = initX + 1;
        mCurLoc[0][Y] = initY;
        
        for ( int i = 1; i < PIECE_SZ; ++i )
        {
            mCurLoc[i][Y] = initY + 1;
        }
    }
    
    @Override
    public String toString()
    {
        String str = "R-Piece\n";
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
                    str += " Y " + mCurLoc[i][n]  + "\n";
                }
            }
        }
        str += "\n";
        return str;
    }
}
