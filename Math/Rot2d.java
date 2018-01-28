package tetris.Math;

import static java.lang.Math.*;

public class Rot2d 
{

    protected static final int X = 0;
    protected static final int Y = 1;
    
    private Rot2d()
    {
        
    }
    
    public static void main ( String[] args )
    {
        int[][] line = new int[4][2];
        for( int i = 2; i < 6; ++i )
        {
            line[i - 2][X] = i;
            line[i - 2][Y] = 3;
        }
        
        rotate2d( line, 2, 90.0 );
        
        int[][] s = new int[4][2];
        
        s[0][X] = 2;
        s[0][Y] = 2;
        s[1][X] = 3;
        s[1][Y] = 2;
        s[2][X] = 3;
        s[2][Y] = 3;
        s[3][X] = 4;
        s[3][Y] = 3;
        
        rotate2d( s, 2, 90.0 );
    }
    
    public static int[][] rotate2d( int[][] d, int center, double angle )
    {
        double radAng = toRadians( angle );
        double s = sin(radAng);
        double c = cos(radAng);

        int centerX = d[center][X];
        int centerY = d[center][Y];
        
        for (int i = 0; i < d.length; ++i) 
        {
            // Translate object to origin;
            d[i][X] = d[i][X] - d[center][X];
            d[i][Y] = d[i][Y] - d[center][Y];
            
            
            // Perform 2D rotation;
            double cx = c * d[i][X];
            double sx = s * d[i][X];
            double cy = c * d[i][Y];
            double sy = s * d[i][Y];

            d[i][X] = (int) Math.round(cx - sy);
            d[i][Y] = (int) Math.round(sx + cy);
           
            
            // Translate object back;
            d[i][X] = d[i][X] + centerX;
            d[i][Y] = d[i][Y] + centerY;            
        }

        return d;
    }
}
