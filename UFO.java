/**
 * @(#)UFO.java
 *
 *
 * @author 
 * @version 1.00 2017/5/5
 */
 import java.awt.Graphics2D;
 import java.awt.Color;
 import java.awt.geom.Ellipse2D;


public class UFO extends Entity
{
	
	private static final int size = 20;
	Ellipse2D top;
	Ellipse2D bottom;
	
	private int moveState;

    public UFO(int x, int y) 
    {
    	super(x, y, 10, new VelocityVector(0, 0));
    	top = new Ellipse2D.Double(getX() + 6, getY(),28,15);
		bottom = new Ellipse2D.Double(getX(), getY() + 5,40,20);
    	moveState = 0;
    }
    
    @Override
    public void draw(Graphics2D gr)
    {
    	top = new Ellipse2D.Double(getX() + 6, getY(),28,15);
		bottom = new Ellipse2D.Double(getX(), getY() + 5,40,20);
		gr.setColor(Color.magenta);
		gr.fill(bottom);
		gr.setColor(Color.green);
		gr.fill(top);	
    }
    
    public double getProjectileDirection(int shipX, int shipY)
    {
    	double deltaX = shipX - getX();
    	double deltaY = (shipY - getY()) * -1;
    	double deviation = Math.random() * 45 - 22.5;
//    	System.out.println("deltaX: " + deltaX + " deltaY: " + deltaY);
    	
    	if (deltaX < 0)
    		return Math.toDegrees(Math.atan(deltaY / deltaX) + Math.PI) + deviation;
    	else if (deltaX > 0)
    		return Math.toDegrees(Math.atan(deltaY / deltaX)) + deviation;
    	else if (deltaX == 0 && deltaY == 0)
    		return 0 + deviation;
    	else if (deltaX == 0 && deltaY > 0)
    		return 90 + deviation;
    	else 
    		return 270 + deviation;
    }

    public boolean hitByAmmo(Ammunition ammu)
    {
	return top.contains(ammu.getX(), ammu.getY()) || bottom.contains(ammu.getX(), ammu.getY());
    }
    
    @Override
    public void move(boolean wrap)
    {
    	if (moveState % 100 == 0)
    		newVelocity(new VelocityVector(Math.random() * 2 * Math.PI, 3));
    	moveState++;
    	super.move(wrap);
    }
    
    public boolean shipCollided(Ship ship)
	{
		for(int i = 0; i < 3; i++)
		{
			int xx = ship.getShip().xpoints[i];
			int yy = ship.getShip().ypoints[i];
			if(top.contains(xx, yy) || bottom.contains(xx, yy))
				return true;
		}
		return false;
	}

}