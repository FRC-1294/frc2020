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
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;
// import edu.wpi.first.wpilibj.hal.I2CJNI;


public class LIDAR extends SubsystemBase {
  /**
   * Creates a new LIDAR.
   */
  I2C LidarPort;
  private static final byte deviceAddress=0x62;
  LIDARCommand workPlz = new LIDARCommand();
  
  public LIDAR() {
    LidarPort=new I2C(I2C.Port.kOnboard,deviceAddress);
    
  } 
  public double getDistance(){
    byte[] temp = new byte[1];
    LidarPort.read(0x10, 1, temp);
    return temp[0];
  }
  protected void initDefaultCommand() {
  }

  @Override
  public void periodic() {
    if (!workPlz.isScheduled()) {
      workPlz.schedule();
    }
    // This method will be called once per scheduler run
  }

}
