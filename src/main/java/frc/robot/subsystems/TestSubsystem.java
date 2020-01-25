/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.commands.TestCommand;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Robot;


public class TestSubsystem extends Subsystem {
  CANSparkMax rightFrontTalon = new CANSparkMax(RobotMap.rightFrontTalon, MotorType.kBrushless);
  CANSparkMax rightRearTalon = new CANSparkMax(RobotMap.rightRearTalon, MotorType.kBrushless);
  CANSparkMax leftFrontTalon = new CANSparkMax(RobotMap.leftFrontTalon, MotorType.kBrushless);
  CANSparkMax leftRearTalon = new CANSparkMax(RobotMap.leftRearTalon, MotorType.kBrushless);
  SpeedControllerGroup sparkDriveLeft = new SpeedControllerGroup(leftFrontTalon,leftRearTalon);
  SpeedControllerGroup sparkDriveRight = new SpeedControllerGroup(rightFrontTalon,rightRearTalon);
  DifferentialDrive sparkDrive = new DifferentialDrive(sparkDriveLeft,sparkDriveRight);
  XboxController driveControl = new XboxController(0);

  public TestSubsystem() {
    rightFrontTalon.setSmartCurrentLimit(60);
    rightRearTalon.setSmartCurrentLimit(60);
    leftFrontTalon.setSmartCurrentLimit(60);
    leftRearTalon.setSmartCurrentLimit(60);

    rightFrontTalon.setClosedLoopRampRate(5);
    rightRearTalon.setClosedLoopRampRate(5);
    leftFrontTalon.setClosedLoopRampRate(5);
    leftRearTalon.setClosedLoopRampRate(5);
    // leftFrontTalon.setInverted(false);
    // rightFrontTalon.setInverted(true);
    // leftRearTalon.setInverted(false);
    // rightRearTalon.setInverted(true);
  }

  public void arcadeDrive(double forward, double turn) {
    forward*=0.8;
    turn*=0.8;
    
    sparkDrive.arcadeDrive(forward, -turn);
  }

  public void execute() {
    // SmartDashboard.putNumber("PID/leftEncoder", rightFrontTalon.getEncoder().getVelocity());
    // SmartDashboard.putNumber("PID/rightEncoder", rightRearTalon.getEncoder().getVelocity());
    // SmartDashboard.putNumber("PID/leftSpeed", leftFrontTalon.getEncoder().getVelocity());
    // SmartDashboard.putNumber("PID/rightSpeed", leftRearTalon.getEncoder().getVelocity());
  }

  public void initDefaultCommand() {
    setDefaultCommand(new TestCommand());
  }
}