/**
 * @(#)Ammunition.java
 *
 *
 * @author 
 * @version 1.00 2017/5/1
 */

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.geom.Point2D;
 
public class Ammunition extends Entity
{
	int size;
	int decay;
	Ellipse2D circle;

    public Ammunition(int x, int y, int angle) 
    {
    	super(x, y, 3, new VelocityVector(Math.toRadians(angle), 10));
    	decay = 0;
		this.size = 3;
    }
    
    public Ammunition(int x, int y, int size, VelocityVector vec)
    {
    	super(x, y, size, vec);
    	decay = 0;
    	this.size = size;
    }
    
    public boolean exists()
    {
    	
    	if (decay < 50)
    	{
    		decay++;
    		return true;
    	}
    	else	
    		return false;
    }
    
    @Override
    public void draw(Graphics2D gr)
	{
		circle = new Ellipse2D.Double(getX(), getY(), size * 2, size * 2);
		gr.setColor(Color.white);
		gr.fill(circle);		
	}

	public boolean shipCollided(Ship ship)
	{
		if (ship.getShip().contains(getX(), getY()))
			return true;
		else
			return false;
	}
}