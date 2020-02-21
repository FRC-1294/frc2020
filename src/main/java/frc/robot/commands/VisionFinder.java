/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.TwentyThreeStabWounds;

public class VisionFinder extends CommandBase {
  DriveAutoSubsystem drive;
  TwentyThreeStabWounds vision;

  boolean isFinished;
  double currentAngle;
  double targetPositionRotations = 0.097;
  double[] startPos;
  double[] speed;

  Timer timer;

  public VisionFinder(DriveAutoSubsystem driver, TwentyThreeStabWounds visionSub) {
    isFinished = false;
    drive = driver;
    vision = visionSub;
  }

  @Override
  public void initialize() {
    drive.setFrontLeftSpeed(0);
    drive.setFrontRightSpeed(0);
    drive.setRearLeftSpeed(0);
    drive.setRearRightSpeed(0);
    
    timer = new Timer();
    timer.start();
    timer.reset();
    
    startPos = new double[] {drive.getFrontLeftPosition(), drive.getFrontRightPosition()};
    speed = new double [] {0, 0};
    currentAngle = 0;
    isFinished = false;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentAngle = (drive.getFrontLeftPosition()-startPos[0]) / targetPositionRotations;
    
    if (vision.isDetected()) {
      if (timer.get() > 1) isFinished = true;
      
      speed[0] = 0;
      speed[1] = 0;
    }
    else if (currentAngle > 90) {
      speed[0] = -0.3;
      speed[1] = 0.3;
      timer.reset();
    }
    else if (currentAngle < -90) {
      speed[0] = 0.3;
      speed[1] = -0.3;
      timer.reset();
    }

    drive.setFrontLeftSpeed(speed[0]);
    drive.setFrontRightSpeed(speed[1]);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drive.setFrontLeftSpeed(0);
    drive.setFrontRightSpeed(0);
    drive.setRearLeftSpeed(0);
    drive.setRearRightSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
