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

public class WallChecker extends CommandBase {
  final int PIDSlot = 1;
  int HAL9000 = 0;
  int amount = 0;
  UltrasonicSubsystem dracula;
  DriveAutoSubsystem whee;
  StalkerRoomba myStalker;
  double[] threeMusketeers = new double[4];
  double margin = 6;
  TurnByCommand tokyoDrift;
  boolean isFinished;

  public WallChecker(int amount, DriveAutoSubsystem driver, UltrasonicSubsystem ultraBurst, StalkerRoomba stalker) {
    this.amount = amount;
    isFinished = false;
    dracula = ultraBurst;
    whee = new DriveAutoSubsystem();
    myStalker = stalker;
  }

  @Override
  public void initialize() {
    HAL9000 = 2;
    isFinished = false;
    threeMusketeers[0] = dracula.getSensourLeft();
    threeMusketeers[1] = dracula.getSensourRight();
    tokyoDrift = new TurnByCommand(amount, whee, PIDSlot);
    tokyoDrift.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!tokyoDrift.isScheduled()){
      if(HAL9000 == 2) {
        System.out.println();
        threeMusketeers[HAL9000] = dracula.getSensourRight();
        tokyoDrift = new TurnByCommand(-amount * 2, whee, PIDSlot);
        tokyoDrift.schedule();
        HAL9000 = 3;
      }
      else if(HAL9000 == 3) {
        threeMusketeers[HAL9000] = dracula.getSensourLeft();
        tokyoDrift = new TurnByCommand(amount, whee, PIDSlot);
        tokyoDrift.schedule();
        HAL9000 = 4;
      }
      else {
        double baseValLeft = threeMusketeers[0];
        double baseValRight = threeMusketeers[1];
        double baseVal = (baseValLeft+baseValRight)/2;

        if (baseValLeft <= baseValRight + margin && baseValLeft >= baseValRight-margin) {
          if (threeMusketeers[2] * Math.abs(Math.cos(amount)) > baseVal + margin) {
            myStalker.isWall = false;
          }
          else if (threeMusketeers[3] * Math.abs(Math.cos(amount)) > baseVal + margin) {
            myStalker.isWall = false;
          }
          else {
            myStalker.isWall = true;
          }
        }
        else {
          myStalker.isWall = false;
        }

        isFinished = true;
      }
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    whee.setFrontRightSpeed(0);
    whee.setFrontLeftSpeed(0);
    whee.setRearRightSpeed(0);
    whee.setRearLeftSpeed(0);

    System.out.println("\n\n\\n\n");
    System.out.println(threeMusketeers[0]);
    System.out.println(threeMusketeers[1] * Math.abs(Math.cos(amount)));
    System.out.println(threeMusketeers[2] * Math.abs(Math.cos(amount)));

    System.out.println(myStalker.isWall + " ENDED");
    System.out.println("\n\n\\n\n");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
