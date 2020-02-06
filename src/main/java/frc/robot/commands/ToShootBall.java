/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;
import frc.robot.subsystems.*;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class ToShootBall extends CommandBase {
  ShootingBall shooter;
  String action;
  public ToShootBall(ShootingBall shooter, String action) {
    this.shooter = shooter;
    this.action = action;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("ToShootBall Command Initialized");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if(action.equals("intake")){
      shooter.intake();
    } 
    if(action.equals("shoot")){
      shooter.shoot(1.0);
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return true;
  }
}
