package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import com.revrobotics.ControlType;

import frc.robot.commands.AutoPath;
import frc.robot.subsystems.DriveAutoSubsystem;

public class MoveByCommand extends CommandBase {
  DriveAutoSubsystem m_driveAuto;
  double targetPositionRotations = 0.54;
  double m_targetLeft;
  double m_targetRight;

  double startingGyro;
  boolean isFinished = false;
  double delta = 50;
  Timer timer = new Timer();
  double recordedTime = 0;

  public MoveByCommand(int amount, DriveAutoSubsystem driveAuto) {
    m_driveAuto = driveAuto;
    m_targetLeft = (amount)*targetPositionRotations + m_driveAuto.getFrontLeftSparkEncoder();
    m_targetRight = (amount)*targetPositionRotations + m_driveAuto.getFrontRightSparkEncoder();

    m_driveAuto.setRamp(0.5);
    System.out.println("In command");

    timer.start();
    int currentAngle = Math.abs(m_driveAuto.getCurrentAngle());
    m_driveAuto.setAmountTraveled(0, (int)Math.cos(currentAngle) * amount);

  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("In intitialize");
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println("in execute");
    m_driveAuto.setFrontLeftPID(m_targetLeft, ControlType.kPosition);
    final double leftSpeed = m_driveAuto.getFrontLeftSpeed();
    //final double leftVelocity = m_driveAuto.getFrontLeftVelocity();
    m_driveAuto.setFrontRightPID(m_targetRight, ControlType.kPosition);
    final double rightSpeed = m_driveAuto.getFrontRightSpeed();

    // m_driveAuto.rearLeftSpark.set(leftSpeed);
    // m_driveAuto.rearRightSpark.set(rightSpeed);

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
    System.out.println("In end");
    m_driveAuto.setFrontLeftSpeed(0);
    m_driveAuto.setFrontRightSpeed(0);
    // m_driveAuto.rearLeftSpark.set(0);
    // m_driveAuto.rearRightSpark.set(0);
    m_driveAuto.setRamp(1);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
