package libPurple;

import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SpeedController;

public class PIDOutput3075 implements PIDOutput{
	
	private SpeedController3075 motor;

	public PIDOutput3075(SpeedController3075 motor) {
		super();
		this.motor = motor;
	}


	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		
		motor.set(motor.get() + output);
		
		
	}

}
