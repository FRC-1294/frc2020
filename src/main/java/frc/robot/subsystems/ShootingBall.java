/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class ShootingBall extends SubsystemBase {
  private TalonFX shooter;
  private TalonSRX indexer;
  private TalonSRX intaker;

  private boolean toShoot = false;
  private boolean toIntake = false;
  public ShootingBall(){
    this(7, 6, 5);
  }

  public ShootingBall(int shootPort, int indexPort, int intakePort) {
    shooter =  new TalonFX(shootPort);
    indexer =  new TalonSRX(indexPort);
    intaker =  new TalonSRX(intakePort);
  }

  @Override
  public void periodic() {
    if(Robot.m_oi.getYButtonPressed()){
      toShoot = (!toShoot);
      if(toShoot){
        setShooter(1);
      } else {
        setShooter(0);
      }
    } 
    if(Robot.m_oi.getXButtonPressed()){
      toIntake = (!toIntake);
      if(toIntake){
        setIntaker(1);
      } else {
        setIntaker(0);
      }
    }

    if(Robot.m_oi.getTriggerRight() != 0){
      setIndexer(Robot.m_oi.getTriggerRight());
    }
  }

  private void setShooter(double speed){
     shooter.set(TalonFXControlMode.PercentOutput, speed);
  }
  private void setIntaker(double speed){
    intaker.set(ControlMode.PercentOutput, speed);
  }
  private void setIndexer(double speed){
    indexer.set(ControlMode.PercentOutput, speed);
  }
}
