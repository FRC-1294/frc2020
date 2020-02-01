package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.subsystems.DriveAutoSubsystem;

import com.revrobotics.ControlType;

public class TurnByCommand extends CommandBase {
  DriveAutoSubsystem m_driveAuto;
  int m_amount;
  double targetPositionRotations = 0.1;
  double m_targetLeft;
  double m_targetRight;
  double startingGyro;
  double delta;
  Timer timer;
  double recordedTime = 0;

  double leftSpeed;
  double rightSpeed;

  public TurnByCommand(int amount, DriveAutoSubsystem driveAuto) {
    m_driveAuto = driveAuto;
    m_amount = amount;
    timer = new Timer();
    delta = amount*0.1 * targetPositionRotations;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    System.out.println("In intitialize");

    m_targetLeft = -(m_amount)*targetPositionRotations + m_driveAuto.getFrontLeftPosition();
    m_targetRight = -(m_amount)*targetPositionRotations + m_driveAuto.getFrontRightPosition();

    m_driveAuto.setRamp(0.5);
    System.out.println("In command");

    timer.start();

    m_driveAuto.setCurrentAngle(m_driveAuto.getCurrentAngle() + m_amount);
    m_driveAuto.setCurrentAngle(m_driveAuto.getCurrentAngle() % 360);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    System.out.println("in TurnCommand");
    m_driveAuto.setFrontLeftPID(m_targetLeft, ControlType.kPosition);
    leftSpeed = m_driveAuto.getFrontLeftSpeed();
    m_driveAuto.setFrontRightPID(m_targetRight, ControlType.kPosition);
    rightSpeed = m_driveAuto.getFrontRightSpeed();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveAuto.setFrontLeftSpeed(0);
    m_driveAuto.setFrontRightSpeed(0);

    m_driveAuto.setRamp(1);
    System.out.println("end");
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    boolean atPos = false;
    boolean atSpeed = false;
    boolean timeHold = false;

    if (Math.abs(Math.abs(m_targetLeft)-Math.abs(m_driveAuto.getFrontLeftPosition())) < delta
    && Math.abs(Math.abs(m_targetRight)-Math.abs(m_driveAuto.getFrontRightPosition())) < delta) {
      atPos = true;
    }

    if (Math.abs(leftSpeed) <= 0.1 && Math.abs(rightSpeed) <= 0.1) {
      atSpeed = true;
    }

    if (atSpeed) {
      if(timer.get() >= 0.3){
        timeHold = true;
      }
    }
    else {
      timer.reset();
    }

    System.out.println(timer.get());

    return atSpeed && timeHold;
  }
}
