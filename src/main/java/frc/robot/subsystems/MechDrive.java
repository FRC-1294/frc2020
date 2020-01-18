/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;
import frc.robot.RobotMap;
import edu.wpi.first.wpilibj.command.Subsystem;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.drive.MecanumDrive;


/**
 * Add your docs here.
 */
public class MechDrive extends Subsystem {
  WPI_TalonSRX rightFrontTalon = new WPI_TalonSRX(RobotMap.rightFrontTalon);
  WPI_TalonSRX rightRearTalon = new WPI_TalonSRX(RobotMap.rightRearTalon);
  WPI_TalonSRX leftFrontTalon = new WPI_TalonSRX(RobotMap.leftFrontTalon);
  WPI_TalonSRX leftRearTalon = new WPI_TalonSRX(RobotMap.leftRearTalon);

  XboxController driveControl = new XboxController(0);
  MecanumDrive md = new MecanumDrive(leftFrontTalon, leftRearTalon, rightFrontTalon, rightRearTalon);

  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }
}
