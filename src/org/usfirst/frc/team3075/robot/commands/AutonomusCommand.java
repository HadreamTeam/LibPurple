package org.usfirst.frc.team3075.robot.commands;

import org.usfirst.frc.team3075.robot.Robot;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */
public class AutonomusCommand extends CommandGroup {
    
    public  AutonomusCommand() {
    	addSequential(Robot.driveSystem.AutoDrive(1, 1));
    }
}
