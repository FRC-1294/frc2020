/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class VisionSubsystem extends SubsystemBase {
  /**
   * Creates a new VisionSubsystem.
   */

  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry tv = table.getEntry("tv");

  public static boolean targetAquired = false;
  public static double horizontalOffset = 0.0f;

  public VisionSubsystem() {    

  }

  public void pollCamera() {

    targetAquired = tv.getBoolean(false);
    horizontalOffset = tx.getDouble(0.0);


    SmartDashboard.putBoolean("Limelight/TargetAquired", targetAquired);
    SmartDashboard.putNumber("Limelight/HorizontalOffset", horizontalOffset);


       
  }
  public static void getInRange()  {

    /*
    float kpDistance = -0.1f;
    float currentDistance = Estimate_Distance();

    float distanceError = desiredDistance - currentDistance;
    drivingAdjust = kpDistance + distanceError;

    leftCommand += distanceAdjust;
    rightCommand += distanceAdjust;
    */

  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run

    /*
    if(Robot.m_oi.Abutton()) {

    }
    */

    pollCamera();

  } 

}
