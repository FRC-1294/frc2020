package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;

public class UltrasonicSubsystem extends SubsystemBase {

  // factor to convert sensor values to a distance in inches
  public static final int MIN_DIS = 32;
  private static final double kValueToInches = 0.0528;
  private static final int kUltrasonicPort = 0;

  private final static AnalogInput m_ultrasonic = new AnalogInput(kUltrasonicPort);
  XboxController stick = new XboxController(0);

  public double getSensour() {
    double currentDistance = m_ultrasonic.getValue() * kValueToInches;
    return currentDistance;
  }

  public void periodic() {
    System.out.println(getSensour());
  }
}