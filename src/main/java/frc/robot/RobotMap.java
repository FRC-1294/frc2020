/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
<<<<<<< Updated upstream
public static int rightFrontTalon = 1;
public static int rightRearTalon = 3;
=======
  public static final int frontRightSpark = 11;
  public static final int frontLeftSpark = 12;
//  public static final int rearRightSpark = 13;
  public static final int rearLeftSpark = 10;   
  
  public static final int intakeTalon = 21;
  public static final int indexerTalon = 23;
  public static final int shooterFalcon = 25;
  public static final int colorTalon = 27;

>>>>>>> Stashed changes

public static int leftFrontTalon = 2;
public static int leftRearTalon = 4;
  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;
}
