/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.DriveAutoSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class NavigateObstacle extends SequentialCommandGroup {
  /**
   * Creates a new NavigateObstacle.
   */
  public NavigateObstacle(double xDis, double yDis, DriveAutoSubsystem driveAuto) {
    addCommands(
      new TurnByCommand(-90, driveAuto),
      new MoveByCommand(yDis, driveAuto),
      new TurnByCommand(90, driveAuto),
      new MoveByCommand(xDis, driveAuto),
      new TurnByCommand(-90, driveAuto),
      new MoveByCommand(yDis, driveAuto),
      new TurnByCommand(90, driveAuto)
    );
  }
}
