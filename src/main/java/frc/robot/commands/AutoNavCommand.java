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
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.UltrasonicSubsystem;

public class AutoNavCommand extends CommandBase {
  int iterations = 0;
  boolean isFinished = false;
  DriveAutoSubsystem m_driveAuto;
  UltrasonicSubsystem m_ultra;
  AutoPath autoPath;
  NavigateObstacle navObs;
  UltraFuseCommand ultraFuse;
  AutoLogic autoLogic;
  boolean isLeft = true;
  int targetAngle;
  boolean loopComplete = false;
  int xTarget = 10*12;
  int yTarget = 4*12;
  final int delta = 2;

  boolean hasReached = false;
  boolean hasShot = false;

  public AutoNavCommand(DriveAutoSubsystem driveAuto, UltrasonicSubsystem ultra, Boolean left) {
    addRequirements(driveAuto);
    addRequirements(ultra);

    m_driveAuto = driveAuto;
    m_ultra = ultra;
    isLeft = left;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    autoPath = new AutoPath(xTarget, yTarget, isLeft, m_driveAuto);
    ultraFuse = new UltraFuseCommand(m_driveAuto, m_ultra);
    navObs = new NavigateObstacle(4.5*12, 4*12, m_driveAuto);
    autoLogic = new AutoLogic(ultraFuse, autoPath);
    CommandScheduler.getInstance().schedule(autoLogic);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //distance left to travel
    double xRem = xTarget - m_driveAuto.getAmountTraveled(0);
    double yRem = yTarget - m_driveAuto.getAmountTraveled(1);

    if (!autoLogic.isScheduled()) {
      if (Math.abs(xRem) <= delta && Math.abs(yRem) <= delta) {
        if (hasReached) {
          System.out.println("Has reached == true");
          isFinished = true;
        }
        else {
          hasReached = true;
          System.out.println("Rescheduling");
          xTarget = 0;
          yTarget = 0;
          autoPath = new AutoPath(xTarget, yTarget, isLeft, m_driveAuto);
          CommandScheduler.getInstance().schedule(autoLogic);
        }
      }
      else {
        if (m_ultra.getSensour() <= m_ultra.MIN_DIS) {
          //follllow5
          System.out.println("Got in sensor < dis loop");
        }
        else {
          xTarget = (int) xRem;
          yTarget = (int) yRem;
          autoPath = new AutoPath(xTarget, yTarget, isLeft, m_driveAuto);
          CommandScheduler.getInstance().schedule(autoLogic);
          System.out.println("Else statement for ultra loop");
        }
      }
    }
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
