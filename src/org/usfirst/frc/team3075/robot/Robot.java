
package org.usfirst.frc.team3075.robot;

import libPurple.DriveSystem3075;
import libPurple.utils;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team3075.robot.commands.AutonomusCommand;
import org.usfirst.frc.team3075.robot.subsystems.ExampleSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {

	public static DriveSystem3075 driveSystem;
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
    Command autonomousCommand;
    
   
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit() {
    	
    	Constants.init();
    	Components.init();
        // instantiate the command used for the autonomous period
        autonomousCommand = new AutonomusCommand();
        
        
    }
	
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

    public void autonomousInit() {
        // schedule the autonomous command (example)
        if (autonomousCommand != null) autonomousCommand.start();
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

        Scheduler.getInstance().run();
    }

    public void teleopInit() {
		// This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to 
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) autonomousCommand.cancel();
        Components.driveLeftEncoder.reset();
        Components.driveRightEncoder.reset();
        
    }

    /**
     * This function is called when the disabled button is hit.
     * You can use it to reset subsystems before shutting down.
     */
    public void disabledInit(){

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
        Scheduler.getInstance().run();
//		Components.robotDrive.arcadeDrive(Components.driveStick);
		SmartDashboard.putNumber("EncoderLeftRate", Components.driveLeftEncoder.getRate());
		SmartDashboard.putNumber("EncoderRightRate", Components.driveRightEncoder.getRate());
		SmartDashboard.putNumber("EncoderLeftDistance", Components.driveLeftEncoder.getDistance());
		SmartDashboard.putNumber("EncoderRightDistance", Components.driveRightEncoder.getDistance());
		SmartDashboard.putNumber("JoystickX", Components.driveStick.getX());
		SmartDashboard.putNumber("JoystickY", Components.driveStick.getY());
//		SmartDashboard.putNumber("PIDleft", driveSystem.getPIDLeft());
//		SmartDashboard.putNumber("PIDright", driveSystem.getPIDRight());
		utils.batteryWatcher();	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
//        LiveWindow.run();
        Scheduler.getInstance().run();
    }
}
