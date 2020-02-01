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

  final double delta = 2 * targetPositionRotations;
  double startingGyro;
  double recordedTime = 0;

  double leftSpeed = m_driveAuto.getFrontLeftSpeed();
  double rightSpeed = m_driveAuto.getFrontRightSpeed();

  public MoveByCommand(int amount, DriveAutoSubsystem driveAuto) {
    m_driveAuto = driveAuto;
    m_targetLeft = (amount)*targetPositionRotations + m_driveAuto.getFrontLeftPosition();
    m_targetRight = (amount)*targetPositionRotations + m_driveAuto.getFrontRightPosition();

    m_driveAuto.setRamp(0.5);
    System.out.println("In command");

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
    leftSpeed = m_driveAuto.getFrontLeftSpeed();
    //final double leftVelocity = m_driveAuto.getFrontLeftVelocity();
    m_driveAuto.setFrontRightPID(m_targetRight, ControlType.kPosition);
    rightSpeed = m_driveAuto.getFrontRightSpeed();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    System.out.println("In end");
    m_driveAuto.setFrontLeftSpeed(0);
    m_driveAuto.setFrontRightSpeed(0);
    m_driveAuto.setRamp(1);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean atPos = false;
    boolean atSpeed = false;

    if (Math.abs(Math.abs(m_targetLeft)-Math.abs(m_driveAuto.getFrontLeftPosition())) < delta) {
      atPos = true;
    }

    if (Math.abs(leftSpeed) <= 0.1 && Math.abs(rightSpeed) <= 0.1) {
      atSpeed = true;
    }

    return atSpeed && atPos;
  }
}
