package libPurple;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;

public class ImageDetection3075 {
	
	private NetworkTable server;
	
	public double targetRatio = 1.75;
	
	public ImageDetection3075()
	{
		server = NetworkTable.getTable("SmartDashboard");
	}
	
	/**
	 * gets the LINE_CORNER array from RoboRealm and converts it to Point array.
	 * @return a Point array of LINE_CORNER
	 */
	public Point[] lineCorner() throws Exception
	{
		if(!server.containsKey("LINE_CORNER"))
		{
			DriverStation.reportError("NOT FOUND****************************", false);
			return null;
		}
		
		double[] vals = server.getNumberArray("LINE_CORNER");
		Point[] res = new Point[vals.length / 2];
		for(int i = 0; i < vals.length; i += 2)
			res[i / 2] = new Point(vals[i], vals[i + 1]);
		return res;
	}
	
	/**
	 * makes a Rectangle object from LINE_CORNER's Point array.
	 * @return a Rectangle object of the LINE_CORNER's points.
	 * @throws Exception 
	 */
	public Rectangle getRectangle() throws Exception
	{
		return new Rectangle(lineCorner());
	}

}
