/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.subsystems.DriveAutoSubsystem;

public class AutoNavCommand extends CommandBase {
  int iterations = 0;
  boolean isFinished = false;
  DriveAutoSubsystem m_driveAuto;
  AutoPath autoPath;

  public AutoNavCommand(DriveAutoSubsystem driveAuto) {
    // Use addRequirements() here to declare subsystem dependencies.
    addRequirements(driveAuto);
    autoPath = new AutoPath(5*12, 2000, driveAuto);
    m_driveAuto = driveAuto;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    CommandScheduler.getInstance().schedule(autoPath);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    //if (!detectObstacle.isScheduled()) {
    //  autoPath.cancel();
    //  frontLeftSpark.set(0);
    //  frontRightSpark.set(0);
    //  rearLeftSpark.set(0);
    //  rearRightSpark.set(0);
    //  CommandScheduler.getInstance().schedule(avoidObstacle);
    //  if(!avoidObstacle.isScheduled()) {
    //    CommandScheduler.getInstance().schedule(detectObstacle);
    //    autoPath = new SequentialCommandGroup();
    //    autoPath.addCommands(new DelayCommand(2000));
    //    if (step == 0) {
    //      autoPath.addCommands(new MoveByCommand(5*12));
    //      autoPath.addCommands(new DelayCommand(2000));
    //    }
    //    if (step <= 1) {
    //       autoPath.addCommands(new TurnByCommand(180));
    //       autoPath.addCommands(new DelayCommand(2000));
    //    }
    //    if (step <= 2) {
    //       autoPath.addCommands(new MoveByCommand(5*12));
    //    }
    //    CommandScheduler.getInstance().schedule(autoPath);
    //  }
    //}
    
    if (autoPath.isFinished()) {
      isFinished = true;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_driveAuto.setFrontLeftSpeed(0);
    m_driveAuto.setFrontRightSpeed(0);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return isFinished;
  }
}
