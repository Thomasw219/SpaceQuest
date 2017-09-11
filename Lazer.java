/**
 * Lazer.java
 * @authors:  Jacob Chmielecki, Amani Duncker, Thomas Wei
 * Periods: 2,3
 * Date: 5/18/2017
 */
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Line2D;
 
public class Lazer extends Ammunition
{
	int size;
	int decay;	
	Line2D.Double line;

    public Lazer(int x, int y, int angle) 
    {
    	super(x, y, 10, new VelocityVector(Math.toRadians(angle), 10));
		size = 10;
    }
    
    @Override
    public void draw(Graphics2D gr)
	{
		double angle = Math.toRadians(getVelocity().getDirection() + 90);
		line = new Line2D.Double(getX(), getY(), getX() + size * Math.sin(angle), getY() + size * Math.cos(angle));
		gr.setColor(Color.white);
		gr.draw(line);		
	}
}