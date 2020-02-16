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
import frc.robot.subsystems.twentythreestabwounds;

public class AutoNavCommand extends CommandBase {
  DriveAutoSubsystem m_driveAuto;
  UltrasonicSubsystem m_ultra;
  twentythreestabwounds m_vision;

  AutoPath autoPath;
  StalkerRoomba moveUntilWall;
  DictatorLocator alignToTarget;
  UltraFuseCommand ultraFuse;

  int iterations = 0;
  boolean isFinished = false;
  boolean left1 = true;
  boolean left2 = true;
  int targetAngle;
  boolean loopComplete = false;
  int xTarget;
  int yTarget;

  final int autoPathMargin = 2;
  final int robotFollowDis = 5*12;
  final int shootDis = 11*12;

  int step = 0;

  public AutoNavCommand(DriveAutoSubsystem driveAuto, UltrasonicSubsystem ultra, twentythreestabwounds vision) {
    m_driveAuto = driveAuto;
    m_ultra = ultra;
    m_vision = vision;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    addRequirements(m_driveAuto);
    addRequirements(m_ultra);

    //go past the init line
    left1 = false;
    left2 = true;
    xTarget = 3*12; //SHOULD BE 10 FT
    yTarget = 0;
    targetAngle = 90;
    autoPath = new AutoPath(xTarget, yTarget, left1, left2, m_driveAuto);
    ultraFuse = new UltraFuseCommand(m_driveAuto, m_ultra);
    moveUntilWall = new StalkerRoomba(robotFollowDis, m_driveAuto, m_ultra);
    alignToTarget = new DictatorLocator(m_vision, m_driveAuto);

    autoPath.schedule();
    ultraFuse.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // //distance left to travel
    double xRem = Math.abs(xTarget - m_driveAuto.getAmountTraveled(0));
    double yRem = Math.abs(yTarget - m_driveAuto.getAmountTraveled(1));

    //if current leg of path finished, schedule next in sequence
    if (!autoPath.isScheduled() && !moveUntilWall.isScheduled() && !alignToTarget.isScheduled() && ultraFuse.isScheduled()) {
      if (Math.abs(xRem) <= autoPathMargin && Math.abs(yRem) <= autoPathMargin) {
        step++;

        //goto the far wall
        if (step == 1) {
          moveUntilWall = new StalkerRoomba(robotFollowDis, m_driveAuto, m_ultra);
          moveUntilWall.schedule();
        }
        //align with hoop and shoot
        else if (step == 2) {
          alignToTarget.schedule();
        }
        //move until shooting distance
        else if (step == 3) {
          // xTarget = 0;
          // left1 = false;
          // left2 = true;
          // targetAngle = 270;
          // xRem = Math.abs(xTarget - m_driveAuto.getAmountTraveled(0));
          // autoPath = new AutoPath(xRem, 0, left1, left2, m_driveAuto);
          // autoPath.schedule();
          moveUntilWall = new StalkerRoomba(shootDis, m_driveAuto, m_ultra);
          moveUntilWall.schedule();
        }
        else if (step == 4) {
          
        }
        //return to startPoint
        else if (step == 5) {
          moveUntilWall = new StalkerRoomba(robotFollowDis, m_driveAuto, m_ultra);
          moveUntilWall.schedule();
        }
        //end command
        else if (step == 6) {
          isFinished = true;
        }
      } 
    }
    

    //if obstacle detected during PID
    if (!ultraFuse.isScheduled()) {
      //if stopping necessary
      if ((!m_driveAuto.getTurning() && !moveUntilWall.isScheduled() && !!alignToTarget.isScheduled())) {
        autoPath.cancel();

        if (m_ultra.getSensour() <= m_ultra.MIN_DIS) {
          //avoid?
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

          //resechedule path if obstacle avoided
          if (xRem >= autoPathMargin || yRem >= autoPathMargin) {
            autoPath = new AutoPath(xRem, yRem, left1, left2, m_driveAuto);
            autoPath.schedule();
          }
        }
      }
      //keep ultraFuse running to check if obstacle moves
      ultraFuse.schedule();
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
