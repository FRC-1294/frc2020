/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DrivingSubsystem extends SubsystemBase {
  public DriveAutoSubsystem m_driveAuto = new DriveAutoSubsystem();
  public XboxController driveJoystick = new XboxController(0);

  public DrivingSubsystem() {
  }

  @Override
  public void periodic() {
    // SmartDashboard.putString("AmountTraveled", m_driveAuto.getAmountTraveled(0) + " , " + m_driveAuto.getAmountTraveled(1));
    // SmartDashboard.putNumber("currentAngle", m_driveAuto.getCurrentAngle());

    if (driveJoystick.getStartButtonPressed()) {
      CommandScheduler.getInstance().cancelAll();
    }

    if (driveJoystick.getBumper(Hand.kRight)) {
      m_driveAuto.setMode("brake");
    }
    else {
      m_driveAuto.setMode("coast");
    }

    m_driveAuto.arcadeDrive(driveJoystick.getY(Hand.kLeft), driveJoystick.getX(Hand.kRight));
  }
}
