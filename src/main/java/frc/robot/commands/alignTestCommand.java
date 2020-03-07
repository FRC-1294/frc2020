/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DriveAutoSubsystem;
import frc.robot.subsystems.TwentyThreeStabWounds;

//used to test aligning
public class alignTestCommand extends CommandBase {
  /**
   * Creates a new alignTestCommand.
   */

   DriveAutoSubsystem    driveAuto;
   TwentyThreeStabWounds vision;
   DictatorLocator alignToTarget;
   Boolean aligned;
  public alignTestCommand(DriveAutoSubsystem driveAuto, TwentyThreeStabWounds vision) {
    // Use addRequirements() here to declare subsystem dependencies.
    this.driveAuto = driveAuto;
    this.vision    = vision;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    alignToTarget = new DictatorLocator(vision, driveAuto);
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    try{
      SmartDashboard.putNumber("Offset:", alignToTarget.Brutus.getHeadingError());
      SmartDashboard.putBoolean("Done:", alignToTarget.isFinished());
    }catch(NullPointerException e){
      System.out.println("Caught nullPointerException 1");
    }
    if(!alignToTarget.isScheduled()){
      alignToTarget = new DictatorLocator(vision, driveAuto);
      alignToTarget.schedule();
    }
    
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    // return false;
    return alignToTarget.isFinished();
  }
}
