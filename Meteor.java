/**
 * This class makes a meteor with methods for moving in space and seeing if it has hit the ship or been hit by ammunition
 * @authors:  Jacob Chmielecki, Amani Duncker, Thomas Wei
 * Periods: 2,3
 * Date: 5/18/2017
 */

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
 

public class Meteor extends Entity
{
	Ellipse2D meteor;
	private Queue<Ellipse2D> queue;
	int size;
	int max;
	
	/**
	 * This constructor creates a meteor with a size, an initial x coordinate, an initial y coordinate, and a direction
	 * @param size the size
	 * @param x the initial x coordinate
	 * @param y the initial y coordinate
	 * @param angle the initial angle
	 **/
	public Meteor(int size, int x, int y, int angle)
	{
		super(x, y, size, new VelocityVector(Math.toRadians(angle), 5));
		this.size = size;
		meteor = new Ellipse2D.Double(getX(),getY(),size,size);
	}
    
    /**
     * this method draws the meteor
     * @param gr the graphics component
     * */
	@Override
	public void draw(Graphics2D gr)
	{
		gr.setColor(Color.red);
		meteor = new Ellipse2D.Double(getX(),getY(),size,size);
		gr.fill(meteor);	
	}
	
	/**
	 * this method checks if the ship has collided with the meteor
	 * @param ship the ship
	 * @return a boolean whether it collided with the ship
	 * */
	public boolean shipCollided(Ship ship)
	{
		boolean crash = false;
		for(int i = 0; i < 3; i++)
			crash = crash || 
				Math.sqrt((ship.getShip().xpoints[i] - (getX() + size)) * (ship.getShip().xpoints[i] - (getX() + size))
					+ (ship.getShip().ypoints[i] - (getY()+ size)) * (ship.getShip().ypoints[i] - (getY() + size))) <=size;
		
		return crash;
	}
	
	/**
	 * this method checks if the meteor has been hit by ammunition
	 * @param ammu the ammunition
	 * @return whether it's been hit
	 * */
	public boolean hitByAmmo(Ammunition ammu)
	{
		return meteor.contains(ammu.getX(),ammu.getY());
		
	}
}