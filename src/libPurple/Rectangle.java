package libPurple;

public class Rectangle {
	
	private Point topRight, topLeft, bottomRight, bottomLeft;
	
	public Rectangle(Point p[])
	{
		assert p.length < 4;
		
		topLeft = p[0];
		topRight = p[1];
		
		if(p[2].getY() < topRight.getY() || p[2].getY() < topLeft.getY())
		{
			if(topRight.getY() < topLeft.getY())
			{
				bottomLeft = topLeft;
				topLeft = p[2];
			}
			
			else
			{
				bottomLeft = p[2];
			}
		}
		
		
		
		
	}
}
