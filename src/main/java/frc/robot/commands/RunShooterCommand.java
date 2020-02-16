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

public class RunShooterCommand extends CommandBase {
  final int MARGIN = 50;

  ShootingBall m_shooter;
  int m_target;

  int speed;
  Timer timer = new Timer();

  public RunShooterCommand(ShootingBall shooter, int target) {
    m_shooter = shooter;
    m_target = target;
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
    speed = m_shooter.getShooterVelocity();
    m_shooter.setShooterPID(m_target);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean atSpeed = false;
    boolean timeHold = false;

    if (Math.abs(speed) <= m_target+MARGIN && Math.abs(speed) >= m_target+MARGIN) {
      atSpeed = true;
    }

    if (atSpeed) {
      if(timer.get() >= 1){
        timeHold = true;
      }
    }
    else {
      timer.reset();
    }

    return atSpeed && timeHold;
  }
}
