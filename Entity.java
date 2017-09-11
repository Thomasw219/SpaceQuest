/**
 * @(#)Entity.java
 *
 *
 * @author 
 * @version 1.00 2017/4/27
 */

import java.awt.Graphics2D;

public abstract class Entity 
{
	//Class Properties
	public static final int FRICTION = -1;
	
	//Object Properties
	private int x;
	private int y;
	private int collisionRadius;
	VelocityVector velocity;
	
    public Entity(int x, int y, int radius, VelocityVector velocity) 
    {
    	this.x = x;
    	this.y = y;
    	collisionRadius = radius;
    	this.velocity = velocity;
    }
    
    public int getX()
    {
    	return x;
    }
    
    public int getY()
    {
    	return y;
    }
    
    public VelocityVector getVelocity()
    {
    	return velocity;
    }
    
    public int getRadius()
    {
    	return collisionRadius;
    }
    
    public void changeVelocity(double direction, double magnitude)
    {
    	velocity.accelerate(Math.toRadians(direction), magnitude);
    }
    
    public void newVelocity(VelocityVector newVelocity)	
    {
    	velocity = newVelocity;
    }
    
    public void move(boolean wrap)
    {
    	if (wrap)
    	{
		int size = SpaceQuestRunner.FRAME_SIZE;
    		if (x + velocity.getVx() > -1)
    			x = (x + velocity.getVx()) % size;
    		else
    			x = (x + velocity.getVx()) + size;
    		if (y + velocity.getVy() > -1)
    			y = (y + velocity.getVy()) % size;
    		else
    			y = (y + velocity.getVy()) + size;
    	}
    	else
    	{
    		x += velocity.getVx();
    		y += velocity.getVy();
    	}
    }
    
    public void friction()
    {
    	double mag = getVelocity().getMagnitude();
    	if (mag < .9)
    		return;
    	else if (mag < 10)
    		changeVelocity(getVelocity().getDirection() + 180, 1);
    	else if (mag < 20)
    		changeVelocity(getVelocity().getDirection() + 180, 1.5);
    	else if (mag < 30)
    		changeVelocity(getVelocity().getDirection() + 180, 2);
    	else
    		changeVelocity(getVelocity().getDirection() + 180, 3);
    }
        
    public void adjust(int dx, int dy)
    {
    	x += dx;
    	y += dy;
    }
    
    public abstract void draw(Graphics2D gr);
    
}