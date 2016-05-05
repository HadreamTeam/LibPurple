package libPurple;

import org.usfirst.frc.team3075.robot.Components;
import org.usfirst.frc.team3075.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *  						*** 3075 ***
 *  This class is our generic drive system. It includes:
 *  PID (by distance)
 *  PID (by speed)
 */
public class DriveSystem3075 extends Subsystem 
{
	private SpeedController3075[] driveLeft = new SpeedController3075[2];
	private SpeedController3075[] driveRight = new SpeedController3075[2];
	
	private PIDGetEncoder encoderRight;
	private PIDGetEncoder encoderLeft;
	
	private PIDController pidLeftDistance;
	private PIDController pidRightDistance;
	private PIDController pidDiff;
	
	private PIDController pidLeftRate;
	private PIDController pidRightRate;
	
	
	private double tolerance = 0.1;
	
//	PIDController pidDiffRate;
	
	private double maxSpeed = 1.25;
	private double accellimit = 0.5;
	
	public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new JoystickArcadeDrive());
    }
	
    public DriveSystem3075(SpeedController3075[] driveLeft, Encoder3075 encoderLeft,
    		SpeedController3075[] driveRight, Encoder3075 encoderRight)
    {
		super();
		this.driveLeft = driveLeft;
		this.driveRight = driveRight;
		
		this.encoderLeft = new PIDGetEncoder(encoderLeft, true);
		this.encoderRight = new PIDGetEncoder(encoderRight, true);

		
		driveLeft[0].setSlave(driveLeft[1]);
		driveRight[0].setSlave(driveRight[1]);
	}
    
    public DriveSystem3075(SpeedController3075 rearLeft, SpeedController3075 frontLeft, Encoder3075 encoderLeft,
    		SpeedController3075 rearRight, SpeedController3075 frontRight, Encoder3075 encoderRight)
    {
		super();
		driveLeft[0] = rearLeft;
		driveLeft[1] = frontLeft;
		driveRight[0] = rearRight;
		driveRight[1] = frontRight;
		
		this.encoderLeft = new PIDGetEncoder(encoderLeft, true);
		this.encoderRight = new PIDGetEncoder(encoderRight, true);
		
		driveLeft[0].setSlave(driveLeft[1]);
		driveRight[0].setSlave(driveRight[1]);
	}
    
    public DriveSystem3075(int rearLeft, int frontLeft, Encoder3075 encoderLeft, int rearRight, int frontRight, Encoder3075 encoderRight)
    {
		super();
		driveLeft[0] = new Victor3075(rearLeft);
		driveLeft[1] = new Victor3075(frontLeft);
		driveRight[0] = new Victor3075(rearRight);
		driveRight[1] = new Victor3075(frontRight);
		this.encoderLeft = new PIDGetEncoder(encoderLeft, true);
		this.encoderRight = new PIDGetEncoder(encoderRight, true);
		
		driveLeft[0].setSlave(driveLeft[1]);
		driveRight[0].setSlave(driveRight[1]);
		
	}
    
    public void setPID(double p, double i, double d)
    {
    	pidLeftRate = new PIDController(p, i, d, encoderLeft, driveLeft[0]);
    	pidRightRate = new PIDController(p, i, d, encoderRight, driveRight[0]);
    	
    	pidRightRate.setInputRange(-maxSpeed, maxSpeed);
    	pidRightRate.setOutputRange(-accellimit, accellimit);
    	pidRightRate.setAbsoluteTolerance(0.3);
    	
    	pidLeftRate.setInputRange(-maxSpeed, maxSpeed);
    	pidLeftRate.setOutputRange(-accellimit, accellimit);
    	pidLeftRate.setAbsoluteTolerance(0.3);

    }
    
    public void enablePID()
    {
    	if(pidLeftRate == null || pidRightRate == null)
    	{
    		utils.printErr("PID have not been set");
    		return;
    	}
    	pidLeftRate.enable();
    	pidRightRate.enable();
    }
    
    public void disablePID()
    {
    	pidLeftRate.disable();
    	pidRightRate.disable();
    }
    
    public void setLeft(double speed)
    {
    	if(pidLeftRate != null && pidLeftRate.isEnable())
    	{
    		pidLeftRate.setSetpoint(speed * maxSpeed);
    		SmartDashboard.putNumber("Left Setpoint", speed * maxSpeed);

    		SmartDashboard.putNumber("Left pid", pidLeftRate.get());
    		
    		if(!(utils.inRange(pidLeftRate.get(), 0, tolerance)))
    			speed += pidLeftRate.get();

    		 	
    	}
    	
    	SmartDashboard.putNumber("Left final", speed);
    	driveLeft[0].set(speed);
    }
    
    public void setRight(double speed)
    {
    	if(pidRightRate != null && pidRightRate.isEnable())
    	{
    		pidRightRate.setSetpoint(speed * maxSpeed);
    		SmartDashboard.putNumber("Right Setpoint", speed * maxSpeed);

    		SmartDashboard.putNumber("Right pid", pidRightRate.get());

    		if(!(utils.inRange(pidRightRate.get(), 0, tolerance)))
    			speed += pidRightRate.get();
    		
    	}
    	
    	SmartDashboard.putNumber("Right final", speed);
    	driveRight[0].set(speed);
    }
    
    public void setTankDrive(double leftSpeed, double rightSpeed)
    {
    	setLeft(leftSpeed);
    	setRight(rightSpeed);
    }
    
    private void setTankDrive(double[] speed)
    {
    	setLeft(speed[0]);
    	setRight(speed[1]);
    }
    
    public void setArcadeDrive(double moveSpeed, double rotateSpeed)
    {
    	setTankDrive(utils.arcadeDrive(moveSpeed, rotateSpeed));
    }
    
    
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	public double getTolerance() {
		return tolerance;
	}

	public void setTolerance(double tolerance) {
		this.tolerance = tolerance;
	}

	/**
	 * 
	 * @param maxSpeed should be the robot's weak side max speed
	 */
	public void setMaxSpeed(double maxSpeed) {
		this.maxSpeed = maxSpeed;
	}
	
	public double getPIDLeft()
	{
		return pidLeftRate.get();
	}
	
	public double getPIDRight()
	{
		return pidRightRate.get();
	}
	
//	double dpMAX = 0.3;
//	double dpMIN = 0.1;
//	public double deadbandPID(double val)
//	{
//		if()
//	}
	
	public AutoDrive AutoDrive(double leftDistance, double rightDistance)
	{
		return new AutoDrive(this, leftDistance, pidLeftDistance, rightDistance, pidRightDistance);
	}
}

class JoystickArcadeDrive extends Command{
	
	public JoystickArcadeDrive() {
		// TODO Auto-generated constructor stub
		requires(Robot.driveSystem);
	}
	
	@Override
	protected void initialize() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void execute()
	{
		// TODO Auto-generated method stub
		double y = utils.deadband(-Components.driveStick.getY(), 0.1);
		double x = utils.deadband(Components.driveStick.getX(), 0.1);
		
		// The motors started getting values when the joystick is resting, so we added this.
		if(y == 0 && x == 0)
			Robot.driveSystem.disablePID();
		
		else 
			Robot.driveSystem.enablePID();
		//****stupid code ends here****
		
		Robot.driveSystem.setArcadeDrive(y, x);

		
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		
	}
	
}

class AutoDrive extends Command{
	
	private double rightDistance;
	private double leftDistance;
	
	private PIDController pidLeftDistance;
	private PIDController pidRightDistance;
	private PIDController pidDiff;
	
	private DriveSystem3075 driveSystem;
	
//	AutoDrive(DriveSystem3075 driveSystem, double leftDistance, double rightDistance)
//	{ 
//    	this.rightDistance = rightDistance;
//    	this.leftDistance = leftDistance;
//    	
//    	this.driveSystem = driveSystem;
//	}
	
    AutoDrive(DriveSystem3075 driveSystem, double leftDistance, PIDController pidLeftDistance, double rightDistance, PIDController pidRightDistance)
	{ 
    	this.rightDistance = rightDistance;
    	this.leftDistance = leftDistance;
    	
    	this.pidLeftDistance = pidLeftDistance;
    	this.pidRightDistance = pidRightDistance;
    	
    	this.driveSystem = driveSystem;

	}
    
    AutoDrive(DriveSystem3075 driveSystem, double leftDistance, PIDController pidLeftDistance, double rightDistance, PIDController pidRightDistance, PIDController pidDiff)
	{ 
    	this.rightDistance = rightDistance;
    	this.leftDistance = leftDistance;
    	
    	this.pidLeftDistance = pidLeftDistance;
    	this.pidRightDistance = pidRightDistance;
    	
    	this.pidDiff = pidDiff;
    	
    	this.driveSystem = driveSystem;
	}
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		
//		if(pidLeftDistance != null){
			utils.printErr("LEFT PID INITILIZWDEAFJ");
			pidLeftDistance.enable();
			pidLeftDistance.setSetpoint(0);
			pidLeftDistance.setSetpoint(-pidLeftDistance.getError() + leftDistance);
//		}
		
		if(pidRightDistance != null){
			utils.printErr("right PID INITILIZWDEAFJ");
			pidRightDistance.enable();
			pidRightDistance.setSetpoint(0);
			pidRightDistance.setSetpoint(-pidRightDistance.getError() + rightDistance);
		}
		
		if(pidDiff != null){
			pidDiff.enable();
			pidDiff.setSetpoint(0);
			pidDiff.setSetpoint(-pidDiff.getError());
		}
		
	}

	@Override
	protected void execute() {
		// TODO Auto-generated method stub
		if(pidDiff != null)
			driveSystem.setTankDrive(pidLeftDistance.get() + pidDiff.get(), pidRightDistance.get() + pidDiff.get());
		else driveSystem.setTankDrive(pidLeftDistance.get(), pidRightDistance.get());
	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return pidLeftDistance.onTarget() && pidRightDistance.onTarget();
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		pidLeftDistance.disable();
		pidRightDistance.disable();
		
		if(pidDiff != null)
			pidDiff.disable();
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		end();
	}
	
}
