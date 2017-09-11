 /**
 *  this class makes a missile whith methods for being drawn and inherited methods for movement and collisions
 * */

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
 
public class Missile extends Ammunition
{
	int size;
	int decay;	


   /**
    * this constructor creates a new missile
    * @param x the initial x coordinate
    * @param y the intial y coordinate
    * @param angle the initial direction
    * */
    public Missile(int x, int y, int angle) 
    {
    	super(x, y, 3, new VelocityVector(Math.toRadians(angle), 10));
    	decay = 0;
		size = 15;
    }
    
    /**
     * this method draws the missile
     * @param gr the graphics component
     * */
    @Override
    public void draw(Graphics2D gr)
	{
		double angle1 = Math.toRadians(getVelocity().getDirection() - 90 + 30);
		double angle2 = Math.toRadians(getVelocity().getDirection() - 90 - 30);
		int[] xx = new int[3];
		int[] yy = new int[3];
		xx[0] = (int)getX();
		yy[0] = (int)getY();
		xx[1] = (int)(getX() + size * Math.sin(angle1));
		yy[1] = (int)(getY() + size * Math.cos(angle1));
		xx[2] = (int)(getX() + size * Math.sin(angle2));
		yy[2] = (int)(getY() + size * Math.cos(angle2));
		gr.setColor(Color.red);
		gr.fill(new Polygon(xx,yy,3));
	}
}