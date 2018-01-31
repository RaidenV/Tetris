package tetris.Listeners;

public interface GameEventListener
{
    void rowComplete( int num );
    void gameStart();
    void gameOver();
}
