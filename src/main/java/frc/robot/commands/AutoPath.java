/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.DelayCommand;
import frc.robot.commands.MoveByCommand;
import frc.robot.commands.TurnByCommand;
import frc.robot.subsystems.DriveAutoSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutoPath extends SequentialCommandGroup {
  /**
   * Creates a new nlasduh.
   */
  public AutoPath(int driveDis, int delay, DriveAutoSubsystem driveAuto) {
    addCommands(
      new MoveByCommand(driveDis, driveAuto)
    );

    addCommands(
    new TurnByCommand(180, driveAuto),
    //new DelayCommand(delay),
    new MoveByCommand(driveDis, driveAuto));
  }
}