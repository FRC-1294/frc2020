/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.commands.LIDARCommand;

public class LIDAR extends SubsystemBase {
  /**
   * Creates a new LIDAR.
   */

  LIDARCommand workPlz = new LIDARCommand();
  
  public LIDAR() {
  } 

  protected void initDefaultCommand() {
  }

  @Override
  public void periodic() {
    System.out.println("HERE");
    if (!workPlz.isScheduled()) {
      System.out.println("SCHEDULING");
      workPlz.schedule();
    }
    // This method will be called once per scheduler run
  }

}
