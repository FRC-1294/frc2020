package frc.robot;

import frc.robot.commands.AutoNavCommand;
import frc.robot.commands.DictatorLocator;
import frc.robot.subsystems.UltrasonicSubsystem;
import frc.robot.subsystems.TwentyThreeStabWounds;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.DrivingSubsystem;
import frc.robot.subsystems.ShootingBall;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static Command m_autonomousCommand;
  public static UltrasonicSubsystem ultrasonic;
  public static TwentyThreeStabWounds cassius;
  public static ShootingBall letsShoot;
  public static DrivingSubsystem driver;

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    letsShoot = new ShootingBall();
    driver = new DrivingSubsystem();
    ultrasonic = new UltrasonicSubsystem();
    cassius = new TwentyThreeStabWounds();
  }
  
  @Override
  public void robotPeriodic() {
    //CommandScheduler.getInstance().run();
  }
  
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
    driver.m_driveAuto.setFrontLeftSpeed(0);
    driver.m_driveAuto.setFrontRightSpeed(0);
    driver.m_driveAuto.setRearLeftSpeed(0);
    driver.m_driveAuto.setRearRightSpeed(0);
    letsShoot.setZero();
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    driver.m_driveAuto.resetEncoders();

    m_autonomousCommand = new AutoNavCommand(driver.m_driveAuto, ultrasonic, letsShoot, cassius);//new DictatorLocator(cassius, driveAuto);

    // schedule the autonomous command (example)
    if (!m_autonomousCommand.isScheduled()) {
      m_autonomousCommand = new DictatorLocator(cassius, driver.m_driveAuto);
      m_autonomousCommand.schedule();
    }
  }

  /**
   * This function is called periodically during autonomo us.
   */
  @Override
  public void autonomousPeriodic() {
    // if (!m_autonomousCommand.isFinished() && !m_autonomousCommand.isScheduled()) {
    //   m_autonomousCommand =  new AutoNavCommand(driveAuto, ultrasonic);
    //   m_autonomousCommand.schedule();
    // }
  }

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
