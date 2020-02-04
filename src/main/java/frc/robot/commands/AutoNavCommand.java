/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.UltrasonicSubsystem;

public class AutoNavCommand extends CommandBase {
  int iterations = 0;
  boolean isFinished = false;
  DriveAutoSubsystem m_driveAuto;
  AutoPath autoPath;
  NavigateObstacle navObs;
  UltraFuseCommand ultraFuse;
  ParallelRaceGroup autoLogic;

  final int xTarget = 10*12;
  final int yTarget = 4*12;
  final int delta = 2;
  boolean hasReached = false;
  boolean hasCompleted = false;

  public AutoNavCommand(DriveAutoSubsystem driveAuto, UltrasonicSubsystem ultra, Boolean left) {
    addRequirements(driveAuto);
    addRequirements(ultra);

    m_driveAuto = driveAuto;

    autoPath = new AutoPath(xTarget, yTarget, left, driveAuto);
    ultraFuse = new UltraFuseCommand(m_driveAuto, ultra);
    navObs = new NavigateObstacle(4.5*12, 4*12, m_driveAuto);
    autoLogic = new ParallelRaceGroup(ultraFuse, autoPath);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    CommandScheduler.getInstance().schedule(autoLogic);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double xChange = xTarget - m_driveAuto.getAmountTraveled(0);
    double yChange = yTarget - m_driveAuto.getAmountTraveled(1);

    if (Math.abs(xChange) <= delta || Math.abs(yChange) <= delta) {
      hasCompleted = true;
    }

    if (autoLogic.isFinished() && hasCompleted) {
      isFinished = true;
    }
    else if (!autoLogic.isScheduled()) {
      xChange = xTarget - m_driveAuto.getAmountTraveled(0);
      yChange = yTarget - m_driveAuto.getAmountTraveled(1);
      if (Math.abs(xChange) <= delta || 
          Math.abs(yChange) <= delta){
            autoPath = new AutoPath(xChange, yChange, true, m_driveAuto);
            //autoLogic = new ParallelRaceGroup(ultraFuse, autoPath);
            //CommandScheduler.getInstance().schedule(autoLogic);
      }
    }
    //else if (!autoLogic.isScheduled()) {
    //  CommandScheduler.getInstance().schedule(navObs);
    //}
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveAuto.setFrontLeftSpeed(0);
    m_driveAuto.setFrontRightSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
