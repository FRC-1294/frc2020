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

public class DentMaker extends CommandBase {
  int HAL9000 = 0;
  int amount = 0;
  UltrasonicSubsystem dracula;
  DriveAutoSubsystem whee;
  double[] threeMusketeers = new double[3];
  TurnByCommand tokyoDrift;
  boolean isFinished;

  public DentMaker(int amount, DriveAutoSubsystem driver, UltrasonicSubsystem ultraBurst) {
    this.amount = amount;
    isFinished = false;
    dracula = ultraBurst;
    whee = new DriveAutoSubsystem();
    tokyoDrift = new TurnByCommand(amount, driver);
  }

  @Override
  public void initialize() {
    CommandScheduler.getInstance().schedule(tokyoDrift);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    /*for(double i : threeMusketeers){
      System.out.print(i + " ");
    }
    System.out.println();

    if(!tokyoDrift.isScheduled()){
      if(HAL9000 < threeMusketeers.length){
        System.out.println("WOW HERE " + HAL9000);
        threeMusketeers[HAL9000] = dracula.getSensour();
        HAL9000++;
        tokyoDrift = new TurnByCommand(-amount, whee);
        CommandScheduler.getInstance().schedule(tokyoDrift);
      }
      else if(HAL9000 == threeMusketeers.length){
        System.out.println("ALMOST DONE");
        tokyoDrift = new TurnByCommand(amount, whee);
        HAL9000++;
        CommandScheduler.getInstance().schedule(tokyoDrift);
      }
      else{
        System.out.println("ISFINISHED");
        isFinished = true;
      }
    } */
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
