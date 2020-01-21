package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Robot;
import com.revrobotics.ControlType;

public class MoveByCommand extends CommandBase {
  double targetPositionRotations = 1000;
  double m_amountLeft;
  double m_amountRight;
  double recordedTime = 0;
  double delta = 2;
  boolean isFinished = false;
  Timer timer = new Timer();

  public MoveByCommand(int amount) {
    m_amountLeft = amount*targetPositionRotations + Robot.driver.frontLeftSpark.getEncoder().getPosition();
    m_amountRight = amount*targetPositionRotations + Robot.driver.frontRightSpark.getEncoder().getPosition();
    timer.start();
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    Robot.driver.frontLeftPID.setReference(m_amountLeft, ControlType.kPosition);
    Robot.driver.frontRightPID.setReference(m_amountRight, ControlType.kPosition);
    Robot.driver.rearLeftTalon.set(Robot.driver.frontLeftSpark.get());
    Robot.driver.rearRightTalon.set(Robot.driver.frontRightSpark.get());
    
    if (Robot.driver.frontLeftSpark.getEncoder().getPosition() == m_amountLeft && Robot.driver.frontRightSpark.getEncoder().getPosition() == m_amountRight) {
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
  }

  // Returns true when the command should end.3
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}