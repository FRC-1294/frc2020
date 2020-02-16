/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShootingBall;

public class ShooterCommand extends CommandBase {
  ShootingBall m_shooter;
  double targetTime;
  Timer timer;

  public ShooterCommand(ShootingBall shooter, double time) {
    m_shooter = shooter;
    targetTime = time;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.start();
    timer.reset();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_shooter.setIndexer(1);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_shooter.setIndexer(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return (timer.get() >= targetTime);
  }
}
