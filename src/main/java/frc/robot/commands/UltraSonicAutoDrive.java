/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;
import frc.robot.subsystems.Ultrasonic;
import edu.wpi.first.wpilibj.PWMSpeedController;

public class UltraSonicAutoDrive extends Command {
 DifferentialDrive m_robotDrive;
  
 final double kHoldDistance = 12.0;
 final double kValueToInches = 0.125;
 final AnalogInput m_ultrasonic = new AnalogInput(Robot.m_Map.UltraSonic);
 double currentDistance;
 
 final double kP = 0.05;

 final int kLLeftMotorPort = 0;
 final int kRightMotorport = 1;
 double speed;
 double motorTurn;
 
  public UltraSonicAutoDrive() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    speed = 0;
    m_robotDrive = new DifferentialDrive(new WPI_TalonSRX(kLLeftMotorPort),  new WPI_TalonSRX(kRightMotorport));
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double currentDistance = m_ultrasonic.getValue() * kValueToInches;
    Robot.ultrasonic.getSensour();
    if (currentDistance >= 36) {
      speed = speed + 0.01;
    } else {
      speed = speed - 0.1;
      if (currentDistance < 12) {
        speed = 0;
      }

    }
    m_robotDrive.arcadeDrive(speed, 0);
    if (currentDistance < 20)
    {
      speed = 0;
      motorTurn = 0.25;
    //Robot stops
    //Robot turns right

   
    } else {
      speed = 0.1;
      motorTurn = 0;
     //Keep going
   }
    m_robotDrive.arcadeDrive(speed, motorTurn);

    	
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
