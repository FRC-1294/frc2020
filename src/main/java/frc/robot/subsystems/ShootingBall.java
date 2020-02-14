/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class ShootingBall extends SubsystemBase {
  //creates the private three motors of their type, the shooter, indexer, and intaker
  private TalonFX shooter;
  private TalonSRX indexer;
  private TalonSRX intaker;
  private TalonSRX colorWheel;

  //to conditions to check if it should shoot or intake for those are one button on off systems.
  private boolean toShoot = false;
  private boolean toIntake = false;
  private boolean toColor = false;
  //creates the shooting ball with their can id's
  public ShootingBall(){
    this(7, 6, 5, 4);
  }

  public ShootingBall(int shootPort, int indexPort, int intakePort, int colorPort) {
    shooter =  new TalonFX(shootPort);
    indexer =  new TalonSRX(indexPort);
    intaker =  new TalonSRX(intakePort);
    colorWheel = new TalonSRX(colorPort);
    shooter.configOpenloopRamp(5);
    shooter.configClosedloopRamp(5);
  }

  //every loop it will check if any of the buttons are pressed and will do the coresponding task related with it
  @Override
  public void periodic() {
    if(Robot.m_oi.getYButtonPressed()){
      //once clicked, it swaps the task, if it was off before, then it is on (true)
      //if it was on, then it will become off (false)
      toShoot = (!toShoot);
      if(toShoot){
        //if it just become on, then it will set max speed
        setFXSpeed(shooter, 1);//SHOULD NOT BE 1, WILL BE CHANGED TO MATCH 5200 RPM REQUIREMENT
      } else {
        //if it just turned off, then it will set speed to 0
        setFXSpeed(shooter, 0);
      }
    } 

    //exact same concept with the intake as the shoot, just that it controls intaker motor instead
    if(Robot.m_oi.getXButtonPressed()){
      toIntake = (!toIntake);
      if(toIntake){
        setSRXSpeed(intaker, 1);
        //setIntaker(1);
      } else {
        setSRXSpeed(intaker, 0);
        //setIntaker(0);
      }
    }

    //this is the indexer which runs at the speed of how much the trigger is pressed and it is not pressed then is off.
    if(Robot.m_oi.getTriggerRight() != 0){
      setSRXSpeed(indexer, Robot.m_oi.getTriggerRight());
    }

    if(Robot.m_oi.getAButtonPressed()) {
      toColor = (!toColor);
      if(toColor){
        setSRXSpeed(colorWheel, 1);
      } else {
        setSRXSpeed(colorWheel, 0);
      }
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
  private void setSRXSpeed(TalonSRX controller, double speed) {
    controller.set(ControlMode.PercentOutput, speed);
  }

  private void setFXSpeed(TalonFX controller, double speed) {
    controller.set(TalonFXControlMode.PercentOutput, speed);
  }
}
