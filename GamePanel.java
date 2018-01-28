/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tetris;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;

/*========================
 *
 * @author Alex Baum
 * Date: Jan 18, 2018
 * Time: 6:53:13 PM
 *
 *========================*/
public class GamePanel extends JPanel
{    
    private final int mBoardSizeX;
    private final int mBoardSizeY;
    private int mBoard[][];

    public GamePanel( int x, int y )
    {
        super();
        mBoardSizeX = x;
        mBoardSizeY = y;
        init();
    }

    private void init()
    {
        this.setBackground( new Color( 68, 76, 81 ) );
        mBoard = new int[mBoardSizeX][mBoardSizeY];
    }

    public void setBoard( int b[][] )
    {
        this.mBoard = b;
    }

    @Override
    public void paint( Graphics g )
    {
        super.paint(g);
        if ( g instanceof Graphics2D )
        {
            Graphics2D g2d = ( Graphics2D ) g;
            drawBoard(g2d);
        }
    }

    public void drawBoard( Graphics2D g2d )
    {

        Rectangle rect = this.getBounds();
        Point p = this.getLocation();
        int relSzX = rect.width / mBoardSizeX;
        int relSzY = rect.height / mBoardSizeY;
        int curX = 0;
        int curY = 0;
        
        for ( int i = 0; i < mBoardSizeY; ++i )
        {
            for ( int n = 0; n < mBoardSizeX; ++n )
            {
                drawBox(g2d, curX, curY, relSzX, relSzY, mBoard[n][i]);
                curX += relSzX;
            }
            curY += relSzY;
            curX = 0;
        }
    }

    public void drawBox( Graphics2D g2d, int x, int y, int wd, int ht, int full )
    {
        switch ( full )
        {
            case 0 : 
                g2d.setColor( new Color(38, 43, 46) );
                break;
            case 1 :
                g2d.setColor( Color.YELLOW );
                break;
            case 2 :
                g2d.setColor( Color.GREEN );
                break;
            case 3 :
                g2d.setColor( Color.PINK );
                break;
            case 4 :
                g2d.setColor( Color.ORANGE );
                break;
            case 5 : 
                g2d.setColor( Color.BLUE );
                break;
            case 6 :
                g2d.setColor( Color.RED );
                break;
            case 7 :
                g2d.setColor( Color.MAGENTA );
                break;
            default :
                g2d.setColor( Color.GRAY );
                break;
        }
        
        g2d.fillRect( x+1, y+1, wd-2, ht-2 );
        g2d.drawRect( x+1, y+1, wd-2, ht-2 );       
    }

}