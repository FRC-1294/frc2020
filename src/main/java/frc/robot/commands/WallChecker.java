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
  final double margin = 12;
  int HAL9000 = 0;
  int amount = 0;
  UltrasonicSubsystem dracula;
  DriveAutoSubsystem whee;
  double[] threeMusketeers = new double[3];
  double[] threeAngles = new double[3];
  TurnByCommand tokyoDrift;
  boolean isFinished;

  Timer timer = new Timer();

  public WallChecker(int amount, DriveAutoSubsystem driver, UltrasonicSubsystem ultraBurst) {
    this.amount = amount;
    dracula = ultraBurst;
    whee = driver;
  }

  @Override
  public void initialize() {
    whee.setFrontLeftSpeed(0);
    whee.setFrontRightSpeed(0);
    whee.setRearLeftSpeed(0);
    whee.setRearRightSpeed(0);
    
    threeMusketeers = new double[3];
    threeAngles = new double[3];

    timer = new Timer();
    timer.start();
    timer.reset();
    
    HAL9000 = 1;
    isFinished = false;
    threeMusketeers[0] = dracula.getSensourLeft();
    threeAngles[0] = whee.getCurrentAngle();
    tokyoDrift = new TurnByCommand(amount, whee, PIDSlot);
    tokyoDrift.schedule();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(!tokyoDrift.isScheduled()){
      if(HAL9000 == 1) {
        if (timer.get() > 1) {
          System.out.println();
          threeMusketeers[HAL9000] = dracula.getSensourLeft();
          tokyoDrift = new TurnByCommand(-amount * 2, whee, PIDSlot);
          tokyoDrift.schedule();
          HAL9000 = 2;
        }
      }
      else if(HAL9000 == 2) {
        if (timer.get() > 1) {
          threeMusketeers[HAL9000] = dracula.getSensourLeft();
          threeAngles[HAL9000-1] = whee.getCurrentAngle();
          tokyoDrift = new TurnByCommand(amount, whee, PIDSlot);
          tokyoDrift.schedule();
          HAL9000 = 3;
        }
      }
      else {
        threeAngles[HAL9000-1] = whee.getCurrentAngle();

        double baseVal = threeMusketeers[0];

        // if (baseValLeft <= baseValRight + margin && baseValLeft >= baseValRight - margin) {
          
        if (baseVal <= baseVal + margin && baseVal >= baseVal - margin) {
          if (threeMusketeers[1] * Math.abs(Math.cos(amount * Math.PI / 180)) > baseVal + margin) {
            whee.setWall(false);
          }
          else if (threeMusketeers[2] * Math.abs(Math.cos(amount* Math.PI / 180)) > baseVal + margin) {
            whee.setWall(false);
          }
          else {
            whee.setWall(true);

          }
        }
        else {
          whee.setWall(false);
        }

        isFinished = true;
      }
    }
    else {
      timer.reset();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    whee.setFrontRightSpeed(0);
    whee.setFrontLeftSpeed(0);
    whee.setRearRightSpeed(0);
    whee.setRearLeftSpeed(0);

    System.out.println("\n\n\n\n");
    System.out.println(threeMusketeers[0]);
    System.out.println(threeMusketeers[1]);// * Math.abs(Math.cos(amount * Math.PI / 180)));
    System.out.println(threeMusketeers[2]);// * Math.abs(Math.cos(amount * Math.PI / 180)));

    System.out.println(whee.getWall() + " ENDED");
    System.out.println("\n\n\n\n");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
