package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.Gains;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.revrobotics.ControlType;

public class TurnByCommand extends CommandBase {
  double targetPositionRotations = 0.1335;
  double m_targetLeft;
  double m_targetRight;
  double startingGyro;
  boolean isFinished = false;
  double delta = 50;
  Timer timer = new Timer();
  double recordedTime = 0;

  public TurnByCommand(int amount) {
    m_targetLeft = (amount)*targetPositionRotations + Robot.driver.frontLeftSpark.getEncoder().getPosition();
    m_targetRight = -(amount)*targetPositionRotations+ Robot.driver.frontRightSpark.getEncoder().getPosition();

    Robot.driver.frontLeftSpark.setClosedLoopRampRate(0.5);
    Robot.driver.frontRightSpark.setClosedLoopRampRate(0.5);

    timer.start();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    DriveSubsystem.lock = true;

    Robot.driver.frontLeftPID.setReference(m_targetLeft, ControlType.kPosition);
    double leftSpeed = Robot.driver.frontLeftSpark.get();
    Robot.driver.frontRightPID.setReference(m_targetRight, ControlType.kPosition);
    double rightSpeed = Robot.driver.frontRightSpark.get();

    Robot.driver.rearLeftTalon.set(ControlMode.Velocity, leftSpeed);
    Robot.driver.rearRightTalon.set(ControlMode.Velocity, rightSpeed);

    if (Math.abs(leftSpeed) <= 0.1 && Math.abs(rightSpeed) <= 0.1) {
      if (timer.get() - recordedTime >= 1) {
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

    Robot.driver.frontLeftSpark.setClosedLoopRampRate(1);
    Robot.driver.frontRightSpark.setClosedLoopRampRate(1);

    Robot.driver.lock = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
