/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.UltrasonicSubsystem;

public class StalkerRoomba extends CommandBase {
  DriveAutoSubsystem m_robotDrive;
  UltrasonicSubsystem m_ultra;
  WallChecker wallChecker;
  TurnByCommand turner;
  double speed;
  boolean shouldCheckWall, hasChecked, hasTurned, isFinished;
  Timer timer;
  double currentDistance = 0.0;
  double newDistance = 0.0;
  double targetDis;
  final double margin = 3;
  final double ultraMargin = 0.2;
  final double offSet = 36 * 0.5;

  public boolean isWall;

  public StalkerRoomba(double dis, DriveAutoSubsystem robotDrive, UltrasonicSubsystem ultra) {
    targetDis = dis;
    m_robotDrive = robotDrive;
    m_ultra = ultra;
    speed = 0.0;
    timer = new Timer();
    isFinished = false;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    isWall = false;
    hasTurned = false;
    shouldCheckWall = false;
    isFinished = false;
    wallChecker = new WallChecker(40, m_robotDrive, m_ultra, this);
    turner = new TurnByCommand(90, m_robotDrive, 0);
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    currentDistance = m_ultra.getSensour();

    //if not checking if a wall
    if (!wallChecker.isScheduled()) {
      //if should continue moving
      if (!isWall) {
        //if outside range
        if(currentDistance < targetDis + margin + offSet){
          System.out.println("IN RANGE");
          speed = 0;
          shouldCheckWall = true;
        }
        else {
          System.out.println("OUT OF RANGE");
          speed += 0.075;
          shouldCheckWall = false;
          hasChecked = false;
        } 

        //speed limiters
        if (speed > 0.5) {
          speed = 0.5;
        }
        if (speed < -0.5) {
          speed = -0.5;
        }

        System.out.println("SPEED " + speed);
        //setting speed
        m_robotDrive.setFrontLeftSpeed(speed);
        m_robotDrive.setFrontRightSpeed(speed);
      }
      //if at a wall
      else {
        if (!turner.isScheduled()) {
          //first time, rotate left
          if (!hasTurned) {
            System.out.println("Turner Scheduled");
            // m_robotDrive.setFrontLeftSpeed(0);
            // m_robotDrive.setFrontRightSpeed(0);
            //CommandScheduler.getInstance().schedule(turner);
            hasTurned = true;
          }

          //once rotated, end command
          else if (hasTurned) {
            System.out.println("Command Finished");
            isFinished = true;
          }
        }
      }

      //if needs to check if at wall
      if (shouldCheckWall && !hasChecked) {
        System.out.println("Wall Checker Scheduled");
        // m_robotDrive.setFrontLeftSpeed(0);
        // m_robotDrive.setFrontRightSpeed(0);
        //CommandScheduler.getInstance().schedule(wallChecker);
        hasChecked = true;
      }
    }
    else {
      System.out.println("Out of IF Statement");

      m_robotDrive.setFrontLeftSpeed(0);
      m_robotDrive.setFrontRightSpeed(0);
    }

    System.out.print("Ultra: " + m_ultra.getSensour() + " ");
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_robotDrive.setFrontLeftSpeed(0);
    m_robotDrive.setFrontRightSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}