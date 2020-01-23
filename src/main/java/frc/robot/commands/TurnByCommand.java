package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;
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
    m_targetLeft = (amount)*targetPositionRotations + Robot.driveAuto.frontLeftSpark.getEncoder().getPosition();
    m_targetRight = -(amount)*targetPositionRotations+ Robot.driveAuto.frontRightSpark.getEncoder().getPosition();

    Robot.driveAuto.frontLeftSpark.setClosedLoopRampRate(0.5);
    Robot.driveAuto.frontRightSpark.setClosedLoopRampRate(0.5);

    timer.start();

    if (Robot.driveAuto.sequence) {
      Robot.driveAuto.step++;
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.driveAuto.lock = true;

    Robot.driveAuto.frontLeftPID.setReference(m_targetLeft, ControlType.kPosition);
    double leftSpeed = Robot.driveAuto.frontLeftSpark.get();
    Robot.driveAuto.frontRightPID.setReference(m_targetRight, ControlType.kPosition);
    double rightSpeed = Robot.driveAuto.frontRightSpark.get();

    Robot.driveAuto.rearLeftTalon.set(ControlMode.Velocity, leftSpeed);
    Robot.driveAuto.rearRightTalon.set(ControlMode.Velocity, rightSpeed);

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
    Robot.driveAuto.frontLeftSpark.set(0);
    Robot.driveAuto.frontRightSpark.set(0);
    Robot.driveAuto.rearLeftTalon.set(0);
    Robot.driveAuto.rearRightTalon.set(0);

    Robot.driveAuto.frontLeftSpark.setClosedLoopRampRate(1);
    Robot.driveAuto.frontRightSpark.setClosedLoopRampRate(1);

    Robot.driveAuto.lock = false;
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
