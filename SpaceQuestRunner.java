/**
 * @(#)SpaceQuest.java
 *
 *
 * @author 
 * @version 1.00 2017/4/25
 */

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.OverlayLayout;
import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.io.*;

public class SpaceQuestRunner extends JFrame
{

	//Constants
	public static final int FRAME_SIZE = 1000;
	private static final int OFFSET = 50;
	
	//Jframe elements
	private JPanel start;
	private JTextField nameField;
	private OverlayLayout layout;
	private KeyListener gameListener;
	private KeyListener enterListener;
	
	//Gamestate
	private SpaceQuest game;
	private Timer gameTimer;
	private Timer rotateTimer;
	private Timer thrustTimer;
   	private HashSet<Integer> keyCodes;
	private boolean inGame;
	
    public SpaceQuestRunner() 
    {
    	//Initial Condidtions
    	inGame = false;
    	
    	//Jframe setup
    	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Space Quest");
		setSize(FRAME_SIZE, FRAME_SIZE);
		setLocation(OFFSET, OFFSET);
		OverlayLayout layout = new OverlayLayout(getContentPane());
		setLayout(layout);
		setResizable(false);
		
		//Navigate to Start Screen
		toStartScreen();
    }
    
    //Main
    public static void main(String[] args)
	{
		SpaceQuestRunner run = new SpaceQuestRunner();
	}
    
    private void toStartScreen()
    {
    	start = new StartScreen();
    	add(start);
    	setVisible(true);
    	start.addMouseListener(new MouseAdapter() { 
            public void mousePressed(MouseEvent me) 
            { 
            	System.out.println("clicked");
            	newGame();
            } 
        });
    }
	
	//Instantiates the Game
	private void newGame()
	{	
		remove(start);
		inGame = true;
		game = new SpaceQuest(true);
		keyCodes = new HashSet<Integer>();
		add(game);
		addInputConversion();
		requestFocus();
		setVisible(true);
		startGame();
	}
	
	//Constructs and adds KeyListener
	private void addInputConversion()
	{
		//Reads key inputs and translates them to changes in ship behavior
		class KeyInterpreter implements KeyListener
    	{
    		@Override
    		public void keyTyped(KeyEvent e){}
    		
   	 		@Override
   	 		public void keyPressed(KeyEvent e)
   	 		{
    			int keyCode = e.getKeyCode();
    			switch(keyCode)
    			{ 
        			case KeyEvent.VK_UP:
         	  		 	keyCodes.add(keyCode);
         	  		 	break;
        			case KeyEvent.VK_DOWN:
         		   		keyCodes.add(keyCode);
          		  		break;
        			case KeyEvent.VK_LEFT:
            			keyCodes.add(keyCode);
            			break;
        			case KeyEvent.VK_RIGHT :
           				keyCodes.add(keyCode);
            			break;
            		case KeyEvent.VK_SPACE :
           				keyCodes.add(keyCode);
            			break;
            		case KeyEvent.VK_X :
            			game.cycle();
            			break;
            		case KeyEvent.VK_E :
            			game.toggleWrap();
            			break;
            	}
    		}
    
    		@Override
    		public void keyReleased(KeyEvent e)
    		{
    			int keyCode = e.getKeyCode();
    			switch(keyCode) 
    			{ 
        			case KeyEvent.VK_UP:
         	  		 	keyCodes.remove(keyCode);
         	  		 	break;
        			case KeyEvent.VK_DOWN:
         		   		keyCodes.remove(keyCode);
          		  		break;
        			case KeyEvent.VK_LEFT:
            			keyCodes.remove(keyCode);
            			break;
        			case KeyEvent.VK_RIGHT :
           				keyCodes.remove(keyCode);
            			break;
            		case KeyEvent.VK_SPACE :
           				keyCodes.remove(keyCode);
            			break;
     			}
    		}
    	}
    	addKeyListener(new KeyInterpreter());
	}
	
	//Puts game into motion (Game right now is on 10 miliseconds per step so 100 frames/second?)
	private void startGame()
	{
		gameTimer = new Timer(10, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if (inGame)
					inGame = game.step();
				else
					gameOver();
			}
		});
		rotateTimer = new Timer(24, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for (int key: keyCodes)
				{
					if (key == KeyEvent.VK_LEFT)
						game.left();
					if (key == KeyEvent.VK_RIGHT)
						game.right();
					if (key == KeyEvent.VK_SPACE)
						game.fire();
				}
			}
		});
		thrustTimer = new Timer(80, new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for (int key: keyCodes)
				{
					if (key == KeyEvent.VK_UP)
						game.up();
					if (key == KeyEvent.VK_DOWN)
						game.down();
				}
			}
		});
		rotateTimer.start();
		thrustTimer.start();
		gameTimer.start();
	}
	
	private void gameOver()
	{
		inGame = false;
		System.out.println("Gameover");
		gameTimer.stop();
		rotateTimer.stop();
		thrustTimer.stop();
		removeKeyListener(gameListener);
		
		nameField = new JTextField();
		nameField.setMaximumSize(new Dimension(200, 30));
		add(nameField);
		nameField.requestFocus();
		setVisible(true);
		enterListener = new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e){}
			
			@Override
			public void keyPressed(KeyEvent e)
			{
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					String name = nameField.getText();
					try{game.saveScores(name);}
					catch(IOException exception){System.out.println("saveScores(name) IOException");}
					resetGame();
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e){}
		};
		
		nameField.addKeyListener(enterListener);
	}
	
	private void resetGame()
	{
		nameField.removeKeyListener(enterListener);
		remove(nameField);
		remove(game);
		game = null;
		toStartScreen();
	}
	
}