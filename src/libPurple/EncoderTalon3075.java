package libPurple;

public class EncoderTalon3075 implements Encoder3075{
	
	private CANTalon3075 myTalon;
	private double dpp = 1;
	private double resetPoint = 0;
	private double inverted = 1;
	private static double encoderPulsesPerRotation = 4;
	
	private static double minimumValue = 0.0005;
	
	
	public EncoderTalon3075(CANTalon3075 talon)
	{
		myTalon = talon;
	}
	
	public EncoderTalon3075(CANTalon3075 talon, boolean inverted)
	{
		myTalon = talon;
		this.inverted = inverted ? -1 : 1;
	}
	
	public void setPulsesPerRotation(double pulses)
	{
		encoderPulsesPerRotation = pulses;
	}
	
	
	public void invert(boolean inverted)
	{
		this.inverted = inverted ? -1 : 1;
	}
	
	/**
	 * 
	 * @return Encoder's rate for 100ms.
	 */
	
	public double getRate()
	{
		double returnValue = (myTalon.getSpeed() / dpp) * inverted * 10;
		return Math.abs(returnValue) < minimumValue ? 0 : returnValue;
	}
	
	public double getRawSpeed()
	{
		return myTalon.getSpeed() * inverted;
	}
	
	public double getRawPosition()
	{
		return myTalon.getPosition() * inverted;
	}
	
	
	public double getDistance()
	{
		double returnValue = (myTalon.getPosition() - resetPoint) / dpp * inverted;
		return Math.abs(returnValue) < minimumValue ? 0 : returnValue;
	}
	
	
	public void setDistancePerPulse(double dpp)
	{
		this.dpp = dpp;
	}
	
	
	public void reset()
	{
		resetPoint = myTalon.getPosition();
	}
}
