package libPurple;

public class Point {
	private double x;
	private double y;
	public Point(double x, double y) {
		super();
		this.x = x;
		this.y = y;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	/**
	 * leftest = http://www.urbandictionary.com/define.php?term=leftest
	 * @param points
	 * @return the leftest point
	 */
	public static Point leftestPoint(Point[] points)
	{
		Point highest = points[0];
		for(Point p : points)
		{
			if(p.getX() < highest.getX())
				highest = p;
		}
		return highest;
	}
	
	public static double distance(Point p1, Point p2)
	{
		return Math.sqrt(Math.pow(p1.getY() - p2.getY(), 2) + Math.pow(p1.getX() - p2.getX(), 2));
	}
	
	public String toString()
	{
		return "(" + x + ", " + y + ")";
	}
	
}
