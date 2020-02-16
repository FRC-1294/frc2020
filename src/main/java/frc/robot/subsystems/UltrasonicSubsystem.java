package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;

public class UltrasonicSubsystem extends SubsystemBase {

  // factor to convert sensor values to a distance in inches
  public static final int MIN_DIS = 32;
  private static final double kValueToInches = 0.0528;

  private final static AnalogInput m_ultrasonicLeft = new AnalogInput(0);
  private final static AnalogInput m_ultrasonicRight = new AnalogInput(1);

  public double getSensourLeft() {
    double currentDistance = m_ultrasonicLeft.getValue() * kValueToInches;
    return currentDistance;
  }

  public double getSensourRight() {
    double currentDistance = m_ultrasonicRight.getValue() * kValueToInches;
    return currentDistance;
  }

  public void periodic() {
    //System.out.println(getSensourLeft() + " , " + getSensourRight());
  }
}