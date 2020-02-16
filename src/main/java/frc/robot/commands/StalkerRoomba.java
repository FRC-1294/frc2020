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
import frc.robot.subsystems.UltrasonicSubsystem;

public class StalkerRoomba extends CommandBase {
  DriveAutoSubsystem m_robotDrive;
  UltrasonicSubsystem m_ultra;
  WallChecker wallChecker;
  TurnByCommand turner;
  double speed;
  boolean shouldCheckWall, hasChecked, hasTurned, isFinished;
  Timer timer;
  double[] currentDistance = new double[2];
  double targetDis;
  final double ultraMargin = 0.2;
  final double offSet = 24;

  public boolean isWall;

  public StalkerRoomba(final double dis, final DriveAutoSubsystem robotDrive, final UltrasonicSubsystem ultra) {
    targetDis = dis;
    m_robotDrive = robotDrive;
    m_ultra = ultra;
    timer = new Timer();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_robotDrive.setFrontLeftSpeed(0);
    m_robotDrive.setFrontRightSpeed(0);
    m_robotDrive.setRearLeftSpeed(0);
    m_robotDrive.setRearRightSpeed(0);

    resetVars();

    wallChecker = new WallChecker(40, m_robotDrive, m_ultra, this);
    turner = new TurnByCommand(90, m_robotDrive, 0);
    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //boolean isStopped = m_robotDrive.getFrontLeftSpeed() == 0 && m_robotDrive.getFrontRightSpeed() == 0;
    currentDistance[0] = m_ultra.getSensourLeft();
    currentDistance[1] = m_ultra.getSensourRight();

    //if not checking if a wall
    if (!wallChecker.isScheduled()) {
      //if needs to check if at wall
      if (shouldCheckWall && !hasChecked) {
        System.out.println("Wall Checker Scheduled");
        hasChecked = true;
        wallChecker.schedule();
      }
      else if (!isWall)  {//if should continue moving
        if(currentDistance[0] < targetDis + offSet ||
          currentDistance[1] < targetDis + offSet){ //if outside range
          System.out.println("IN RANGE");

          if (timer.get() >= 1) {
            shouldCheckWall = true;
          }

          //setting speed
          m_robotDrive.setFrontLeftSpeed(0);
          m_robotDrive.setFrontRightSpeed(0);
          m_robotDrive.setRearLeftSpeed(0);
          m_robotDrive.setRearRightSpeed(0);
        }
        else {
          System.out.println("OUT OF RANGE");
          timer.reset();
          hasChecked = false;
          shouldCheckWall = false;

          //setting speed
          m_robotDrive.setFrontLeftSpeed(0.3);
          m_robotDrive.setFrontRightSpeed(0.3);
          m_robotDrive.setRearLeftSpeed(0.3);
          m_robotDrive.setRearRightSpeed(0.3);
        }
      }
      //if at a wall
      else if (isWall) {
        if (!turner.isScheduled() ) {
          //first time, rotate left
          if (!hasTurned) {
            System.out.println("Turner Scheduled");
            turner.schedule();
            hasTurned = true;
          }

          //once rotated, end command
          else if (hasTurned) {
            System.out.println("Command Finished");
            isFinished = true;
          }
        }
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(final boolean interrupted) {
    m_robotDrive.setFrontLeftSpeed(0);
    m_robotDrive.setFrontRightSpeed(0);
    m_robotDrive.setRearLeftSpeed(0);
    m_robotDrive.setRearRightSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }

  public void resetVars() {
    speed = 0;
    shouldCheckWall = false;
    hasChecked = false;
    hasTurned = false;
    isFinished = false;
    timer.reset();
    currentDistance[0] = 0.0;
    currentDistance[1] = 0.0;

    isWall = false;
  }
}