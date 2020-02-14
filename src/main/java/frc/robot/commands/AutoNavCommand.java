/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.UltrasonicSubsystem;

public class AutoNavCommand extends CommandBase {
  int iterations = 0;
  boolean isFinished = false;
  DriveAutoSubsystem m_driveAuto;
  UltrasonicSubsystem m_ultra;
  StalkerRoomba moveUntilWall;
  AutoPath autoPath;
  UltraFuseCommand ultraFuse;
  boolean left1 = true;
  boolean left2 = true;
  int targetAngle;
  boolean loopComplete = false;
  int xTarget;
  int yTarget;
  final int delta = 2;

  int step = 0;

  public AutoNavCommand(DriveAutoSubsystem driveAuto, UltrasonicSubsystem ultra) {
    m_driveAuto = driveAuto;
    m_ultra = ultra;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    addRequirements(m_driveAuto);
    addRequirements(m_ultra);

    left1 = false;
    left2 = true;
    xTarget = 3*12;
    yTarget = 0;//15*12;
    targetAngle = 180;

    //go past the init line
    autoPath = new AutoPath(xTarget, yTarget, left1, left2, m_driveAuto);
    ultraFuse = new UltraFuseCommand(m_driveAuto, m_ultra);
    moveUntilWall = new StalkerRoomba(5*12, m_driveAuto, m_ultra);

    CommandScheduler.getInstance().schedule(autoPath);
    CommandScheduler.getInstance().schedule(ultraFuse);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //distance left to travel
    double xRem = Math.abs(xTarget - m_driveAuto.getAmountTraveled(0));
    double yRem = Math.abs(yTarget - m_driveAuto.getAmountTraveled(1));

    if ((!autoPath.isScheduled() && !moveUntilWall.isScheduled()) && ultraFuse.isScheduled()) {
      if (Math.abs(xRem) <= delta && Math.abs(yRem) <= delta) {
        step++;

        //goto the far wall
        if (step == 1) {
          yTarget = 0;
          left1 = false;
          left2 = true;
          targetAngle = 90;
          yRem = Math.abs(yTarget - m_driveAuto.getAmountTraveled(1));
          moveUntilWall = new StalkerRoomba(5*12, m_driveAuto, m_ultra);
          CommandScheduler.getInstance().schedule(moveUntilWall);
        }
        //align with hoop and shoot
        else if (step == 2) {
          xTarget = 0;
          left1 = false;
          left2 = true;
          targetAngle = 270;
          xRem = Math.abs(xTarget - m_driveAuto.getAmountTraveled(0));
          autoPath = new AutoPath(xRem, 0, left1, left2, m_driveAuto);
          CommandScheduler.getInstance().schedule(autoPath);
        }
        //return to startPoint
        else if (step == 3) {
          yTarget = 0;
          left1 = false;
          left2 = true;
          targetAngle = 0;
          yRem = Math.abs(yTarget - m_driveAuto.getAmountTraveled(1));
          moveUntilWall = new StalkerRoomba(4*12, m_driveAuto, m_ultra);
          CommandScheduler.getInstance().schedule(moveUntilWall);
        }
        //end command
        else if (step == 4) {
          isFinished = true;
        }
      } 
    }
    
    //if obstacle detected
    if (!ultraFuse.isScheduled() && !m_driveAuto.getTurning() && !moveUntilWall.isScheduled()) {
      autoPath.cancel();

      if (m_ultra.getSensour() <= m_ultra.MIN_DIS) {
        //follllow
      }
      else {
        if (targetAngle - m_driveAuto.getCurrentAngle() == 0) {
          left1 = false;
          left2 = false;
        } 
        else if (targetAngle - m_driveAuto.getCurrentAngle() == 90) {
          left1 = false;
          left2 = true;
        } 
        else if (targetAngle - m_driveAuto.getCurrentAngle() == 180) {
          left1 = true;
          left2 = true;
        }

        if (xRem >= delta || yRem >= delta) {
          autoPath = new AutoPath(xRem, yRem, left1, left2, m_driveAuto);
          CommandScheduler.getInstance().schedule(autoPath);
        }
        CommandScheduler.getInstance().schedule(ultraFuse);
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
