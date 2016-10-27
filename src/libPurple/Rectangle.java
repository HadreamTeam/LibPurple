package libPurple;

import java.util.ArrayList;
import java.util.Arrays;

import org.usfirst.frc.team3075.robot.Constants;
import org.usfirst.frc.team3075.robot.Robot;

public class Rectangle {
	
	private Point topRight, topLeft, bottomRight, bottomLeft;
	
	private double targetRatio = 1;

	public Rectangle(Point p[])
	{
		assert p.length < 4;
		
		topLeft = Point.leftestPoint(p);
		ArrayList<Point> tempoints = new ArrayList<Point>(Arrays.asList(p));
		tempoints.remove(topLeft);
		
		bottomLeft = Point.leftestPoint(Arrays.copyOf(tempoints.toArray(), tempoints.toArray().length, Point[].class));
		
		tempoints.remove(bottomLeft);
		topRight = tempoints.get(0);
		bottomRight = tempoints.get(1);
		
		sortTopAndBottom(topLeft, bottomLeft);
		sortTopAndBottom(topRight, bottomRight);
		
	}
	
	// doesn't matter if really top and bottom, name of the original variable's
	// name is important.
	public void sortTopAndBottom(Point top, Point bottom)
	{
		if(top.getY() > bottom.getY())
		{
			Point temp = top;
			top = bottom;
			bottom = temp;
		}
	}
	
	/**
	 * calculates the quadrate's center by average of points.
	 * @return the point of the quadrate's center
	 */
	public Point getCenter()
	{
		double avgX = (topLeft.getX() + topRight.getX() + bottomLeft.getX() + bottomRight.getX()) / 4;
		double avgY = (topLeft.getY() + topRight.getY() + bottomLeft.getY() + bottomRight.getY()) / 4;
		return new Point(avgX, avgY);
	}
	
	/**
	 * calculates the average length of the side edges (height).
	 * @return the average length of the side edges.
	 */
	public double getHeight()
	{
		return (Point.distance(topLeft, bottomLeft) + Point.distance(topRight, bottomRight))/2;
	}
	
	public double getWidth()
	{
		return (Point.distance(topLeft, topRight) + Point.distance(bottomLeft, bottomRight))/2;
	}
	
	
	//TODO fix this some day :/
	public double getAngle()
	{
		double x = getCenter().getX();
		double absAngle = x / (Math.toRadians(Constants.FOV) * Constants.imageWidth);
    	double realAngle = absAngle - Math.toRadians(Constants.FOV)/2;
    	realAngle = Math.toDegrees(realAngle);
    	return realAngle;

	}
	
	public String toString()
	{
		return topLeft + "_____" + topRight + "\n|\n|\n|\n" + bottomLeft + "_____" + bottomRight + "\n";
	}
	
	public double getTargetRatio() {
		return targetRatio;
	}

	public void setTargetRatio(double targetRatio) {
		this.targetRatio = targetRatio;
	}
}
