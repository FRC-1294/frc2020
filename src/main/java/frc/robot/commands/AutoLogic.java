/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.ParallelRaceGroup;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class AutoLogic extends ParallelRaceGroup {
  /**
   * Creates a new AutoLogic.
   */
  public AutoLogic(UltraFuseCommand ultraFuse, AutoPath autoPath) {
    // Add your commands in the super() call, e.g.
    super(ultraFuse, autoPath);
  }
}
