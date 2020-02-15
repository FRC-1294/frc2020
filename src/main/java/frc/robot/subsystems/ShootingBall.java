/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class ShootingBall extends SubsystemBase {
  //creates the private three motors of their type, the shooter, indexer, and intaker
  private TalonFX shooter = new TalonFX(RobotMap.shooterFalcon);
  private TalonSRX indexer = new TalonSRX(RobotMap.indexerTalon);
  private TalonSRX intaker =  new TalonSRX(RobotMap.intakeTalon);
  private TalonSRX colorWheel = new TalonSRX(RobotMap.colorTalon);

  //to conditions to check if it should shoot or intake for those are one button on off systems.
  private boolean toIndex = false;
  private boolean toIntake = false;
  private boolean toColor = false;

  private double ticksPerRev = 1;

  public ShootingBall() {
    shooter.configOpenloopRamp(5);
    shooter.configClosedloopRamp(5);
    shooter.config_kP(0,1);
    shooter.config_kI(0,0);
    shooter.config_kD(0,1);

    shooter.configNominalOutputForward(0);
    shooter.configNominalOutputReverse(0);
    shooter.configPeakOutputForward(1);
    shooter.configPeakOutputReverse(-1);
  }

  //every loop it will check if any of the buttons are pressed and will do the coresponding task related with it
  @Override
  public void periodic() {
    double shooterSpeed = shooter.getSelectedSensorVelocity()*ticksPerRev;
    SmartDashboard.putNumber("Shooter RPM", shooterSpeed);

    if(Robot.m_oi.getAButtonPressed()){
      setSRXSpeed(indexer, 1);
    }
    else {
      setSRXSpeed(indexer, 0);
    }

    if(Robot.m_oi.triggerDrive() != 0){
      setSRXSpeed(intaker, -Robot.m_oi.triggerDrive());
    }
    else {
      setSRXSpeed(intaker, 0);
    }

    if(Robot.m_oi.gameShootingArm.getY(Hand.kLeft) != 0){
      setFXSpeed(shooter, -Robot.m_oi.gameShootingArm.getY(Hand.kLeft));
    }
    else {
      setFXSpeed(shooter, 0);
    }

    if(Robot.m_oi.gameShootingArm.getX(Hand.kRight) != 0){
      setSRXSpeed(colorWheel, -Robot.m_oi.gameShootingArm.getX(Hand.kRight));
    }
    else {
      setSRXSpeed(colorWheel, 0);
    }
  }


  //methods to set the speeds and to create simplicity
  // private void setShooter(double speed){
  //   shooter.set(TalonFXControlMode.PercentOutput, speed);
  // }
  // private void setIntaker(double speed){
  //   intaker.set(ControlMode.PercentOutput, speed);
  // }
  // private void setIndexer(double speed){
  //   indexer.set(ControlMode.PercentOutput, speed);
  // }

  //better methods for increased versatility
  public void setSRXSpeed(TalonSRX controller, double speed) {
    controller.set(ControlMode.PercentOutput, speed);
  }

  public void setFXSpeed(TalonFX controller, double speed) {
    controller.set(TalonFXControlMode.PercentOutput, speed);
  }

  public void setZero() {
    setSRXSpeed(intaker, 0);
    setSRXSpeed(indexer, 0);
    setSRXSpeed(colorWheel, 0);
    setFXSpeed(shooter, 0);
  }
}
