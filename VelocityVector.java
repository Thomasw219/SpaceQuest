/**
 * @(#)VelocityVector.java
 *
 *
 * @author 
 * @version 1.00 2017/4/28
 */


public class VelocityVector 
{
	
	//Vector data
	private double direction;
	private double magnitude;

    public VelocityVector(double dir, double mag) 
    {
    	direction = dir;
    	magnitude = mag;
    }
    
    public int getVx()
    {
    	return (int) (magnitude * Math.cos(direction));
    }
    
    public int getVy()
    {
    	return - (int) (magnitude * Math.sin(direction));
    }
    
    public double getMagnitude()
    {
    	return magnitude;
    }
    
    public double getDirection()
    {
    	return Math.toDegrees(direction);
    }
    
    public void accelerate(double dir, double mag)
    {
    	double sumVx = (magnitude * Math.cos(direction)) + (mag * Math.cos(dir));
    	double sumVy = (magnitude * Math.sin(direction)) + (mag * Math.sin(dir));
    	magnitude = Math.sqrt(sumVx * sumVx + sumVy * sumVy);
    	if (magnitude == 0)
    		direction = 0;
    	else if (sumVx < 0)
	   		direction = Math.atan(sumVy / sumVx) + Math.PI;
    	else
    		direction = Math.atan(sumVy / sumVx);
    }
    
    public VelocityVector vectorAddition(VelocityVector v)
    {
    	double sumVx = (magnitude * Math.cos(direction)) + (v.getMagnitude() * Math.cos(Math.toDegrees(v.getDirection())));
    	double sumVy = (magnitude * Math.sin(direction)) + (v.getMagnitude() * Math.sin(Math.toDegrees(v.getDirection())));
    	double newMagnitude = Math.sqrt(sumVx * sumVx + sumVy * sumVy);
    	double newDirection;
    	if (magnitude == 0)
    		newDirection = 0;
    	else if (sumVx < 0)
	   		newDirection = Math.atan(sumVy / sumVx) + Math.PI;
    	else
    		newDirection = Math.atan(sumVy / sumVx);
    	return new VelocityVector(newDirection, newMagnitude);
    }
    
    @Override
    public String toString()
    {
    	return String.format("Magnitude: %.1f Direction: %.1f Vx: %d Vy: %d", magnitude, Math.toDegrees(direction), getVx(), getVy());
	}
}