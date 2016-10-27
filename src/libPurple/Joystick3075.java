package libPurple;
import edu.wpi.first.wpilibj.Joystick;
public class Joystick3075 extends Joystick 
{
	public Joystick3075(int port) {
		super(port);
		// TODO Auto-generated constructor stub
	}
	public static double deadband = 0;
	public static int power = 1;
	
	public double xGet()
	{
		return Math.pow(utils.deadband(super.getX(), deadband), power);
		
	}
	public double yGet()
	{
		return Math.pow(utils.deadband(super.getY(), deadband), power);
	}
	
	public static double getDeadband() {
		return deadband;
	}
	
	public static void setDeadband(double deadband) {
		Joystick3075.deadband = deadband;
	}
	
	public static int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		Joystick3075.power = power;
	}
}
