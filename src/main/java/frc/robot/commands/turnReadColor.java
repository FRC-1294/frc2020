/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.ColorSensor;
import frc.robot.subsystems.LinearActuator;

public class turnReadColor extends CommandBase {
  private String color;
  private ColorSensor colorReader;
  private boolean done = false;
  private String desired;
  private Timer timer;

  public turnReadColor(ColorSensor colorReader, String desired) {
    this.colorReader = colorReader;
    this.desired = desired;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    Robot.raiser.linActuator.set(0.609);
    timer.start();
    timer.reset();
    colorReader.setSpeed(0.5);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.raiser.linActuator.set(0.609);
    color = colorReader.getColor();
    if(color.equals(desired)){
      colorReader.setSpeed(0);
      if (timer.get() > 1) done = true;
    } else {
      colorReader.setSpeed(0.5);
      timer.reset();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.raiser.linActuator.set(0.2);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return done;
  }
}
