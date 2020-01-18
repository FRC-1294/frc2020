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
import com.revrobotics.CANError;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import frc.robot.Robot;


public class TestSubsystem extends Subsystem {
  CANSparkMax rightFrontTalon = new CANSparkMax(RobotMap.rightFrontTalon, MotorType.kBrushless);
  WPI_TalonSRX rightRearTalon = new WPI_TalonSRX(RobotMap.rightRearTalon);
  CANSparkMax leftFrontTalon = new CANSparkMax(RobotMap.leftFrontTalon, MotorType.kBrushless);
  WPI_TalonSRX leftRearTalon = new WPI_TalonSRX(RobotMap.leftRearTalon);
  DifferentialDrive sparkDrive = new DifferentialDrive(leftFrontTalon,rightFrontTalon);
  DifferentialDrive talonDrive = new DifferentialDrive(leftRearTalon,rightRearTalon);
  XboxController driveControl = new XboxController(0);

  public void arcadeDrive(double forward, double turn) {
    sparkDrive.arcadeDrive(-forward, turn);
    talonDrive.arcadeDrive(forward, turn);
  }

  public void initDefaultCommand() {
    setDefaultCommand(new TestCommand());
  }
}
