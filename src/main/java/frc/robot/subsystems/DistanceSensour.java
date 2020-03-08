/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.Rev2mDistanceSensor;
import com.revrobotics.Rev2mDistanceSensor.Port;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DistanceSensour extends SubsystemBase {
  /**
   * Creates a new DistanceSensour.
   */

  private Rev2mDistanceSensor distOnboard; 
  private Rev2mDistanceSensor distMXP;
  
  public DistanceSensour() {
    distOnboard = new Rev2mDistanceSensor(Port.kOnboard);
    distMXP = new Rev2mDistanceSensor(Port.kMXP);

    distOnboard.setAutomaticMode(true);

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
    if(distOnboard.isRangeValid()) {
      SmartDashboard.putNumber("Range Onboard", distOnboard.getRange());
      SmartDashboard.putNumber("Timestamp Onboard", distOnboard.getTimestamp());
    }

    if(distMXP.isRangeValid()) {
      SmartDashboard.putNumber("Range MXP", distMXP.getRange());
      SmartDashboard.putNumber("Timestamp MXP", distMXP.getTimestamp());
    }
  }
}
