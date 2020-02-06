/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.commands.ToShootBall;

public class ShootingBall extends SubsystemBase {
  private CANSparkMax topMotor = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax bottomMotor = new CANSparkMax(2, MotorType.kBrushless);
  
  public ShootingBall() {
    topMotor.setOpenLoopRampRate(1);
    bottomMotor.setOpenLoopRampRate(1);
    topMotor.setClosedLoopRampRate(1);
    bottomMotor.setClosedLoopRampRate(1);
  }

  @Override
  public void periodic() {
    if(Robot.m_oi.getAButtonPressed()){
      CommandScheduler.getInstance().schedule(new ToShootBall(this, "shoot"));
    }
    if(Robot.m_oi.getXButtonPressed()){
      CommandScheduler.getInstance().schedule(new ToShootBall(this, "intake"));
    }
  }

  public void intake(){
    setSpeedBottom(1);
  }
  public void shoot(Double speed){
    setSpeedTop(speed);
    setSpeedBottom(-1*speed);
  }
  public void stopMotors(){
    setSpeedTop(0);
    setSpeedBottom(0);
  }
  private void setSpeedTop(double speed) {
      topMotor.set(speed);
  }
  private void setSpeedBottom(double speed){
      bottomMotor.set(speed);
  }
}
