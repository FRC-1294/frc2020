package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.DriveAutoSubsystem;

import com.revrobotics.ControlType;

public class TurnByCommand extends CommandBase {
  DriveAutoSubsystem m_driveAuto;
  double targetPositionRotations = 0.1335;
  double m_targetLeft;
  double m_targetRight;
  double startingGyro;
  boolean isFinished = false;
  double delta = 50;
  Timer timer = new Timer();
  double recordedTime = 0;

  public TurnByCommand(int amount, DriveAutoSubsystem driveAuto) {
    m_driveAuto = driveAuto;
    m_targetLeft = (amount)*targetPositionRotations + m_driveAuto.getFrontLeftSparkEncoder();
    m_targetRight = (amount)*targetPositionRotations + m_driveAuto.getFrontRightSparkEncoder();

    m_driveAuto.setRamp(0.5);

    timer.start();

    if (m_driveAuto.getSequence()) {
      m_driveAuto.setStep(m_driveAuto.getStep()+1);
      m_driveAuto.setCurrentAngle(m_driveAuto.getCurrentAngle() + amount);
      m_driveAuto.setCurrentAngle(m_driveAuto.getCurrentAngle() % 360);
    }
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_driveAuto.setLock(true);

    m_driveAuto.setFrontLeftPID(m_targetLeft, ControlType.kPosition);
    double leftSpeed = m_driveAuto.getFrontLeftSpeed();
    m_driveAuto.setFrontRightPID(m_targetRight, ControlType.kPosition);
    double rightSpeed = m_driveAuto.getFrontRightSpeed();

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
    m_driveAuto.setFrontLeftSpeed(0);
    m_driveAuto.setFrontRightSpeed(0);
    // m_driveAuto.rearLeftSpark.set(0);
    // m_driveAuto.rearRightSpark.set(0);

    m_driveAuto.setRamp(1);
    m_driveAuto.setLock(false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
