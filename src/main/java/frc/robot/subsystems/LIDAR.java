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
  public LIDAR() {

    LIDARCommand l = new LIDARCommand();

    CommandScheduler.getInstance().schedule(l);
  }  

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }

}
