/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class ShootingBall extends SubsystemBase {
  private TalonFX shooter = new TalonFX(Constants.shooterFalcon);
  private TalonSRX indexer = new TalonSRX(Constants.indexerTalon);
  private TalonSRX intaker =  new TalonSRX(Constants.intakeTalon);
  private TalonSRX colorWheel = new TalonSRX(Constants.colorTalon);
  private boolean toIndex = false;
  private boolean toIntake = false;
  private boolean toColor = false;
  private double ticksPerRev = -2.59;
  private XboxController gameJoystick = new XboxController(Constants.gameJoystick);
  
  public ShootingBall() {
    shooter.configOpenloopRamp(5);
    shooter.configClosedloopRamp(5);
    shooter.config_kP(0, 1);//TO BE TWEAKED
    shooter.config_kI(0, 0);
    shooter.config_kD(0, 1);//TO BE TWEAKED

    shooter.setNeutralMode(NeutralMode.Coast);
    shooter.configNominalOutputForward(0);
    shooter.configNominalOutputReverse(0);
    shooter.configPeakOutputForward(1);
    shooter.configPeakOutputReverse(-1);
  }

  //every loop it will check if any of the buttons are pressed and will do the coresponding task related with it
  @Override
  public void periodic() {
    if(Robot.m_oi.getYButtonPressed()){
      //once clicked, it swaps the task, if it was off before, then it is on (true)
      //if it was on, then it will become off (false)
      toShoot = (!toShoot);
    } 

    



    //exact same concept with the intake as the shoot, just that it controls intaker motor instead
    if(Robot.m_oi.getXButtonPressed()){
      toIntake = (!toIntake);
    }

    if(Robot.m_oi.getBButtonHeld()) {

      setSRXSpeed(intaker, -1);

      setFXSpeed(shooter, -.1);



    } else {
    if(toIntake){
      setSRXSpeed(intaker, 1);
      //setIntaker(1);
    } else {
      setSRXSpeed(intaker, 0);
      //setIntaker(0);
    }

    if(toShoot){
      //if it just become on, then it will set max speed
      setFXSpeed(shooter, 1);//SHOULD NOT BE 1, WILL BE CHANGED TO MATCH 5200 RPM REQUIREMENT
    } else {
      //if it just turned off, then it will set speed to 0
      setFXSpeed(shooter, 0);
    }
  }

    //this is the indexer which runs at the speed of how much the trigger is pressed and it is not pressed then is off.
    if(Robot.m_oi.getTriggerRight() != 0){
      setSRXSpeed(indexer, Robot.m_oi.getTriggerRight());
    }

    //shooter
    if(gameJoystick.getY(Hand.kLeft) != 0){
      setFXSpeed(shooter, gameJoystick.getY(Hand.kLeft));
    }
    else {
      setFXSpeed(shooter, 0);
    }

    //colorer
    if(gameJoystick.getX(Hand.kRight) != 0){
      setSRXSpeed(colorWheel, -gameJoystick.getX(Hand.kRight));
    }
    else {
      setSRXSpeed(colorWheel, 0);
    }
  }

  public double triggerDrive() {
    return gameJoystick.getTriggerAxis(Hand.kRight) - gameJoystick.getTriggerAxis(Hand.kLeft);
  }

  //better methods for increased versatility
  public void setSRXSpeed(TalonSRX controller, double speed) {
    controller.set(ControlMode.PercentOutput, speed);
  }

  public void setFXSpeed(TalonFX controller, double speed) {
    controller.set(TalonFXControlMode.PercentOutput, speed);
  }

  // public void setIndexer(double speed) {
  //   indexer.set(ControlMode.PercentOutput, speed);
  // }

  // public void setShooterPID(double velocity) {
  //   shooter.set(TalonFXControlMode.Velocity, velocity);
  // }

  // public void setShooter(double speed) {
  //   shooter.set(TalonFXControlMode.PercentOutput, speed);
  // }

  // public int getShooterVelocity() {
  //   return shooter.getSelectedSensorVelocity();
  // }

  public void setZero() {
  //  setSRXSpeed(intaker, 0);
   // setSRXSpeed(indexer, 0);
    // setSRXSpeed(colorWheel, 0);
    // setFXSpeed(shooter, 0);
  }
}
