package libPurple;
import edu.wpi.first.wpilibj.Joystick;
public class Joystick3075 extends Joystick 
{
	public Joystick3075(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}
	public static double deadband =0.1;
	public static int power =1;
	
	public double xget()
	{
		return  super.getX();
		
	}
	public double yGet()
	{
		 return super.getY();
	}
	
	
//	public double axisget(int axis)
//	{
//		double value =super.getRawAxis(axis);
//		
//	}
	public double get()
	{
		return 0;
	}
}
