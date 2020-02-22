/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import frc.robot.Constants;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.turnReadColor;

public class ColorSensor extends SubsystemBase {
  /**
   * Creates a new ColorSensor.
   */
  private XboxController controller = new XboxController(0);
  private static ColorSensorV3 colour;
  private String desiredColor;
  private static I2C.Port pourt = I2C.Port.kOnboard;
  private static final int red = Integer.parseInt("bf7dec", 16);
  private static final int blue = Integer.parseInt("52739c", 16);
  private static final int yellow = Integer.parseInt("989682", 16);
  private static final int green = Integer.parseInt("638164", 16);
  private static final ArrayList<String> colorValList = new ArrayList<String>(List.of("RED", "YELLOW", "BLUE", "GREEN"));
  private String currentColor; 
  public TalonSRX colorMotor = new TalonSRX(3);

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
    parser.close();
    readColorClosest(hex);

  }
  public void readColorRange(String hex){
    // System.out.println(hex);
    String color = hex.substring(0, 6);
    int colVal = Integer.parseInt(color, 16);
    int range = 500000;
    if(colVal >= red-range && colVal <= red+range){
      currentColor = "RED";
    } else if(colVal >= blue-range && colVal <= blue+range){ 
      currentColor = "BLUE"; 
    } else if(colVal >= yellow-range && colVal <= yellow+range){
      currentColor = "YELLOW"; 
    } else if(colVal >= green-range && colVal <= green+range){
      currentColor = "GREEN"; 
    } else { 
      currentColor = null; 
    }
  }


  public static void readColorClosest(String hex) {
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

  public String getColor(){
    int index = colorValList.indexOf(currentColor);
    index += 2;
    if(index > 3){
      index -= 4;
    }
    return colorValList.get(index);
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

  public void setSpeed(double speed){
    colorMotor.set(ControlMode.PercentOutput, speed);
  }

/**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  public void robotPeriodic() {
    if(controller.getYButtonPressed()){
      CommandScheduler.getInstance().schedule(new turnReadColor(this, desiredColor));
    }
    
  }
}