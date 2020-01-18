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
import edu.wpi.first.wpilibj.util.Color;
import java.util.Scanner;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ColorSensor extends SubsystemBase {
  /**
   * Creates a new ColorSensor.
   */
  private ColorSensorV3 colour;
  private I2C.Port pourt = I2C.Port.kOnboard;

  public ColorSensor() {
    colour = new ColorSensorV3(pourt);
  }

  @Override
  public void periodic() {
    SmartDashboard.putString("Colour", Integer.toString(colour.hashCode()));
    String hexString = colour.getColor().toString();
    Scanner parser = new Scanner(hexString).useDelimiter("@");
    parser.next();
    String hex = parser.next();
    System.out.println(hex);
    /*
    String color = hex.substring(0,1);
    int colVal = Integer.parseInt(color="0", 16);

    int red = Integer.parseInt("c0", 16);
    int blue = Integer.parseInt("40", 16);
    int yellow = Integer.parseInt("90", 16);
    int green = Integer.parseInt("50", 16);
    int range = 10;
    if(colVal >= red-range && colVal <= red+range){
      System.out.println("RED \n");
    } else if(colVal >= blue-range && colVal <= blue+range){
      System.out.println("BLUE \n");
    } else if(colVal >= yellow-range && colVal <= yellow+range){
      System.out.println("YELLOW \n");
    } else if(colVal >= green-range && colVal <= green+range){
      System.out.println("GREEN \n");
    } else {
      System.out.println("NOT WORKING");
    }
    */
  }

  public static int closeTo(int value, int comp1, int comp2, int comp3, int comp4){
    comp1 = Math.abs(value - comp1);
    comp2 = Math.abs(value - comp2);
    comp3 = Math.abs(value - comp3);
    comp4 = Math.abs(value - comp4);
    int greater1 = lesser(comp1, comp2);
    int greater2 = lesser(comp3, comp4);
    int greatest = lesser(greater1, greater2);
    return greatest;
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
