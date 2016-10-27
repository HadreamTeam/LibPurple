package libPurple;

import java.sql.DriverAction;

import org.usfirst.frc.team3075.robot.Components;
import org.usfirst.frc.team3075.robot.Constants;
import org.usfirst.frc.team3075.robot.Robot;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
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
	
	
	private double toleranceRate = 0.001;
	private double toleranceDistance = 0.005;
	
	
//	PIDController pidDiffRate;
	
	private double maxSpeed = 1.25;
	private double accellimit = 0.1;
	
	private boolean accelControl = false;
	private BuiltInAccelerometer accel = new BuiltInAccelerometer();
	private double smoothAccel = 0;
	private double frontLimit = 0;
	private double backLimit = 0;
	private double accelP = 0;
	private double accelError = 0;
	
	
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

		
		for(int i = 1; i < driveLeft.length; i++)
		{
			driveLeft[i - 1].setSlave(driveLeft[i]);
			driveRight[i - 1].setSlave(driveRight[i]);
		}
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
    	pidRightRate.setAbsoluteTolerance(toleranceRate);
    	
    	pidLeftRate.setInputRange(-maxSpeed, maxSpeed);
    	pidLeftRate.setOutputRange(-accellimit, accellimit);
    	pidLeftRate.setAbsoluteTolerance(toleranceRate);

    }
    
    public void setPID(PIDvalue pid)
    {
    	pidLeftRate = new PIDController(pid.kP, pid.kI, pid.kD, encoderLeft, driveLeft[0]);
    	pidRightRate = new PIDController(pid.kP, pid.kI, pid.kD, encoderRight, driveRight[0]);
    	
    	pidRightRate.setInputRange(-maxSpeed, maxSpeed);
    	pidRightRate.setOutputRange(-accellimit, accellimit);
    	pidRightRate.setAbsoluteTolerance(toleranceRate);
    	
    	pidLeftRate.setInputRange(-maxSpeed, maxSpeed);
    	pidLeftRate.setOutputRange(-accellimit, accellimit);
    	pidLeftRate.setAbsoluteTolerance(toleranceRate);

    }
    
    public void enablePID()
    {
    	if(pidLeftRate == null || pidRightRate == null)
    	{
    		utils.printErr("PID has not been set yet");
    		return;
    	}
    	    	
    	pidLeftRate.enable();
    	pidRightRate.enable();
    	
    }
    
    public void disablePID()
    {
    	pidLeftRate.setSetpoint(0);
    	pidRightRate.setSetpoint(0);
    	
    	pidLeftRate.reset();
    	pidRightRate.reset();

    	pidLeftRate.disable();
    	pidRightRate.disable();
    }
    
    public void setLeft(double speed)
    {
    	if(pidLeftRate != null && pidLeftRate.isEnabled())
    	{
    		pidLeftRate.setSetpoint(speed * maxSpeed);
    		SmartDashboard.putNumber("Left Setpoint", speed * maxSpeed);

    		SmartDashboard.putNumber("Left pid", pidLeftRate.get());
    		
    		if(!(utils.inRange(pidLeftRate.get(), 0, toleranceRate)))
    			speed = pidLeftRate.get();
    		
    		 	
    	}
    	if(smoothAccel > frontLimit){
    		accelError = frontLimit - smoothAccel;
    	}
    	
    	else if(smoothAccel < backLimit){
    		accelError = smoothAccel - backLimit;
    	}
    	else
    		accelError = 0;
    	
    	if(accelControl)
    		speed += accelError*accelP;
    	
    	SmartDashboard.putNumber("Left final", speed);
    	driveLeft[0].set(speed);
    	
    }
    
    public void setRight(double speed)
    {
    	if(pidRightRate != null && pidRightRate.isEnabled())
    	{
    		pidRightRate.setSetpoint(speed * maxSpeed);
    		SmartDashboard.putNumber("Right Setpoint", speed * maxSpeed);

    		SmartDashboard.putNumber("Right pid", pidRightRate.get());

    		if(!(utils.inRange(pidRightRate.get(), 0, toleranceRate)))
    			speed = pidRightRate.get();
    		
    	}
    	if(smoothAccel > frontLimit){
    		accelError = frontLimit - smoothAccel;
    	}
    	
    	else if(smoothAccel < backLimit){
    		accelError = smoothAccel - backLimit;
    	}
    	else
    		accelError = 0;
    	
    	if(accelControl)
    		speed += accelError*accelP;
    	
    	driveRight[0].set(speed);
    	SmartDashboard.putNumber("Right final", speed);
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
	
	public double getToleranceRate() {
		return toleranceRate;
	}

	public void setToleranceRate(double toleranceRate) {
		this.toleranceRate = toleranceRate;
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
	
	public double getRightRate()
	{
//		((Encoder3075) encoderLeft)
		// might be the magic we are looking for. 2016
		return encoderRight.getEncoder().getRate();
	}
	
	public double getLeftRate()
	{
//		((Encoder3075) encoderLeft).; // might be the magic we are looking for. 2016
		return encoderLeft.getEncoder().getRate();
	}
	
	public double getRightDistance()
	{
//		((Encoder3075) encoderLeft)
		// might be the magic we are looking for. 2016
		return encoderRight.getEncoder().getDistance();
	}
	
	public double getLeftDistance()
	{
//		((Encoder3075) encoderLeft).; // might be the magic we are looking for. 2016
		return encoderLeft.getEncoder().getDistance();
	}
	
	public double getRate()
	{
//		((Encoder3075) encoderLeft).; // might be the magic we are looking for. 2016
		return (encoderLeft.getEncoder().getRate() + encoderRight.getEncoder().getRate()) / 2;
	}
	
	public void setPIDLeftDistance(PIDvalue p, double driveOutputRange)
	{
		pidLeftDistance = new PIDController(p.kP, p.kI, p.kD, encoderLeft, driveLeft[0]);
		pidLeftDistance.setAbsoluteTolerance(toleranceDistance);
		pidLeftDistance.setOutputRange(-driveOutputRange, driveOutputRange);
	}
	
	public void setPIDRightDistance(PIDvalue p, double driveOutputRange)
	{
		pidRightDistance = new PIDController(p.kP, p.kI, p.kD, encoderRight, driveRight[0]);
		pidRightDistance.setAbsoluteTolerance(toleranceDistance);
		pidRightDistance.setOutputRange(-driveOutputRange, driveOutputRange);
	}
	
	public AutoDrive AutoDrive(double leftDistance, double rightDistance)
	{	
		setReturnRate(false);
		return new AutoDrive(this, leftDistance, pidLeftDistance, rightDistance, pidRightDistance);
	}
	
	public AutoDrive AutoDrive(double[] distance)
	{	
		setReturnRate(false);
		return new AutoDrive(this, distance[0], pidLeftDistance, distance[1], pidRightDistance);
	}
	
	public void setReturnRate(boolean b)
	{
		encoderLeft.setReturnRate(b);
		encoderRight.setReturnRate(b);

	}

	public double getToleranceDistance() {
		return toleranceDistance;
	}

	public void setToleranceDistance(double toleranceDistance) {
		this.toleranceDistance = toleranceDistance;
	}
	
	public void resetEncoders()
	{
		encoderLeft.getEncoder().reset();
		encoderRight.getEncoder().reset();

	}

	public boolean isAccelControl() {
		return accelControl;
	}

	public void enableAccelControl(boolean accelControl) {
		this.accelControl = accelControl;
	}
	
	public void setAccelControl(double frontLimit, double backLimit, double accelP){
		this.backLimit = backLimit;
		this.frontLimit = frontLimit;
		this.accelP = accelP;
	}

	public BuiltInAccelerometer getAccel() {
		return accel;
	}
	
	public void update()
	{
    	smoothAccel = smoothAccel*0.5 + accel.getZ()*0.5;
	}
}

class JoystickArcadeDrive extends Command{
	
	double y;
	double x;
	
	double lastY;
	double lastX;
	
	
	public JoystickArcadeDrive() {
		// TODO Auto-generated constructor stub
		requires(Robot.driveSystem);
	}
	
	@Override
	protected void initialize() 
	{
		// TODO Auto-generated method stub
//		Robot.driveSystem.setReturnRate(true);
		
		
	}

	@Override
	protected void execute()
	{
		// TODO Auto-generated method stub
		y = utils.deadband(-Components.driveStick.getY(), 0.1);
		x = utils.deadband(Components.driveStick.getX(), 0.1);
		
		y = utils.accellimit(y, lastY, Constants.accellimit);
		x = utils.accellimit(x, lastX, Constants.accellimit);
		
		// The motors started getting values when the joystick is resting, so we added this.
//		if(y == 0 && x == 0)
//			Robot.driveSystem.disablePID();
		
//		else 
//			Robot.driveSystem.enablePID();
		//****stupid code ends here****
		
		Robot.driveSystem.setArcadeDrive(y, x);
		
		lastX = x;
		lastY = y;

		
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
    	requires(Robot.driveSystem);

	}
    
    AutoDrive(DriveSystem3075 driveSystem, double leftDistance, PIDController pidLeftDistance, double rightDistance, PIDController pidRightDistance, PIDController pidDiff)
	{ 
    	this.rightDistance = rightDistance;
    	this.leftDistance = leftDistance;
    	
    	this.pidLeftDistance = pidLeftDistance;
    	this.pidRightDistance = pidRightDistance;
    	
    	this.pidDiff = pidDiff;
    	
    	this.driveSystem = driveSystem;
    	requires(Robot.driveSystem);
	}
	
	@Override
	protected void initialize() {
		// TODO Auto-generated method stub
		driveSystem.resetEncoders();
		pidLeftDistance.reset();
		pidRightDistance.reset();

		if(pidLeftDistance != null){
			pidLeftDistance.enable();
			pidLeftDistance.setSetpoint(0);
			pidLeftDistance.setSetpoint(-pidLeftDistance.getError() + leftDistance);
		}
		
		if(pidRightDistance != null){
			pidRightDistance.enable();
			pidRightDistance.setSetpoint(0);
			pidRightDistance.setSetpoint(-pidRightDistance.getError() + rightDistance);
		}
		
		if(pidDiff != null){
			pidDiff.enable();
			pidDiff.setSetpoint(0);
			pidDiff.setSetpoint(-pidDiff.getError());
		}
		
		driveSystem.setReturnRate(false);
	}

	@Override
	protected void execute()
	{
		// TODO Auto-generated method stub
//			DriverStation.reportError("\nP: " + pidLeftDistance.getP(), false);
//			DriverStation.reportError("\nIs enb: " + pidLeftDistance.isEnabled(), false);

			driveSystem.setTankDrive(pidLeftDistance.get() , pidRightDistance.get());
			SmartDashboard.putNumber("Right Distance Setpoint", pidRightDistance.getSetpoint());
			SmartDashboard.putNumber("Left Distance Setpoint", pidLeftDistance.getSetpoint());
			DriverStation.reportError("\n P: " + pidLeftDistance.getP(), false);
			

	}

	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		return utils.inRange(driveSystem.getRightDistance(), rightDistance, driveSystem.getToleranceDistance()) 
				&& utils.inRange(driveSystem.getLeftDistance(), leftDistance, driveSystem.getToleranceDistance());
//		return false;
	}

	@Override
	protected void end() {
		// TODO Auto-generated method stub
		DriverStation.reportError("Done!!!!!!!!!", false);
		driveSystem.setReturnRate(true);
		pidLeftDistance.disable();
		pidRightDistance.disable();
		driveSystem.disablePID();
		if(pidDiff != null)
			pidDiff.disable();
	}

	@Override
	protected void interrupted() {
		// TODO Auto-generated method stub
		end();
	}
	
}
