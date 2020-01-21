/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import frc.robot.commands.MoveByCommand;
import frc.robot.commands.TurnByCommand;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.ControlType;
import com.revrobotics.CANPIDController;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Gains;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

public class DriveSubsystem extends SubsystemBase {
  public CANSparkMax frontLeftSpark = new CANSparkMax(1, MotorType.kBrushless);
  public CANPIDController frontLeftPID = frontLeftSpark.getPIDController();
  public CANSparkMax frontRightSpark = new CANSparkMax(2, MotorType.kBrushless);
  public CANPIDController frontRightPID = frontRightSpark.getPIDController();

  public WPI_TalonSRX rearLeftTalon = new WPI_TalonSRX(3);
  public WPI_TalonSRX rearRightTalon = new WPI_TalonSRX(4);

  XboxController joystick = new XboxController(0);

  public final AHRS navX = new AHRS(SPI.Port.kMXP);

  public static final int kSlotIdx = 0;
	public static final int kPIDLoopIdx = 0;
	public static final int kTimeoutMs = 30;
	public static boolean kSensorPhase = true;
  public static boolean kMotorInvert = false;
  public boolean lock = false;
  static final Gains kGains = new Gains(0.5, 0.00001, 1.0, 0.0, 0.0, -0.5, 0.5);
  double targetPositionRotations = 4096/2;
  double prevPos = 0;
  XboxController driveJoystick = new XboxController(0);
  JoystickButton driveXButton = new JoystickButton(driveJoystick, 3);

  public DriveSubsystem() {
    frontLeftSpark.setInverted(true);
    rearRightTalon.setInverted(true);
    setPidControllers(frontLeftPID);
    setPidControllers(frontRightPID);
  }

  @Override
  public void periodic() {
   // SmartDashboard.putNumber("PID/leftEncoder", frontLeftSpark.getEncoder().getPosition());
   // SmartDashboard.putNumber("PID/rightEncoder", frontRightSpark.getEncoder().getPosition());
    
    if (driveJoystick.getBumper(Hand.kRight) && !this.lock) {
      CommandScheduler.getInstance().schedule(new TurnByCommand(-90));
    }
    else if (driveJoystick.getBumper(Hand.kLeft) && !this.lock) {
      CommandScheduler.getInstance().schedule(new TurnByCommand(90));
    }
    else if (driveJoystick.getAButtonPressed() && !this.lock) {
      CommandScheduler.getInstance().schedule(new MoveByCommand(1));
    }
    else if (driveJoystick.getBButtonPressed() && !this.lock) {
      CommandScheduler.getInstance().schedule(new MoveByCommand(1));
    }
  }

  public void setPidControllers (CANPIDController pidController) {
    pidController.setP(kGains.kP);
    pidController.setI(kGains.kI);
    pidController.setD(kGains.kD);
    pidController.setIZone(kGains.kIz);
    pidController.setFF(kGains.kFF);
    pidController.setOutputRange(kGains.kMinOutput, kGains.kMaxOutput);
  }
}