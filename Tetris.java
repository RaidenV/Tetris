package tetris;

import tetris.Listeners.GameEventListener;
import tetris.Listeners.GameLevelListener;
import tetris.Listeners.GamePanelListener;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;


public class Tetris extends JFrame implements GameEventListener, 
    GameLevelListener, GamePanelListener
{

    private final int DEFAULT_TIC = 15;
    
    private JPanel mContent;
    private GamePanel mGamePanel;
    private GameLogic mGameLogic;
    private GameMusic mGameMusic;
    private GameLevel mGameLevel;
    private GameEffects mGameEffects;
    private Timer mGameTimer;
    private ControlPanel mConPan;
    private int mDifficulty = 200;
    private int mCurTic;

    public static void main( String[] args )
    {
        EventQueue.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    Tetris tetris = new Tetris();
                    tetris.setVisible(true);
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }
        });
    }

    public Tetris()
    {
        init();
    }

    private void init()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 750);
        setMinimumSize(new Dimension(400, 750));
        setMaximumSize(new Dimension(400, 750));
        setResizable(false);

        this.setTitle("Tetris");
        this.setBackground(new Color(13, 24, 30));

        ImageIcon img = new ImageIcon("src/tetris/Resources/icons/logo.png");
        this.setIconImage(img.getImage());

        mGameLogic = new GameLogic();
        mGameTimer = new Timer( DEFAULT_TIC, new TimerListener());

        configContent();
        configGamePan();
        loadSoundFx();
        configConPan();
        configGameLvl();
        configListeners();

        this.setFocusable(true);
        this.addKeyListener(new KeyBoardListener());
    }

    private void configContent()
    {
        mContent = new JPanel();
        mContent.setBackground(new Color(13, 24, 30));
        mContent.setLayout(new BorderLayout(10, 10));
        setContentPane(mContent);
    }

    private void configConPan()
    {
        mConPan = new ControlPanel();
        mConPan.setVisible(true);
        mContent.add(mConPan, BorderLayout.EAST);
    }

    private void configGamePan()
    {
        mGamePanel = new GamePanel(mGameLogic.getBoardX(),
                                   mGameLogic.getBoardY());
        mGamePanel.addKeyListener(new KeyBoardListener());
        mGamePanel.setVisible(true);
        mContent.add(mGamePanel, BorderLayout.CENTER);
    }
    
    private void configGameLvl()
    {
        mGameLevel = new GameLevel();
        mDifficulty = mGameLevel.getDifficulty();
        mCurTic = mDifficulty;
    }

    private void loadSoundFx()
    {
        mGameMusic = new GameMusic();
        mGameEffects = new GameEffects();
    }
    
    private void configListeners()
    {
        mGameLevel.addListener( this );
        mGameLevel.addListener( mGamePanel );
        mGameLevel.addListener( mGameEffects );
        mGameLevel.addListener( mGameMusic );
        
        mGameLogic.addListener(mGameMusic);
        mGameLogic.addListener(mGameEffects);
        mGameLogic.addListener(this);
        mGameLogic.addListener(mGamePanel);
        mGamePanel.addListener(this);
    }

    private void resetGame()
    {
        gameOver();
        mConPan.setScore( 0 );
    }

    @Override
    public void rowComplete( int num )
    {
    }

    @Override
    public void gameOver()
    {
        mGameMusic.stopBg();
        mConPan.setStartButton("Start");
        mGameTimer.stop();
        mGameMusic.stopBg();
        mGameLevel.reset();
        mGameLogic.reset();
    }

    @Override
    public void gameStart()
    {
        mConPan.disableBtns( false );
    }

    @Override
    public void levelComplete( int lvl )
    {
        mGameTimer.stop();
        mGameLevel.increaseLevel();
        mDifficulty = mGameLevel.getDifficulty();
        mConPan.disableBtns( true );
    }

    @Override
    public void titleComplete()
    {
        mGameLogic.reset();
        mGameMusic.stopBg();
        mGameTimer.start();
    }

    public class ControlPanel extends JPanel
    {

        private JButton mStartStop;
        private JButton mReset;
        private JTextField mScoreBoard;
        private GamePanel mNextPiece;
        private final int NEXT_PCE_SZ = 6;

        public ControlPanel()
        {
            initControlPanel();
            this.setPreferredSize(new Dimension(100, 500));
        }

        private void initControlPanel()
        {
            ButtonListener bList = new ButtonListener();

            this.setBackground(new Color(13, 24, 30));

            // Add start/stop button;
            mStartStop = new JButton("Start");
            mStartStop.addActionListener(bList);
            mStartStop.addKeyListener(new KeyBoardListener());

            // Add reset button;
            mReset = new JButton("Reset");
            mReset.addActionListener(bList);

            // Add the next-piece panel;
            mNextPiece = new GamePanel(NEXT_PCE_SZ, NEXT_PCE_SZ);
            mNextPiece.setPreferredSize(new Dimension(100, 100));
            mNextPiece.setMinimumSize(new Dimension(100, 100));
            mNextPiece.addKeyListener(new KeyBoardListener());
            mNextPiece.setVisible(true);

            // Add the scoreboard;
            mScoreBoard = new JTextField();
            mScoreBoard.setHorizontalAlignment(JTextField.CENTER);
            mScoreBoard.setEditable(false);
            mScoreBoard.setText("0");
            mScoreBoard.addKeyListener(new KeyBoardListener());

            // Using a GBL will allow for better control over components;
            this.setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            // Add constraints for spacing and setting the GBL towards the top;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.anchor = GridBagConstraints.NORTH;

            // Add all pieces
            this.add(mStartStop, gbc);
            this.add(mReset, gbc);
            this.add(mNextPiece, gbc);
            this.add(mScoreBoard, gbc);

            // Add a phantom components for pushing everything else upwards;
            // This seems like a kludge, but I'm simply tired of dealing
            // with manually configuring all of this stuff.
            gbc.weightx = 1;
            gbc.weighty = 1;
            this.add(new JLabel(" "), gbc);

            this.setFocusable(true);
            this.addKeyListener(new KeyBoardListener());
        }

        public void setScore( int s )
        {
            mScoreBoard.setText(Integer.toString(s));
        }

        public void setStartButton( String str )
        {
            mStartStop.setText(str);
        }

        public void setNextPiece( int[][] b )
        {
            mNextPiece.setBoard(b);
            mNextPiece.revalidate();
            mNextPiece.repaint();
        }

        public int getNextPieceSz()
        {
            return NEXT_PCE_SZ;
        }

        private void disableBtns( boolean b )
        {
            mStartStop.setEnabled( !b );
            mReset.setEnabled( !b );
        }
    }

    public class ButtonListener implements ActionListener
    {

        @Override
        public void actionPerformed( ActionEvent e )
        {
            switch ( e.getActionCommand() )
            {
                case "Start":
                {
                    JButton jb = ( JButton ) ( e.getSource() );
                    jb.setText("Stop");
                    mGameTimer.start();
                    break;
                }
                case "Stop":
                {
                    JButton jb = ( JButton ) ( e.getSource() );
                    jb.setText("Start");
                    mGameTimer.stop();
                    mGameMusic.stopBg();
                    break;
                }
                case "Reset":
                    resetGame();
                    mConPan.setStartButton("Start");
                    mGamePanel.setBoard(mGameLogic.getBoard());
                    mGamePanel.repaint();
                    mGameTimer.stop();
                    mGameLogic.reset();
                    break;
                default:
                    break;
            }
        }
    }

    public class TimerListener implements ActionListener
    {

        @Override
        public void actionPerformed( ActionEvent e )
        {
            mCurTic++;
            if ( mCurTic >= mDifficulty )
            {
                mGameLogic.update();
                mGameLevel.checkLevel( mGameLogic.getScore() );
                int sz = mConPan.getNextPieceSz();
                mConPan.setNextPiece(mGameLogic.getNextPiece(sz, sz));
                mConPan.setScore(mGameLogic.getScore());
                mCurTic = 0;
            }

            mGamePanel.setBoard(mGameLogic.getBoard());
            mGamePanel.repaint();
        }
    }

    public class KeyBoardListener implements KeyListener
    {

        @Override
        public void keyTyped( KeyEvent e )
        {
            this.keyPressed(e);
        }

        @Override
        public void keyPressed( KeyEvent e )
        {

            if ( e.getKeyCode() == KeyEvent.VK_RIGHT )
            {
                mGameLogic.moveRight();
            }

            if ( e.getKeyCode() == KeyEvent.VK_LEFT )
            {
                mGameLogic.moveLeft();
            }

            if ( e.getKeyCode() == KeyEvent.VK_UP )
            {
                mGameLogic.rotateRight();
            }

            if ( e.getKeyCode() == KeyEvent.VK_Z )
            {
                mGameLogic.rotateLeft();
            }

            if ( e.getKeyCode() == KeyEvent.VK_DOWN )
            {
                mGameLogic.moveDown();
            }

            if ( e.getKeyCode() == KeyEvent.VK_C )
            {
                mGameLogic.dropPiece();
            }

        }

        @Override
        public void keyReleased( KeyEvent e )
        {

        }

    }
}
