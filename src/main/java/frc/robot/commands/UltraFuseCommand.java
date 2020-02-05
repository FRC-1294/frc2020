/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/
package frc.robot.commands;

import frc.robot.Robot;
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.UltrasonicSubsystem;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class UltraFuseCommand extends CommandBase {
  UltrasonicSubsystem ultrasonic;
  DriveAutoSubsystem driveSub;
  final double kHoldDistance = 12.0; 
  final double kP = 0.05;
  double speed;
  double motorTurn;
  boolean isFinished;

  public UltraFuseCommand(DriveAutoSubsystem driver, UltrasonicSubsystem ultra) {
    // Use addRequirements() here to declare subsystem dependencies.
    speed = 0.0;
    motorTurn = 0.0;
    ultrasonic = ultra;
    driveSub = driver;
    
    addRequirements(Robot.ultrasonic);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double currentDistance = ultrasonic.getSensour();
   
    if (currentDistance < 24){
      isFinished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    driveSub.setRamp(0.3);

    driveSub.setFrontLeftSpeed(0);
    driveSub.setFrontRightSpeed(0);
    driveSub.setRearLeftSpeed(0);
    driveSub.setRearRightSpeed(0);

    driveSub.setRamp(1);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}