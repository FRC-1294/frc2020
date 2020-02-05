package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class UltrasonicSubsystem extends SubsystemBase {

  // factor to convert sensor values to a distance in inches
  private static final double kValueToInches = 0.0528;
  private static final int kUltrasonicPort = 1;

  private final static AnalogInput m_ultrasonic = new AnalogInput(kUltrasonicPort);
  XboxController stick = new XboxController(0);
  JoystickButton abutton = new JoystickButton(stick, 1);
  JoystickButton bButton = new JoystickButton(stick, 2);
  JoystickButton yButton = new JoystickButton(stick, 3);

  public double getSensour() {
    double currentDistance = m_ultrasonic.getValue() * kValueToInches;
    return currentDistance;
  }

  public void periodic() {
    //System.out.println(getSensour());
  }
}