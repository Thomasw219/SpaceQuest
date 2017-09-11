/**
 * @(#)SpaceQuestComponent.java
 *
 *
 * @author 
 * @version 1.00 2017/4/26
 */

import java.lang.Runnable;
import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.*;

public class SpaceQuest extends JComponent
{
	//Game objects
	private ArrayList<Ammunition> ammo;
	private ArrayList<Entity> hazards;
	private Ship ship;

	//Gamestate Properties
	private int lives;
	private int count;
	private boolean wrap;
	private boolean run;
	private boolean ufo;
	private int score;
	
	//0 = missles, 1 = gun, 2 = lazer
	private int ammoState;
	private int[] ammoAmount;
	
	//Saving
	List<Score> scores;

    public SpaceQuest(boolean wrap)
    {
    		ammo = new ArrayList<Ammunition>();
    		hazards = new ArrayList<Entity>();
    		ship = new Ship();
    		count = 0;
    		this.wrap = wrap;
			run = true;
			ufo = false;
			lives = 3;
			ammoState = 2;
			ammoAmount = new int[2];
			ammoAmount[0] = 3;
			ammoAmount[1] = 30;
			
    		try
    		{
    			getScores();
    			Collections.sort(scores, Collections.reverseOrder());
    		}
    		catch (IOException e)
    		{
    			scores = new ArrayList<Score>();
    			System.out.println("No Previous Scores");
    		}
    
    }
    
    @Override
    public void paintComponent(Graphics g)
    {
    	Graphics2D g2 = (Graphics2D) g;
    	drawGame(g2);
    	drawGameInfo(g2);
    }
    
    public boolean step()
    {
    	if (run)	
    	{
    		generatehazards();
			advanceEntities();
			handleCollisions();
    		repaint();
    		count++;
    	}	
    	return run;
    }
    
    public void up()
    {ship.thrust();}
    
    public void down()
    {ship.reverseThrust();}
    
    public void right()
    {ship.rotateRight();}
    
    public void left()
    {ship.rotateLeft();}
    
    public void cycle()
    {ammoState = (ammoState + 1) % 3;}
    
    public void toggleWrap()
    {wrap = !wrap;}
    
    public void fire()
    {
    	if (!ship.isCooling())
    	{
    		if (ammoState == 2)
    		{
    			ammo.add(new Lazer(ship.getFrontX(), ship.getFrontY(), ship.getAngle()));
    			ship.setCooldown(30);
    		}
    		else if (ammoState == 1 && ammoAmount[ammoState] > 0)
    		{
    			ammo.add(new Ammunition(ship.getFrontX(), ship.getFrontY(), ship.getAngle()));
    			ship.setCooldown(5);
    			ammoAmount[ammoState]--; 
    		}	
    		else if (ammoState == 0 && ammoAmount[ammoState] > 0)
    		{
    			ammo.add(new Missile(ship.getFrontX(), ship.getFrontY(), ship.getAngle()));
    			ship.setCooldown(30);
    			ammoAmount[ammoState]--; 
    		}	
    		else if (ammoAmount[ammoState] <= 0)
    			ammoState = 2;
    	}
    }
    
    public void saveScores(String name) throws IOException
	{
		Score newScore = new Score(name, score);
		scores.add(newScore);
		
		FileOutputStream fos = new FileOutputStream("scores.tmp");
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		
		for (Score value : scores)
		{
			oos.writeObject(value);
		}
		
		System.out.println("Score Saved - " + newScore);
		
		Collections.sort(scores, Collections.reverseOrder());
	}
    
    public void getScores() throws IOException
	{
    	FileInputStream fis = new FileInputStream("scores.tmp");
    	ObjectInputStream ois = new ObjectInputStream(fis);
		List<Score> scoreList = new ArrayList<Score>();
		try
		{
			while (true)
			{
				Score score = (Score) ois.readObject();
				scoreList.add(score);
			}
		}
		catch (EOFException exc){}
		catch (ClassNotFoundException exc)
		{
			System.out.println("Class not found exception");
		}
		scores = scoreList;
	}
    
    private void generatehazards()
    {
    	int pos = (int) (Math.random() * 1000);
    	if (hazards.size() < 8)
    	{
			int angle = (int) (Math.random() * 90);
			if(count % 400 == 0)
			{
				hazards.add(new Asteroid(50, pos, 1000, angle + 225));
				hazards.add(new Meteor(30,pos,800,angle + 50));
			}	
			if(count % 400 == 100)
			{
				hazards.add(new Asteroid(50, 0, pos, angle + 315));
				//hazards.add(new Meteor(30,0,pos,angle + 260));
			}	
			if(count % 400 == 200)
				hazards.add(new Asteroid(50, pos, 0, angle + 405));
			if(count % 400 == 300)
				hazards.add(new Asteroid(50, 1000, pos, angle + 495));
			
    	}
    	if (ufo == false && count % 800 == 301)
    	{
    		hazards.add(new UFO(pos, pos));
    		ufo = true;
    	}
			
    }
    
    private void advanceEntities()
    {
    	ship.move(wrap);
    	int dx = 0;
    	int dy = 0;
    	
    	//Active for procedural generation only
    	if (!wrap)
    	{
		int size = SpaceQuestRunner.FRAME_SIZE;
    		if (ship.getX() > size - 300)
    			dx = size - 300 - ship.getX();
    		else if (ship.getX() < 300)
    			dx = 300 - ship.getX();
    		if (ship.getY() > size - 300)
    			dy = size - 300 - ship.getY();
    		else if (ship.getY() < 300)
    			dy = 300 - ship.getY();
    		ship.adjust(dx, dy);
    		
    		for(int k = hazards.size() - 1; k > -1; k--)
				if(Math.abs(hazards.get(k).getX() - ship.getX()) > 1000|| Math.abs(hazards.get(k).getY() - ship.getY()) > 1000)
					hazards.remove(k);	
    	}
   	
    	int numHazards = hazards.size();
    	for (int i = 0; i < numHazards; i++)
    	{
    		Entity e = hazards.get(i);
    		e.move(wrap);
    		e.adjust(dx, dy);    		
    		if (e instanceof UFO)
    		{
//    			if (count % 4 == 0)
//    				e.friction();
    			if (count % 100 == 0)
    			{
//    				System.out.println(((UFO) e).getProjectileDirection(ship.getX(), ship.getY()));
    				hazards.add(new Ammunition(e.getX(), e.getY(), (int) ((UFO) e).getProjectileDirection(ship.getX(), ship.getY())));
    			}
    		}
    		if (e instanceof Ammunition)
    			if (!((Ammunition) e).exists())
    			{
    				hazards.remove(i);
    				numHazards--;
    				i--;
    			}
    	}
    	
    	for (int i = ammo.size() - 1; i > -1; i--)
    	{
    		Ammunition a = ammo.get(i);
    		if (!a.exists())
    			ammo.remove(i);
    		else
    		{
    			a.move(wrap);
    			a.adjust(dx, dy);
    		}
    	}
    			
    	if (count % 4 == 0)
			ship.friction();
    }
    
    private void handleCollisions()
    {
    	for (int i = 0; i < hazards.size(); i++)
    	{
    		if (hazards.get(i) instanceof Asteroid && ((Asteroid) hazards.get(i)).shipCollided(ship) && !ship.isInvincible())
    			resetShip();
    		else if (hazards.get(i) instanceof Meteor && ((Meteor) hazards.get(i)).shipCollided(ship) && !ship.isInvincible())
    			resetShip();
    		else if (hazards.get(i) instanceof UFO && ((UFO) hazards.get(i)).shipCollided(ship) && !ship.isInvincible())
    			resetShip();
    		else if (hazards.get(i) instanceof Ammunition && ((Ammunition) hazards.get(i)).shipCollided(ship) && !ship.isInvincible())
    			resetShip();
    		for (int j = 0; j < ammo.size(); j++)
    		{
    			if (hazards.get(i) instanceof Asteroid)
    			{
    				boolean go = false;
    				try {go = ((Asteroid) hazards.get(i)).hitByAmmo(ammo.get(j));}
    				catch (NullPointerException e)
    				{
    					System.out.println("hazards.size(): " + hazards.size() + " i: " + i + " ammo.size(): " + ammo.size() + " j: " + j);
    					e.printStackTrace();
    				}
    				if (go)
    				{
    					if(ammo.get(j) instanceof Missile)
    					{
        					Asteroid a = (Asteroid) hazards.get(i);
        					ammo.remove(j);
        					j--;
        					hazards.remove(i);
        					i--;
        					if(a.getRadius() == 50)
    						{
        						score += 300;
    						}
    						else if(a.getRadius() == 30)
    						{
    							score += 200;
    						}
    						else if (a.getRadius() == 20)
    						{
    							score += 100;
    						}
    						break;
    						
    					}
    					else
    					{
	    					score += 100;
	    					Asteroid a = (Asteroid) hazards.get(i);
	    					ammo.remove(j);
	    					j--;
	    					hazards.remove(i);
	    					i--;
	    					if(a.getRadius() == 50)
							{
								hazards.add(new Asteroid(30, a.getX() + 15, a.getY() + 15, (int) a.getVelocity().getDirection() + 45));
								hazards.add(new Asteroid(30, a.getX() + 15, a.getY() + 15, (int) a.getVelocity().getDirection() - 45));
							}
							else if(a.getRadius() == 30)
							{
								hazards.add(new Asteroid(20, a.getX() + 15, a.getY() + 15, (int) a.getVelocity().getDirection() + 45));
								hazards.add(new Asteroid(20, a.getX() + 15, a.getY() + 15, (int) a.getVelocity().getDirection() - 45));
							}
							else if (a.getRadius() == 20)
							{}
							break;	
    					}	
    				}
    			}
    			else if (hazards.get(i) instanceof Meteor)
    			{
    				boolean go = false;
    				try {go = ((Meteor) hazards.get(i)).hitByAmmo(ammo.get(j));}
    				catch (NullPointerException e)
    				{
    					System.out.println("hazards.size(): " + hazards.size() + " i: " + i + " ammo.size(): " + ammo.size() + " j: " + j);
    					e.printStackTrace();
    				}
    				if (go)
    				{
    					score += 300;
						ammo.remove(j);
						j--;
						hazards.remove(i);
    					i--;
    					break;
    				}
    			}
				else if (hazards.get(i) instanceof UFO) 
				{
					boolean go = false;
    				try {go = ((UFO) hazards.get(i)).hitByAmmo(ammo.get(j));}
    				catch (NullPointerException e)
    				{
    					System.out.println("hazards.size(): " + hazards.size() + " i: " + i + " ammo.size(): " + ammo.size() + " j: " + j);
    					e.printStackTrace();
    				}
    				if (go)
    				{
    					score += 500;
						ammo.remove(j);
						j--;
						hazards.remove(i);
    					i--;
    					ufo = false;
    					ammoAmount[0] += 3; 
    					ammoAmount[1] += 30;
    					break;
    				}
    			}
    		}						
    	}
    	if (lives < 1)
    		run = false;
    }
    
    private void resetShip()
    {
    	lives--;
    	ship = new Ship();
    	ammoAmount[0] = 3;
		ammoAmount[1] = 30;
    }
    
    private void drawGame(Graphics2D g2)
    {
    	g2.setColor(Color.BLACK);
    	g2.fillRect(0, 0, 1000, 1000);
    	
    	for (Entity e: ammo)
    		e.draw(g2);
    	for (Entity e: hazards)
    		e.draw(g2);
    	if (!ship.isInvincible() || count % 10 < 5)
    		ship.draw(g2);
    }
    
    private void drawGameInfo(Graphics2D g2)
    {
    	if (run)
    	{
	    	g2.setColor(Color.WHITE);
	    	g2.drawString(ship.getVelocity().toString(), 15, 15);
	    	g2.drawString("X: " + ship.getX() + " Y: " + ship.getY() + " Angle: " + ship.getAngle(), 15, 30);
	    	
	    	g2.setColor(Color.yellow);
			Font font = new Font("Verdana", Font.BOLD, 20);
	  		g2.setFont(font);
			g2.drawString("Lives: " + lives, 15, 50);
			g2.drawString("Score: " + score, 15, 70);	
			if (ammoState == 2)
				g2.setColor(Color.RED);
			g2.drawString("Lazer: Infinite", 15, 925);
			g2.setColor(Color.yellow);
			if (ammoState == 0)
				g2.setColor(Color.RED);
			g2.drawString("Missile: " + ammoAmount[0], 15, 945);
			g2.setColor(Color.yellow);
			if (ammoState == 1)
				g2.setColor(Color.RED);
			g2.drawString("Machine Gun: " + ammoAmount[1], 15, 965);
    	}
		else
    	{
    		g2.setColor(Color.yellow);
  			g2.setFont(new Font("Verdana", Font.BOLD, 30));
			g2.drawString("Game Over", 402, 395);
			g2.setFont(new Font("Courier", Font.BOLD, 20));
			for (int i = 0; i < scores.size() && i < 10; i++)
				g2.drawString(scores.get(i).toString(), 390, 510 + i * 20);
    	}
    }
 
}