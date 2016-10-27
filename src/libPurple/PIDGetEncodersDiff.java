package libPurple;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;

public class PIDGetEncodersDiff implements PIDSource{

	private PIDController left;
	private PIDController right;
	
	double distanceLeft;
	double distanceRight;
	
	public PIDGetEncodersDiff(PIDController left, PIDController right)
	{
		this.left = left;
		this.right = right;
		distanceLeft = -left.getError();
		distanceRight = -right.getError();
	}
	
	public double pidGet() {
		
		return left.getError()/distanceLeft - right.getError()/distanceRight;
	}

	
	// TODO complete this fuck
	public void setPIDSourceType(PIDSourceType pidSource) {
		// TODO Auto-generated method stub
		
	}

	
	// TODO complete this fuck
	public PIDSourceType getPIDSourceType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
