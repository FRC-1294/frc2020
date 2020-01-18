/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import frc.robot.Robot;
import frc.robot.RobotMap;


/**
 * Add your docs here.
 * 
 */
public class RobotMoves extends Subsystem {
  // Put methods for controlling this subsystem
  // here. Call these from Commands.
WPI_TalonSRX rightFrontTalon = new WPI_TalonSRX(RobotMap.rightFrontTalon);
  WPI_TalonSRX rightRearTalon = new WPI_TalonSRX(RobotMap.rightRearTalon);
  WPI_TalonSRX leftFrontTalon = new WPI_TalonSRX(RobotMap.leftFrontTalon);
  WPI_TalonSRX leftRearTalon = new WPI_TalonSRX(RobotMap.rightRearTalon);

  SpeedControllerGroup rightTalons = new SpeedControllerGroup(rightFrontTalon, rightRearTalon);
  SpeedControllerGroup leftTalons = new SpeedControllerGroup(leftFrontTalon, leftRearTalon);

  DifferentialDrive tankTreads = new DifferentialDrive(rightTalons, leftTalons);


public void arcadeDrive(double forward, double turn)
{
  tankTreads.arcadeDrive(forward, turn);
}
public void Stop(boolean AIsPressed)
{
  if(AIsPressed)
  {
  tankTreads.stopMotor();
  }
}

public void ControllerRumbles(boolean YIsPressed)
{
  if(Robot.m_oi.YisPressed())
  {
    Robot.m_oi.driveControl.setRumble(RumbleType.kLeftRumble,1.0);
  }
}


  // Put methods for controlling this subsystem
  // here. Call these from Commands.

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
