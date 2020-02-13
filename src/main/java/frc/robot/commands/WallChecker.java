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

public class WallChecker extends CommandBase {
  final int PIDSlot = 1;
  int HAL9000 = 0;
  int amount = 0;
  UltrasonicSubsystem dracula;
  DriveAutoSubsystem whee;
  StalkerRoomba myStalker;
  double[] threeMusketeers = new double[3];
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
    HAL9000 = 0;
    threeMusketeers[HAL9000] = dracula.getSensour();
    tokyoDrift = new TurnByCommand(amount, whee, PIDSlot);
    CommandScheduler.getInstance().schedule(tokyoDrift);
    HAL9000 = 1;
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    for(double i : threeMusketeers){
      System.out.print(i + " ");
    }
    System.out.println();

    if(!tokyoDrift.isScheduled()){
      if(HAL9000 == 1) {
        threeMusketeers[HAL9000] = dracula.getSensour();
        tokyoDrift = new TurnByCommand(-amount * 2, whee, PIDSlot);
        CommandScheduler.getInstance().schedule(tokyoDrift);
        HAL9000 = 2;
      }
      else if(HAL9000 == 2) {
        threeMusketeers[HAL9000] = dracula.getSensour();
        tokyoDrift = new TurnByCommand(amount, whee, PIDSlot);
        CommandScheduler.getInstance().schedule(tokyoDrift);
        HAL9000++;
      }
      else {
        double baseVal = threeMusketeers[0];

        if (threeMusketeers[1] * Math.cos(amount) > baseVal) {
          myStalker.isWall = true;
        }
        else if (threeMusketeers[1] * Math.cos(amount) > baseVal) {
          myStalker.isWall = true;
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
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
