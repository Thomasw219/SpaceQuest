/**
 * @(#)ShipMaker.java
 *
 *
 * @author 
 * @version 1.00 2017/4/27
 */

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Polygon;
import java.awt.Color;

public class Ship extends Entity
{
	Polygon[] positions;
	Polygon current;
	boolean invincible;
	boolean cooldown;
	int nextFire;
	int state;
	int angle;
	int[] x;
	int[] y;

    public Ship() 
    {
    	super(SpaceQuestRunner.FRAME_SIZE / 2, SpaceQuestRunner.FRAME_SIZE / 2, 0, new VelocityVector(0,0));
    	x = new int[36];
    	y = new int[36];
    	for(int a = 0; a < 36; a += 1)
    	{
    		x[a] = (int) (Math.sin(10.0 * a * Math.PI / 180) * 20);
    		y[a] = (int) (Math.cos(10.0 * a * Math.PI / 180) * 20);    		
    	}
    	
    	positions = new Polygon[36];
    	for(int i = 0; i < 36; i++)
    	{
    		int[] xp = {x[(i + 15 + 9) % 36] + getX(), x[(i + 9) % 36] + getX(), x[(i + 21 + 9) % 36] + getX()};
    		int[] yp = {y[(i + 15 + 9) % 36] + getY(), y[(i + 9) % 36]  + getY(), y[(i + 21 + 9) % 36] + getY()};
    		positions[i] = new Polygon(xp,yp,3);
    	}
    	angle = 0;
    	current = positions[0];
    	invincible = true;
    	cooldown = false;
    	state = 0;
    }
    
    public Polygon getShip()
    {
    	return current;
    }
    
    @Override
    public void draw(Graphics2D gr)
	{
		gr.setColor(Color.WHITE);
		for(int i = 0; i < 36; i++)
    	{
    		int[] xp = {x[(i + 15 + 9) % 36] + getX(), x[(i + 9) % 36] + getX(), x[(i + 21 + 9) % 36] + getX()};
    		int[] yp = {y[(i + 15 + 9) % 36] + getY(), y[(i + 9) % 36]  + getY(), y[(i + 21 + 9) % 36] + getY()};
    		positions[i] = new Polygon(xp,yp,3);
    	}
    	current = positions[angle / 10];
		gr.draw(current);	
	}
    
    public int getAngle()
    {
    	return angle;
    }
    
    public void rotateRight()
    {
    	angle+= -10;
    	if(angle < 0)
    		angle+= 360;
    	current = positions[angle / 10];
    }
    
    public void rotateLeft()
    {
    	angle+= 10;
    	if(angle >= 360)
    		angle+= -360;
    	current = positions[angle / 10];
    }
    
    public void thrust()
    {
    	changeVelocity(angle, 3);
    }
    
    public void reverseThrust()
    {
    	changeVelocity((angle + 180) % 360, 3);
    }
    
    public int getFrontX()
	{
		return getX();
	}
	
	public int getFrontY()
	{
		return getY();
	}
	
	public boolean isInvincible()
	{
		return invincible;
	}
	
	public void setCooldown(int cooltime)
	{
		cooldown = true;
		nextFire = state + cooltime;
	}
	
	public boolean isCooling()
	{
		return cooldown;
	}
	
	public int getState()
	{
		return state;
	}
	
	@Override
	public void move(boolean wrap)
	{
		if (state > 100)
			invincible = false;
		if (state >= nextFire)
			cooldown = false;
		state++;
		super.move(wrap);
	}
    
}