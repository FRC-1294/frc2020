/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Robot;
import frc.robot.commands.ToShootBall;

public class ShootingBall extends SubsystemBase {
  private CANSparkMax topMotor = new CANSparkMax(1, MotorType.kBrushless);
  private CANSparkMax bottomMotor = new CANSparkMax(2, MotorType.kBrushless);
  
  XboxController shootButton = new XboxController(3);
  public ShootingBall() {
    topMotor.setOpenLoopRampRate(1);
    bottomMotor.setOpenLoopRampRate(1);
    topMotor.setClosedLoopRampRate(1);
    bottomMotor.setClosedLoopRampRate(1);

  }

  @Override
  public void periodic() {
    if(Robot.m_oi.getDpadGame() <= 360 && Robot.m_oi.getDpadGame() >= 0){
      CommandScheduler.getInstance().schedule(new ToShootBall());
    }
  }

  public void changeSpeed(double speed) {
      topMotor.set(speed);
      bottomMotor.set(-1*speed);
  }
}
