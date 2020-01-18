/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import frc.robot.commands.UltraSonicAutoDrive;

/**
 * Add your docs here.
 */
public class Ultrasonic extends Subsystem {
  final double kValueToInches = 0.125;
  final AnalogInput m_ultrasonic = new AnalogInput(Robot.m_Map.UltraSonic);
  double currentDistance = m_ultrasonic.getValue() * kValueToInches;
  public double getSensour() {
    
    return currentDistance;
  }
  @Override
  public void initDefaultCommand() {
    setDefaultCommand(new UltraSonicAutoDrive());
    
  }
}
