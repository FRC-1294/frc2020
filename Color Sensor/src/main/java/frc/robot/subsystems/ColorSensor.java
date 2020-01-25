/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.util.Scanner;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensor extends SubsystemBase {
  /**
   * Creates a new ColorSensor.
   */
  private ColorSensorV3 colour;
  private I2C.Port pourt = I2C.Port.kOnboard;
  private static int red = Integer.parseInt("bf7dec", 16);
  private static int blue = Integer.parseInt("52739c", 16);
  private static int yellow = Integer.parseInt("989682", 16);
  private static int green = Integer.parseInt("638164", 16);

  public ColorSensor() {
    colour = new ColorSensorV3(pourt);
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("Colour", Integer.toString(colour.hashCode()));
    String hexString = colour.getColor().toString();
    readColor(hexString);

    /*
     * String color = hex.substring(0,1); int colVal = Integer.parseInt(color="0",
     * 16);
     * 
     * int red = Integer.parseInt("c0", 16); int blue = Integer.parseInt("40", 16);
     * int yellow = Integer.parseInt("90", 16); int green = Integer.parseInt("50",
     * 16); int range = 10; if(colVal >= red-range && colVal <= red+range){
     * System.out.println("RED \n"); } else if(colVal >= blue-range && colVal <=
     * blue+range){ System.out.println("BLUE \n"); } else if(colVal >= yellow-range
     * && colVal <= yellow+range){ System.out.println("YELLOW \n"); } else if(colVal
     * >= green-range && colVal <= green+range){ System.out.println("GREEN \n"); }
     * else { System.out.println("NOT WORKING"); }
     */
  }

  public static void readColor(String hexString) {
    Scanner parser = new Scanner(hexString).useDelimiter("@");
    parser.next();
    String hex = parser.next();
    // System.out.println(hex);
    String color = hex.substring(0, 6);
    int colVal = Integer.parseInt(color, 16);
    // System.out.println(colVal);
    // System.out.println(red + " " + blue + " " + yellow + " " + green);
    int result = closeTo(colVal, red, blue, yellow, green);
    //System.out.println(result);
    if(result == red){
      System.out.println("RED \n");
      //return "RED";
    } else if(result == blue){
      System.out.println("BLUE \n");
      //return "BLUE";
    } else if(result == yellow){
      System.out.println("YELLOW \n");
      //return "YELLOW";
    } else if(result == green){
      System.out.println("GREEN \n");
      //return "GREEN";
    } else {
      System.out.println("HAHA RIP \n");
    }
  }
  public static int closeTo(int value, int comp1, int comp2, int comp3, int comp4){
    int dif1 = Math.abs(value - comp1);
    int dif2 = Math.abs(value - comp2);
    int dif3 = Math.abs(value - comp3);
    int dif4 = Math.abs(value - comp4);
    //System.out.println(comp1 + " " + comp2 + " " + comp3 + " " + comp4);
    int lesser1 = lesser(dif1, dif2);
    int lesser2 = lesser(dif3, dif4);
    int least = lesser(lesser1, lesser2);
    //System.out.println(least);
    if(least == dif1){
      return comp1;
    } else if (least == dif2){
      return comp2;
    } else if (least == dif3){
      return comp3;
    } else {
      return comp4;
    }
  }

  public static int lesser(int a, int b){
    if(a <= b){
      return a;
    } else {
      return b;
    }
  }

/**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }
}