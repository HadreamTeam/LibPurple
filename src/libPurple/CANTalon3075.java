package libPurple;

import edu.wpi.first.wpilibj.CANTalon;

public class CANTalon3075 extends CANTalon implements SpeedController3075 {
    
    private double motorBound = 0;
    private int inverted = 1;
    private CANTalon3075 slave;
	
	public CANTalon3075(int channel)
	{
		super(channel);
	}
	
	public CANTalon3075(int channel, boolean inverted)
	{
		super(channel);
		super.reverseOutput(inverted);
		this.inverted = inverted ? -1 : 1;
	}
	
	public CANTalon3075(int channel, CANTalon3075 master)
	{
		super(channel);
		super.changeControlMode(TalonControlMode.Follower);
		super.set(master.getDeviceID());
	}
	
	public CANTalon3075(int channel, CANTalon3075 master, boolean inverted)
	{
		super(channel);
		super.changeControlMode(TalonControlMode.Follower);
		super.reverseOutput(inverted);
		this.inverted = inverted ? -1 : 1;
		super.set(master.getDeviceID());
	}
	
	public void setSpeed(double speed)
	{
		super.set(utils.motorBound(speed, motorBound));
		if(slave != null)
    		slave.set(speed);
	}
	
	@Override
	public void set(double outputValue) {
		// TODO Auto-generated method stub
		super.set(outputValue * inverted);
		if(slave != null)
    		slave.set(outputValue);
	}

	@Override
	public void setInverted(boolean isInverted) {
		// TODO Auto-generated method stub
		this.inverted = isInverted ? -1 : 1;
	}

	public void setMotorBound(double newMotorBound) {
		// TODO Auto-generated method stub
		motorBound = newMotorBound;
	}
	
	public void setSlave(SpeedController3075 slave) {
		// TODO Auto-generated method stub
		this.slave = (CANTalon3075) slave;

	}
	
	public void setSlave(SpeedController3075 slave, boolean inverted)
	{
		this.slave = (CANTalon3075) slave;
		this.slave.setInverted(inverted);
	}
	
	public void setSlaveInverted(boolean inverted)
	{
		this.slave.setInverted(inverted);
	}

	@Override
	public double get() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void set(double speed, byte syncGroup) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean getInverted() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		
	}
	
	public void setPID(PIDvalue value)
	{
		setPID(value.kP, value.kI, value.kD);
	}
}
