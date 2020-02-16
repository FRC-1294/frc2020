/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotMap;

public class DrivingSubsystem extends SubsystemBase {
  /**
   * Creates a new DrivingSubsystem.
   */
  private CANSparkMax frontRightSpark = new CANSparkMax(RobotMap.frontRightSpark, MotorType.kBrushless);
  private CANSparkMax rearRightSpark = new CANSparkMax(RobotMap.rearRightSpark, MotorType.kBrushless);
  private CANSparkMax frontLeftSpark = new CANSparkMax(RobotMap.frontLeftSpark, MotorType.kBrushless);
  private CANSparkMax rearLeftSpark = new CANSparkMax(RobotMap.rearLeftSpark, MotorType.kBrushless);
  private SpeedControllerGroup sparkDriveLeft = new SpeedControllerGroup(frontLeftSpark,rearLeftSpark);
  private SpeedControllerGroup sparkDriveRight = new SpeedControllerGroup(frontRightSpark,rearRightSpark);
  private DifferentialDrive sparkDrive = new DifferentialDrive(sparkDriveLeft,sparkDriveRight);
  private XboxController driveJoystick = new XboxController(0);

  public DrivingSubsystem() {
    frontRightSpark.setSmartCurrentLimit(60);
    rearRightSpark.setSmartCurrentLimit(60);
    frontLeftSpark.setSmartCurrentLimit(60);
    rearLeftSpark.setSmartCurrentLimit(60);

    frontRightSpark.setClosedLoopRampRate(1);
    rearRightSpark.setClosedLoopRampRate(1);
    frontLeftSpark.setClosedLoopRampRate(1);
    rearLeftSpark.setClosedLoopRampRate(1);
    frontRightSpark.setOpenLoopRampRate(1);
    rearRightSpark.setOpenLoopRampRate(1);
    frontLeftSpark.setOpenLoopRampRate(1);
    rearLeftSpark.setOpenLoopRampRate(1);

    frontLeftSpark.setInverted(false);
    frontRightSpark.setInverted(true);
    rearLeftSpark.setInverted(false);
    rearRightSpark.setInverted(false);
    frontRightSpark.setIdleMode(IdleMode.kCoast);
    rearRightSpark.setIdleMode(IdleMode.kCoast);
    frontLeftSpark.setIdleMode(IdleMode.kCoast);
    rearLeftSpark.setIdleMode(IdleMode.kCoast);

    rearRightSpark.follow(frontRightSpark);
    rearLeftSpark.follow(frontLeftSpark);
  }

  @Override
  public void periodic() {
    frontRightSpark.set(0.3);
    rearRightSpark.set(0.3);
    frontLeftSpark.set(0.3);
    rearLeftSpark.set(0.3);
    // This method will be called once per scheduler run
    // /arcadeDrive(driveJoystick.getY(Hand.kLeft), driveJoystick.getX(Hand.kRight));
  }

  public void arcadeDrive(double forward, double turn) {
    sparkDrive.arcadeDrive(forward, turn*0.5);
  }
}
