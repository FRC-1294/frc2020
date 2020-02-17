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
import frc.robot.subsystems.DriveAutoSubsystem;


public class TwentyThreeStabWounds extends SubsystemBase {
  NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
  NetworkTableEntry tx = table.getEntry("tx");
  NetworkTableEntry tv = table.getEntry("tv");
  
  DriveAutoSubsystem drive = new DriveAutoSubsystem();

  public static boolean targetAquired = false;
  public static double horizontalOffset = 0.0f;

  public double distanceFromTarget;

  public TwentyThreeStabWounds() {    
    
  }

  public void pollCamera() {
    targetAquired = tv.getBoolean(false);
    horizontalOffset = tx.getDouble(0.0);

    SmartDashboard.putBoolean("Limelight/TargetAquired", targetAquired);
    SmartDashboard.putNumber("Limelight/HorizontalOffset", horizontalOffset);
  }

  public double getHorizontalOffSet(){
    return -horizontalOffset;
  }

  public double getHeadingError() {
    return tx.getDouble(0.0) * -1;
  }

  public static void getInRange()  {
    // float kpDistance = -0.1f;
    // float currentDistance = Estimate_Distance();

    // float distanceError = desiredDistance - currentDistance;
    // drivingAdjust = kpDistance + distanceError;

    // leftCommand += distanceAdjust;
    // rightCommand += distanceAdjust;
  }

  @Override
  public void periodic() {   
    pollCamera();
    // System.out.println("HERE");
  } 
}