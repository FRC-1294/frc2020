package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.ControlType;

public class TurnByCommand extends CommandBase {
  double targetPositionRotations = 0.1320111168755426;
  double m_target;
  double startingGyro;
  boolean isFinished = false;
  double delta = 50;
  Timer timer = new Timer();
  double recordedTime = 0;

  public TurnByCommand(int amount) {
    Robot.driver.frontLeftSpark.getEncoder().setPosition(0);
    Robot.driver.frontRightSpark.getEncoder().setPosition(0);
    m_target = (amount)*targetPositionRotations;
    System.out.println(m_target);
    timer.start();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.driver.lock = true;
    Robot.driver.frontLeftPID.setReference(m_target, ControlType.kPosition);
    Robot.driver.frontRightPID.setReference(-m_target, ControlType.kPosition);
    //Robot.driver.rearLeftTalon.set(Robot.driver.frontLeftSpark.get());
    //Robot.driver.rearRightTalon.set(Robot.driver.frontRightSpark.get());
    if (Robot.driver.frontLeftSpark.getEncoder().getPosition() == m_target && Robot.driver.frontRightSpark.getEncoder().getPosition() == -m_target) {
      if (recordedTime-timer.get() >= 3) {
        isFinished = true;
      }
    }
    else {
      recordedTime = timer.get();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    Robot.driver.frontLeftSpark.set(0);
    Robot.driver.frontRightSpark.set(0);
    Robot.driver.rearLeftTalon.set(0);
    Robot.driver.rearRightTalon.set(0);
    Robot.driver.lock = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
