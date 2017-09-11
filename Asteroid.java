/**
 * @
 *
 *
 * @author 
 * @version 1.00 2017/4/27
 */

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.geom.Point2D;
 

public class Asteroid extends Entity
{
	Polygon asteroid;
	int size;
	
	public Asteroid(int size, int x, int y,int angle)
	{
		super(x, y, size, new VelocityVector(Math.toRadians(angle), 2));
		asteroid = new Polygon(new int []{(int)(getX() + size),(int)getX() ,(int)(getX() + (size/3)),(int)(getX() + (4 * size) / 3),(int)(getX() + (5*size) / 3),(int)(getX() + size)},
		new int[]{(int)getY() - (2*size)/3,(int)getY(),(int)getY()+(4*size) /3,(int)getY() + size,(int)getY() +(size/3),(int)getY() - (2*size)/3}, 6);
		this.size = size;
	}
	
	@Override
	public void draw(Graphics2D gr)
	{
		gr.setColor(Color.gray);
		asteroid = new Polygon(new int []{(int)(getX() + size),(int)getX() ,(int)(getX() + (size/3)),(int)(getX() + (4 * size) / 3),(int)(getX() + (5*size) / 3),(int)(getX() + size)},
		new int[]{(int)getY() - (2*size)/3,(int)getY(),(int)getY()+(4*size) /3,(int)getY() + size,(int)getY() +(size/3),(int)getY() - (2*size)/3}, 6);
		gr.fill(asteroid);
	}
	
	public int getRadius()
	{
		return size;
	}
	
	public boolean shipCollided(Ship ship)
	{
		for(int i = 0; i < 3; i++)
		{
			int xx = ship.getShip().xpoints[i];
			int yy = ship.getShip().ypoints[i];
			if(asteroid.contains(xx,yy))
				return true;
		}
		return false;
	}
	
	public boolean hitByAmmo(Ammunition ammu)
	{
		return asteroid.contains(ammu.getX(),ammu.getY());
	}

}