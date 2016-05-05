package org.usfirst.frc.team3075.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import libPurple.CANTalon3075;
import libPurple.DriveSystem3075;
import libPurple.EncoderTalon3075;
import libPurple.Victor3075;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class Components {
	 
	public static Victor3075[] left;
	public static Victor3075[] right;
//	public static RobotDrive robotDrive;
	
	
	public static Joystick driveStick;
	
	public static CANTalon3075 leftWheel;
	public static CANTalon3075 rightWheel;
	
	public static EncoderTalon3075 driveLeftEncoder; //Red izo
	public static EncoderTalon3075 driveRightEncoder; //Blue izo
	
	public static void init() {
		left = new Victor3075[2];
		left[0] = new Victor3075(0);
		left[1] = new Victor3075(1);
		right = new Victor3075[2];
		right[0] = new Victor3075(2);
		right[1] = new Victor3075(3);
		left[0].setInverted(true);
		left[1].setInverted(true);
		
//		robotDrive = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
		driveStick = new Joystick(0);
		
		leftWheel = new CANTalon3075(0);
	    rightWheel = new CANTalon3075(2, true);
		
		driveLeftEncoder = new EncoderTalon3075(leftWheel, false);
		driveLeftEncoder.setDistancePerPulse(Constants.driveDistancePerPulseLeft);
		driveRightEncoder = new EncoderTalon3075(rightWheel, true);
		driveRightEncoder.setDistancePerPulse(Constants.driveDistancePerPulseRight);
		
		Robot.driveSystem = new DriveSystem3075(left, driveLeftEncoder, right, driveRightEncoder);
		Robot.driveSystem.setPID(0.35, 0.0003, 0.5);
		Robot.driveSystem.enablePID();
	}
}

  

